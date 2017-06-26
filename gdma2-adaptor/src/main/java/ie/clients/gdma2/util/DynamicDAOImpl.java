package ie.clients.gdma2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ColumnDataUpdate;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.DropDownColumn;
import ie.clients.gdma2.domain.ui.Filter;
import ie.clients.gdma2.spi.ServiceException;
import ie.clients.gdma2.spi.interfaces.UserContextProvider;
import ie.clients.gdma2.util.TableRowDTO.TableColumn;

@Repository
public class DynamicDAOImpl implements DynamicDAO{

	private static final Logger logger = LoggerFactory.getLogger(DynamicDAOImpl.class);

	@Autowired
	protected RepositoryManager repositoryManager;

	@Autowired
	protected UserContextProvider userContextProvider;

	@Autowired
	private DataSourcePool dataSourcePool;

	@Override
	public void setDataSourcePool(DataSourcePool dataSourcePool) {
		this.dataSourcePool = dataSourcePool;
	}

	@Override
	public DataSourcePool getDataSourcePool() {
		return this.dataSourcePool;
	}

	/**
	 * Queries DB column 'connection_types_gdma2.select_get_tables' e.g. 'SHOW TABLES' for MySQL
	 * result must be single column containing all DB Table names on Server 
	 * @param Server server
	 * @param SqlGetTables - SQL query
	 * @return List of Table names on Server
	 */
	public List<String> getSqlGetTables(Server server, String SqlGetTables){
		logger.info("getSqlGetTables(): " + SqlGetTables + ", for server: " + server.getId());
		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);
		List<String> tableList  = jdbcTemplate.queryForList(SqlGetTables, String.class);
		return tableList;
	}


	/**
	 * Uses metadata to fetch DB Columns for given DB Table on selected Server
	 * maps column metadata to Column Entity
	 * creates Set of Column Entities to represent all Columns of Table
	 * @param Server server 
	 * @param String tableName
	 * @return Set of Column Entities
	 */
	@Override
	public Set<Column> getTableColumns(Server server, String tableName) {
		logger.info("getTableColumns for table: " + tableName);
		//String sqlTableMetadata = "select * from " + tableName + " where 1 = 0";
		String sqlTableMetadata = SQLUtil.createSelectColumnMetadata(server, tableName);
		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);
		Set<Column> columns = jdbcTemplate.query(sqlTableMetadata, new ResultSetExtractorColumns());

		determinePrimaryKey(jdbcTemplate, tableName, columns);

		for (Column column : columns) {
			logger.info("remote table column: " + column.getName() + ", is PK: " + column.isPrimarykey());
		}
		return columns;


	}

	/*
	 * determine PK,
	 * by default Column.primarykey = false;
	 * in caller  - ResultSetExtractorColumns PK could now be determined (only combination of isAutoincrement + isNotNull could be used there
	 * as criteria)
	 *   so another approach is to use DatabaseMetaData from connection
	 *  //TODO make sure handling exceptions and see if connection closing is needed here or DS takes care of it
	 * */
	private void determinePrimaryKey(JdbcTemplate jdbcTemplate,
			String tableName, Set<Column> columns)  {
		logger.info("determinePrimaryKey");
		Connection connection = null;
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet primaryKeysRS = meta.getPrimaryKeys(null, null, tableName);

			while(primaryKeysRS.next()){
				String columnNamePK = primaryKeysRS.getString("COLUMN_NAME");
				logger.info("PK column found: " + columnNamePK);
				for (Column column : columns) {
					if( columnNamePK.equalsIgnoreCase(column.getName())){
						column.setPrimarykey(true);
						column.setAllowUpdate(false);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("Error while getting PK column  " + e);
			//TODO !!!
		} 
		finally {
			//without this block code hangs at connection = jdbcTemplate.getDataSource().getConnection(); after several ittrations
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


	}




	/**
	 * Synch Tables for selected Server (double clicked Server in Admin Module) between Remote DB server and GDMA DB server 
	 */
	public void synchTablesForServer(Server server, List<Table> tableList) {
		// TODO use an AOP trigger for this
		logger.info("starting table SYNCH");
		Set<Table> resyncTableList = resyncTableList(server, tableList);
		//serverDao.save(server);OLD CODE
		logger.info("saving tablesSynchResult");
		//TODO TX !!! save RESULT: - return to metaDataSerice caller where @Transactionl save method is and save there !!! 
		repositoryManager.getTableRepository().save(resyncTableList);
		logger.info("...table SYNCH ended");
	}


	/**

	 * 
	 * GDMA1:  Called from	 GdmaAdminAjaxFacade:
	 *   Only called from admin and it's a special case. We need to re-sync the tables before calling it,
	 *    just to ensure that the list is current
	 * 	
	 * This method will get the list of tables from the remote database
	 *  and then iterate over the list of tables form the server object in GDMA DB.
	 *  It will see if any are missing,. If there is, then it will create a new table object IN GDMA DB and save it.
	 * 

	 *****************************
	  Maintaining list of Tables on Server.
	 *************************************

		FIRST TIME: 
		When Admin user connects to ‘Maintained/REMOTE’ server for the first time, there are no Tables for that Server in ‘Metadata/GDMA’ yet. 
		App logic is executed – all Tables and their Columns are scanned from ‘REMOTE’ DB and Stored in ‘GDMA’ DB. <BR>

		AFTER FIRST TIME: 
		After Initial usage, ‘REMOTE’ DB can be changed : 


		1. TABLE NAMES COMPARE (REMOTE -> GDMA)  : 
			Tables names are in synch if Table names are equal and Table in ‘GDMA’ DB is ACTIVE.

			1.1	 TABLE NAMES MATCH 

			 	a)	...and GDMA table is ACTIVE
					Add existing GDMA table to the result Set of Tables

				 b) ...but ‘GDMA’ Table is NOT ACTIVE, then make 2 changes: 

			 		b1) UPDATE Inactive GDMA table name - add Timestamp suffix : TABLENAME_timestamp and change ACTIVE flag to TRUE <BR>
					b1) Add back existing GDMA table to the result Set of Tables (now with UPDATEed name)

					b2) Create new Entity Table using name of Remote DB, set serverId, set active to TRUE
					b2) Add New Table to result Set of Tables 

			1.2		NO MATCH - 
				  Add New Table to result Set of Tables - Create new Entity Table using name of Remote DB, set serverId, set active to TRUE

		2. TABLE NAMES COMPARE (GDMA -> REMOTE), DELETED from GDMA 

		-	DELETED table in ‘Maintained’ DB (still existing in ‘Metadata’ DB) 

			BIZ Rule: If the table has been DELETED from the 'maintained' database then:

			a)	make it inactive in GDMA, so that it is unavailable to users but remains in the database for auditing purposes.
				Add back existing GDMA table to the result Set of Tables (now deactivated)

			b)	DELETE all records in link table USER_ACCESS for that deactivated tableId

			c1)	create list of ColumnIds for all columns that belong to deactivated table
			c2) get all GDMA tables from server and
			      iterate over all Columns for all tables
			         iterate over ColumnsIds list of deactivated table 
			            if column from deactivated Table is found as 'DropDownColumnDisplay' 'or getDropDownColumnStore' in any column (c) from any table
			               			c.setDropDownColumnDisplay(null);
            						c.setDropDownColumnStore(null); 

        END: 
         save     result Set of Tables						
	 * @param tableList 

	 */
	private Set<Table> resyncTableList(Server server, List<Table> tableList) {

		logger.info("resyncTableList: " + server.getId());
		//TODO define in parent interface, separate SYNHC logic from dynamicDAO logic used for DATA and not ADMIN module??

		//RESULT of synch   
		Set<Table> tablesSynchResult = new HashSet<Table>(); 

		//GDMA tables list
		//Set<Table> tablesGDMA = server.getTables();
		Set<Table> tablesGDMA = new HashSet<Table>(tableList);

		logger.info("Number of tables in GDMA: " + tablesGDMA.size());

		//Remote server table list
		List<String> tableNameListRemoteDB = getSqlGetTables(server, server.getConnectionType().getSqlGetTables());
		printRemoteDBTableDetails(tableNameListRemoteDB);

		//LOOP remote DB table names
		for (String tableNameRemote : tableNameListRemoteDB) {
			logger.info("checking : tableNameRemote:" + tableNameRemote);

			Table tableGDMA = remoteTableAlreadyExistsInGDMA(tableNameRemote, tablesGDMA);
			if(tableGDMA != null) {
				//1.1	 TABLE NAMES MATCH
				logger.info("	remote table: " + tableNameRemote + " is found on local");
				resolveTablesWithSameNames(tablesSynchResult, tableGDMA, tableNameRemote, server);
			} else {
				//1.2	NO MATCH  
				logger.info("    remote table:" + tableNameRemote + " not found on local DB");
				createNewGDMATable(tablesSynchResult, tableNameRemote, server);
			}


		}//for1

		//previous double loop is ended, now compare in other direction to detect GDMA tables that don't exist on remote Server - Deleted
		logger.info("-----------------");
		resolveDeletedTables(tablesSynchResult, tablesGDMA, tableNameListRemoteDB);

		printSynchResult(tablesSynchResult);
		return tablesSynchResult;
	}


	private Table remoteTableAlreadyExistsInGDMA(String tableNameRemote, Set<Table> tablesGDMA){
		for (Table tableGDMA : tablesGDMA) {
			if(tableNameRemote.equalsIgnoreCase(tableGDMA.getName()))
				return tableGDMA;
		}
		return null;
	}

	private Table localTableStillExistsOnRemoteServer(Table tableGDMA, List<String> remoteTableNames){

		for (String remoteTableName : remoteTableNames) {
			if(remoteTableName.equalsIgnoreCase(tableGDMA.getName())){
				return tableGDMA;
			}
		}
		return null;
	}

	/**
	 * see above comment: 
	 * 	1.1 a) ACTIVE
	 *  1.1 b) not ACTIVE
	 * 
	 * @param tablesSynchResult
	 * @param tableGDMA
	 * @param tableNameRemote
	 * @param server
	 */
	private void resolveTablesWithSameNames(Set<Table> tablesSynchResult,
			Table tableGDMA, String tableNameRemote, Server server) {

		logger.info("resolveTablesWithSameNames: " + tableNameRemote);

		if (tableGDMA.isActive()) {
			logger.info("active table with same name found: " + tableGDMA.getName() + ", keeping old table");
			tablesSynchResult.add(tableGDMA);
		} else {
			logger.info("inactive table found: " + tableGDMA.getName());
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			String timestamp = currentTimestamp.toString();

			//rename inactive table
			tableGDMA.setName(tableGDMA.getName() + "_" + timestamp);
			tableGDMA.setAlias(tableGDMA.getName() + "_" + timestamp);
			logger.info("inactive table renamed to: " + tableGDMA.getName());
			//add to result
			tablesSynchResult.add(tableGDMA);

			//add active Table to GDMA
			createNewGDMATable(tablesSynchResult, tableNameRemote, server);

		}

	}

	/** There is a new DB table on Remote Server, create new Active Table and add to result Table set */
	private void createNewGDMATable(Set<Table> tablesSynchResult, String tableNameRemote, Server server) {
		logger.info("createNewGDMATable: " + tableNameRemote);
		Table table = new Table();
		table.setName(tableNameRemote);
		table.setServer(server);
		table.setActive(true);
		table.setAlias(tableNameRemote);
		tablesSynchResult.add(table);
	}

	/** see above comment: 2. TABLE NAMES COMPARE (GDMA -> REMOTE), Deleted from GDMA  
	 * 
	 * search for DELETED tables - if local GDMA table is not anymore in remote table list
	 * deactivate it, add to result Table set, 
	 * remove UserAccess, 
	 * and resolve column dependencies for DropDownColumnStore columns and DropDownColumnDisplay columns in foreign tables
	 * @param tablesSynchResult
	 * @param tablesGDMA
	 * @param tableNameListRemoteDB
	 * @param server
	 */
	private void resolveDeletedTables(Set<Table> tablesSynchResult,
			Set<Table> tablesGDMA, List<String> tableNameListRemoteDB) {
		logger.info("resolveDeletedTables");

		for(Table tableGDMA : tablesGDMA){

			Table table = localTableStillExistsOnRemoteServer(tableGDMA, tableNameListRemoteDB);

			//if (table != null) - table still exists, skip and and continue searching for non-existing
			if( table == null) {

				//deactivate tableGDMA
				tableGDMA.setActive(false);
				logger.info("table :" + tableGDMA.getName() + " is not in remote table list anymore, deactivating table");

				//add deactivated table to result table set
				tablesSynchResult.add(tableGDMA);

				//TODO ? Where to place this and  how to make TRANSACTION!?!
				logger.info("deleting UserAccess for table: " + tableGDMA.getName());
				List<UserAccess> userAcceseList = repositoryManager.getUserAccessRepository().findByTableId(tableGDMA.getId());
				repositoryManager.getUserAccessRepository().delete(userAcceseList);

				//resolve columns
				resolveColumnsForDeactivatedTable(tableGDMA, tablesGDMA);

			}//if
		}//for
	}


	/**
	 * Iterate over all columns on all tables on server,
	 *  and see if some column from Deactivated Table above is present there as DropDown column, if so : null them 
	 *  save changes
	 * @param tableDeactiveGDMA
	 * @param tablesGDMA 
	 * @param server
	 */
	private void resolveColumnsForDeactivatedTable(Table tableDeactiveGDMA, Set<Table> tablesGDMA) {
		logger.info("resolveColumnsForDeactivatedTable: " + tableDeactiveGDMA.getName());

		//Set<Column> columnsDeactiveGDMA = tableDeactiveGDMA.getColumns();
		Set<Column> columnsDeactiveGDMA = repositoryManager.getColumnRepository().findByTableId(tableDeactiveGDMA.getId());
		List<Integer> columnDeactiveIdList = new ArrayList<Integer>();
		for(Column columnGDMA: columnsDeactiveGDMA){
			columnDeactiveIdList.add(columnGDMA.getId());
		}

		//TODO !!! make sure Server -> table -> columns are loaded because of tranzient nature!
		//Set<Table> tablesGDMA = server.getTables();
		logger.info("Number of tables to check against: " + tablesGDMA.size());

		//locate if any column of inactive table is maybe FK in any other column of any table on the same server
		//precondition is that step2 resynch column is done and that columns are already there. 
		for (Table tableGDMA : tablesGDMA) {
			logger.info("tableGDMA.getName(): " + tableGDMA.getName());

			logger.info("Loading all columns for table...");
			//Set<Column> columnsGDMA = tableGDMA.getColumns(); EMPTY !!!
			Set<Column> columnsGDMA = repositoryManager.getColumnRepository().findByTableId(tableGDMA.getId());

			for (Column col : columnsGDMA) {
				logger.info("columnsGDMA: " + col.getName());
				for(Integer colDeactiveId : columnDeactiveIdList){
					if (col.getDropDownColumnDisplay() != null && col.getDropDownColumnDisplay().getId() == colDeactiveId){
						logger.info("colName: " + col.getName());
						col.setDropDownColumnDisplay(null);
						col.setDropDownColumnStore(null);
						logger.info("Removed foreign key reference from column " + col.getName() + " in table: " + tableGDMA.getName());
					}

					if(col.getDropDownColumnStore() != null && col.getDropDownColumnStore().getId() == colDeactiveId){
						col.setDropDownColumnDisplay(null);
						col.setDropDownColumnStore(null);
						logger.info("Removed foreign key reference from column " + col.getName() + " in table: " + tableGDMA.getName());
					}

				}//for3
			}//for2

			//save all updates columns changed
			logger.info("saving column updates");
			//EntityUtils.applyColumnRules(columnsGDMA);//no column rules needed here
			repositoryManager.getColumnRepository().save(columnsGDMA);
			logger.info("...saving ended");

		}//for1

		//TODO - Persist all columns for all tables on server that where just changed!!! - TX
	}

	private void printRemoteDBTableDetails(List<String> tableNameListRemoteDB){
		logger.info("Number of tables in Remote DB: " + tableNameListRemoteDB.size());
		for(String tableName : tableNameListRemoteDB){
			logger.info("remote DB table name: " + tableName);
		}
	}

	private void printSynchResult(Set<Table> tablesSynchResult) {
		logger.info("printSynchResult...");
		for (Table table : tablesSynchResult) {
			logger.info("synched table name: " + table.getName());
		}
	}


	@Override
	public void synchColumnsForTable(Server server, Table table, Set<Column> columns) {
		logger.info("synchColumnsForTable");

		logger.info("starting column SYNCH");
		Set<Column> resyncColumnList = resyncColumnList(server, table, columns);
		logger.info("...column SYNCH ended");

		repositoryManager.getColumnRepository().save(resyncColumnList);
		//reload table list after synch
		//TODO OPEN Q - after saving columns - updating table that contains it ?

	}



	private Set<Column> resyncColumnList(Server server, Table tableGDMA, Set<Column> columnsGDMA) {
		logger.info("resyncColumnList, for table: " + tableGDMA.getName());

		//RESULT of synch   
		Set<Column> columnsSynchResult = new HashSet<Column>(); 

		//GDMA column list
		logger.info("columnsGDMA size: " + ( columnsGDMA == null ? 0 : columnsGDMA.size() ) );

		//Remote server, column list for table
		Set<Column> columnsRemote = getTableColumns(server, tableGDMA.getName());

		//set parent
		for (Column column : columnsRemote) {
			column.setTable(tableGDMA);
		}

		logger.info("Number of columns in remote Table: " + columnsRemote.size());
		//printRemoteDBTableDetails(tableNameListRemoteDB);
		for (Column columnRemote : columnsRemote) {
			logger.info("remote column: " + columnRemote.getName());

			Column columnGDMA = remoteColumnAlreadyExistsInGDMA(columnRemote.getName(),columnsGDMA);
			if(columnGDMA == null){// no, so create a new column (see ResultSetExtractorColumns) and add to columnsGDMA
				logger.info("remote column does not exists in GDMA, creating new column: " + columnRemote.getName());
				//rr columnsGDMA.add(columnRemote);//ADD
				columnsSynchResult.add(columnRemote);
				logger.info(" *** columnsSynchResult : " + columnsSynchResult.size());
			} else {	
				resolveColumnWithTheSameName(columnRemote, columnGDMA, columnsSynchResult);
			}

		}//for

		//previous double loop is ended, now compare in other direction - sure none GDMA columns were deleted & reset
		logger.info("-----------------");
		//tableDao.save(table);

		resolveDeletedAndResetedColumns(server,columnsRemote, columnsGDMA, columnsSynchResult);

		//finally save all synched columns
		return columnsSynchResult;

	}//end




	/**
	 * for every GDMA column that is deleted or reset:
	 *  - deactivate it and set dispyaed to false
	 *  - fetch all columns from all tables on server and see if deleted Col is FK, 
	 *    if so remove references in getDropDownColumnStore and getDropDownColumnDisplay and save table after change
	 *  
	 * @param server
	 * @param columnsRemote
	 * @param columnsGDMA
	 * @param columnsSynchResult 
	 */
	private void resolveDeletedAndResetedColumns(Server server, Set<Column> columnsRemote, Set<Column> columnsGDMA, Set<Column> columnsSynchResult) {
		logger.info("resolveDeletedAndResetedColumns");
		// the orderby
		//int idx = 0; //TODO if needed for existing
		Set<Column> nonExisingtColumns = nonExisingtColumns(columnsRemote, columnsGDMA);

		for (Column columnGDMA : nonExisingtColumns) {
			//columns.remove(column);
			logger.info("columnGDMA: " + columnGDMA.getName());

			columnGDMA.setDisplayed(false);
			columnGDMA.setActive(false);

			//add deactivated table to result table set
			columnsSynchResult.add(columnGDMA); //will not add existing previously deactivated with timestamp added to name

			logger.info(" *** columnsSynchResult : " + columnsSynchResult.size());

			List<Table> allTablesOnServer = repositoryManager.getTableRepository().findByServerId(server.getId());
			Set<Table> allTables = new HashSet<Table>(allTablesOnServer);

			for (Table t : allTables) {
				Set<Column> tableColumns = t.getColumns();

				for(Column c: tableColumns){

					if(c.getDropDownColumnDisplay() != null && (c.getDropDownColumnDisplay().getId() == columnGDMA.getId())){
						c.setDropDownColumnStore(null);
						c.setDropDownColumnDisplay(null);

						//SAVE TABLE
						repositoryManager.getTableRepository().save(t);

						logger.info("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
					}  

					if(c.getDropDownColumnStore() != null && (c.getDropDownColumnStore().getId() == columnGDMA.getId())){
						c.setDropDownColumnStore(null);
						c.setDropDownColumnDisplay(null);

						//SAVE TABLE
						repositoryManager.getTableRepository().save(t);

						logger.info("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
					}  

				}//for3
			}//for2
		}//for1


		/* TODO if needed - for existing
		else {
			columnGDMA.setOrderby(idx);
			idx++;
		}
		 */

	}

	private void resolveColumnWithTheSameName(Column columnRemote, Column columnGDMA, Set<Column> columnsSynchResult) {
		logger.info("column already exists...");
		if (columnGDMA.isActive()) {
			// update type - just in case
			logger.info(".... and is active, updating: " + columnGDMA.getName());
			columnGDMA.setColumnType(columnRemote.getColumnType());
			columnGDMA.setColumnTypeString(columnRemote.getColumnTypeString());
			columnGDMA.setNullable(columnRemote.isNullable());
			columnGDMA.setColumnSize(columnRemote.getColumnSize());
			columnGDMA.setActive(true);

			columnsSynchResult.add(columnGDMA);
			logger.info(" *** columnsSynchResult : " + columnsSynchResult.size());

		} else {
			//change name of deactivated and create new using Remote
			logger.info("... and is inactive column : " + columnGDMA.getName());
			Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
			String timestamp = currentTimestamp.toString();
			String inactiveColumnName = columnGDMA.getName() + "_" + timestamp;

			columnGDMA.setName(inactiveColumnName);
			columnGDMA.setActive(false);
			columnsSynchResult.add(columnGDMA);

			logger.info(" *** columnsSynchResult : " + columnsSynchResult.size());
			logger.info("column deactivated and name changed: " + columnGDMA.getName());
			logger.info("creating new column: " + columnRemote.getName());

			boolean addSuccess = columnsSynchResult.add(columnRemote);
			logger.info("addSuccess:" + addSuccess);
			logger.info(" *** columnsSynchResult : " + columnsSynchResult.size());
		}

	}

	private Column remoteColumnAlreadyExistsInGDMA(String columnNameRemote, Set<Column> columnsGDMA) {
		for (Column columnGDMA : columnsGDMA) {
			if (columnNameRemote.equalsIgnoreCase(columnGDMA.getName())) {
				return columnGDMA;
			}
		}
		return null;
	}


	//Columns in GDMA that don't exist in remote DB anymore
	private Set<Column> nonExisingtColumns(Set<Column> remoteColumns, Set<Column> GDMAcolumns){
		logger.info("nonExisingtColumns");
		Set<Column> nonExistingColumns = new HashSet<Column>();
		for (Column colGDMA : GDMAcolumns) {
			if(!columnNameExists(colGDMA.getName(), remoteColumns)){
				nonExistingColumns.add(colGDMA);
			};
		}

		logger.info("nonExisingtColumns size: " + ( nonExistingColumns == null ? 0 : nonExistingColumns.size() ) );
		return nonExistingColumns;
	}

	private boolean columnNameExists(String colName, Set<Column> columns){
		for (Column col : columns) {
			if(colName.equalsIgnoreCase(col.getName())){
				return true;
			}
		}
		return false;
	}


	/*DATA MODULE SECTION*/




	/*	 sql: SELECT count(1)  FROM customers WHERE + filters*/
	@Override
	public Long getCount(Server server, Table table, List<Filter> filters) {
		// TODO optimise!!
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());
		final String sql = SQLUtil.createCount(server, table, filters);
		logger.info("getCount sql: " + sql);

		//DEPRICATED return jdbcTemplate.queryForLong(sql, convertFiltersToSqlParameterValues(filters).toArray());
		// NOW: jdbcTemplate.queryForObject(sql, Long.class);

		return	jdbcTemplate.queryForObject(sql, convertFiltersToSqlParameterValues(filters).toArray(), Long.class); 

	}

	private void declareSqlParameters(PreparedStatementCreatorFactory psc, List<Filter> filters, Server server) {
		for (Filter filter : filters) {
			//if filter is null or blank
			if ((filter.getFilterOperator() == 8 || filter.getFilterOperator() == 9))
				continue;
			if (StringUtils.hasText(filter.getFilterValue())) {
				//Teradata and Oracle 
				if (server.getConnectionUrl().contains("jdbc:teradata"))					
				{                                       
					if(filter.getColumnType() == 91)
					{
						psc.addParameter(new SqlParameter(93));
					}
					else
					{
						psc.addParameter(new SqlParameter(filter.getColumnType()));
					}
				}
				else
				{
					psc.addParameter(new SqlParameter(filter.getColumnType()));
				}				
			}
		}
	}


	private List<Object> convertFiltersToSqlParameterValues(List<Filter> filters) {
		final List<Object> params = new ArrayList<Object>();
		for (Filter filter : filters) {
			//if ((filter.isNullValue() || filter.isBlank())&&(filter.getFilterValue()== null))
			if ((filter.getFilterOperator() == 8 || filter.getFilterOperator() == 9))
				continue;
			//if (StringUtils.hasText(filter.getFilterValue())) {
			if (!(filter.getFilterOperator() == 8 || filter.getFilterOperator() == 9)){
				Object param = filter.getFilterValue();
				if (SQLUtil.isNumeric(filter.getColumnType())) {
					logger.info("Number as string as parameter: " + param);
				} else if (SQLUtil.isDate(filter.getColumnType())) {
					logger.info("DATE filter detected: " + filter.getFilterValue());
					try {
						param = Formatter.formatDate(Formatter.parseDate(filter.getFilterValue()));
						logger.info("Date as parameter: " + param);
					} catch (Exception ex) {
						logger.info("Could not parse the date: " + filter.getFilterValue(), ex);
					}
				} else if (SQLUtil.isDateTime(filter.getColumnType())) {
					logger.info("DATETIME filter detected: " + filter.getFilterValue());
					try {
						param = Formatter.parseDateTime(filter.getFilterValue());
						logger.info("DATETIME as parameter: " + param);
					} catch (Exception ex) {
						logger.info("Could not parse the DATETIME: " + filter.getFilterValue(), ex);
					}
				}else if (SQLUtil.isTime(filter.getColumnType())) {
					try {
						param = Formatter.parseTime(filter.getFilterValue());
						logger.info("Time as parameter: " + param);
					} catch (Exception ex) {
						logger.info("Could not parse the time: " + filter.getFilterValue(), ex);
					}
				} else {
					// For LIKE stmt
					//deal with "Begins With" filter operator
					if(filter.getFilterOperator() == 5)
					{
						param = param + "%";
					}
					//deal with "Contains" filter operator
					else if(filter.getFilterOperator() == 6)
					{
						param = "%" + param + "%";
					}
					//deal with "Ends With" filter operator
					else if(filter.getFilterOperator() == 7)
					{
						param = "%" + param;
					}
					else
					{
						param = param;
					}
					logger.info("Generic parameter: " + param);
				}
				params.add(param);
			}
		}
		return params;
	}

	/*
	 * get Data from remote config Tables to be presented as dropdown on UI 
	 * Precondition: previosly set DD and DS on selected column in Admin/Colums part of app 
	 * (result is : metadata for selected column now has metadata colum_ID of choosen DD and DS columns)
	 * 
	 * BE : while loading data for colum, check in metadata id DD or DS Id is set, if to initiate this logic else skip
	 * 
	 * in GDMA1 this is all 1 call when fetching DATA for columns of table:  
	 * GdmaAjax.getUserAccessDetails.dwr - Gets user access for Active table and logged in User

	GdmaAjax.getTableDetails.dwr
	GdmaAjax.getData.dwr

	GdmaAjax.getDropDownData.dwr  (3 calls - 1 per each column)
	GdmaAjax.getDropDownData.dwr
	GdmaAjax.getDropDownData.dwr

	sql created: SELECT customers.city, customers.country FROM customers ORDER BY customers.country asc
		list members: [0, Melbourne, Australia]
		list members: [1, Chatswood, Australia]
		list members: [2, North Sydney, Australia]

	URL: 		http://localhost/gdma2/rest/column/data/dropdown/display/608/store/609

	 * */
	@Override
	public List getDropDownData(Column display, Column store) {
		logger.info("dynamicDao: getDropDownData");
		//get server by column
		//Server server = gdmaFacade.getServerDao().getByColumn(store.getId());
		Server server = repositoryManager.getServerRepository().activeServerWithActiveTableForColumn(store.getId());
		//open Q - both columns belong to same table?
		Table table = repositoryManager.getTableRepository().activeTableForColumn(store.getId());
		//String sql = SqlUtil.createDropDownSelect(server, server.getTables().iterator().next(), display, store);
		String sql = SQLUtil.createDropDownSelect(server, table, display, store);

		logger.info("sql created: " + sql);

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);
		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		// don't need transacton manager for lookup
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());
		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);

		//PaginatedResponse paginatedResponse = new PaginatedResponse();

		//todo see ResultSetExtractorColumns and use it instead  
		List list = (List)jdbcTemplate.query(psc.newPreparedStatementCreator(Collections.EMPTY_LIST), new RowMapper());
		for (Object object : list) {
			logger.info("list members: " + object.toString());
		}
		return list; 
	}


	/*TRANSACTIONALLY INSERT multiple rows INTO remote DB
	 * 
	 * PROBLEM 1: is remote table ID PK autoincrement? If not... we need to either use sequencer - which we don't have or
	 * create new PK id using application logic - take existing id+1???
	 * 
	 * Problem 2: composite PK - example DB 'classicmodels' see TABLE 'orderdetails'  PRIMARY KEY ('orderNumber','productCode')
	 * TODO: define incoming object and what needs to be returned to UI
	 * 
	 * OLD app: void	GdmaAjaxFacade.addRecord(UpdateRequest updateRequest), do auth user 
	 * */

	//@Auditable
	@Override
	public void addRecord(UpdateDataRequest updateRequest) {
		logger.info("addRecord");

		//Server server = repositoryManager.getServerRepository().findOne(updateRequest.getServerId()); REMOVED param from UI call
		Table table = repositoryManager.getTableRepository().findOne(updateRequest.getTableId());
		Server server = table.getServer();
		updateRequest.setServerId(server.getId());
		logger.info("server and table set");


		if( null == server || null == table){
			logger.error("Server or table does not exist!");
			return; //TODO 
		}

		//TODO check conditions : does column need to be active...
		List<Column> activeColumns = repositoryManager.getColumnRepository().findByTableIdAndActiveTrueAndDisplayedTrue(table.getId());
		table.setColumns(new LinkedHashSet(activeColumns));//IF BIDIRECTION IS TO BE REMOVED - to change this and pass colums to utility method themselves


		List<List<ColumnDataUpdate>> columnsUpdate = updateRequest.getUpdates();

		DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
		logger.info("transactionManager obtained");

		txTemplate.execute(new org.springframework.transaction.support.TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(org.springframework.transaction.TransactionStatus status) {
				logger.info("doInTransactionWithoutResult");
				for (List<ColumnDataUpdate> colList : columnsUpdate) {
					logger.info("1: iterrate in colUpdateList");
					//create new list of columns loaded from DB, use columnType to convert new value and create paremeters 
					//then handle SPECIAL columns and create INSERT 
					List<Column> columns = new ArrayList<Column>();
					final List parameters = new ArrayList();

					for (ColumnDataUpdate col : colList) {
						logger.info("2: single col update....");
						//Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
						Column column = repositoryManager.getColumnRepository().findOne(col.getColumnId());
						logger.info("3: colId from request found in DB: " + column.getId() + ", colName: " + column.getName());

						if(server.getConnectionUrl().toLowerCase().contains(("teradata").toLowerCase()) 
								&& column.isPrimarykey() && col.getNewColumnValue().equals("")){

						}else{
							columns.add(column);
							logger.info("4: params...");
							parameters.add(SQLUtil.convertToType(col.getNewColumnValue(), column.getColumnType()));
							logger.info("5: ...params converted");
						}
					}

					handleSpecialColumns(table.getColumns(), columns, parameters);
					final String sql = SQLUtil.createInsertStatement(server, table, columns);

					logger.info("sql: " + sql);
					logger.info("parameters: "  + parameters);


					JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
					//JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);
					logger.info("6: UPDATE...");
					jdbcTemplate.update(sql, parameters.toArray());
					logger.info("6: ..UPDATE end");

				}

			}
		});//inner class impl

	}



	/**
	 * Used during INSERT/UPDATE new Table data records if columns is 'Special' 
	 * 
	 * USER - 'U'
	 * 	get Logged in UserName / or use 'UNKNOWN' ???
	 *  convert to Object of String type and use as param for PreparedStatement 
	 *  >> result : save logged in username in remote DB 
	 * 
	 * 
	 * DATE - 'D':
	 * 
	 * new java.util.Date()
	 * 	format to String in format:  String dateFormat = "yyyy-MM-dd";
	 * get madata Column.getDateType() - must be sort of DATE/TIME type
	 * convert String dateFormat = "yyyy-MM-dd" into Object of type DATE/TIME
	 * add to params to be used in PreparedStatement
	 *  
	 *  >> result: save current Date in remote DB
	 * 
	 * @param metadataColumnSet
	 * @param columns
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	private void handleSpecialColumns(Set<Column> metadataColumnSet, List<Column> columns, List parameters) {
		logger.info("handleSpecialColumns, size " + metadataColumnSet.size());
		logger.info("columns, size " + columns.size());
		logger.info("parameters, size " + parameters.size());
		for (Column metadataColumn : metadataColumnSet) {

			logdetails(metadataColumn);//TODO REMOVE

			if (StringUtils.hasText(metadataColumn.getSpecial())) {
				//USER
				if ("U".equals(metadataColumn.getSpecial())) {
					logger.info("special column 'U' detected");
					String userName = userContextProvider.getLoggedInUserName();
					logger.info("logged user: " + userName);
					if(userName == null || userName.isEmpty()){
						userName = "UNKNOWN"; //TODO ? how and when can this happen???
					}

					// first see if by error the column is already included
					if (columns.contains(metadataColumn)) {
						int index = columns.indexOf(metadataColumn);
						logger.info("columns.indexOf(metadataColumn): " + index);
						parameters.set(index, SQLUtil.convertToType(userName, metadataColumn.getColumnType()));
					} else {
						columns.add(metadataColumn);
						parameters.add(SQLUtil.convertToType(userName, metadataColumn.getColumnType()));
					}
					//DATE
				} else if ("D".equals(metadataColumn.getSpecial())) {
					logger.info("special column 'D' detected");
					// first see if by error the column is already included
					if (columns.contains(metadataColumn)) {
						int index = columns.indexOf(metadataColumn);
						logger.info("columns.indexOf(metadataColumn): " + index);
						parameters.set(index, SQLUtil.convertToType(Formatter.formatDate(new Date()), metadataColumn.getColumnType()));
					} else {
						columns.add(metadataColumn);
						// can't be guaranteed that the column is a dated column
						// so convert to string
						parameters.add(SQLUtil.convertToType(Formatter.formatDate(new Date()), metadataColumn.getColumnType()));
					}
				}
			}
		}//for

	}


	private void logdetails(Column metadataColumn) {
		logger.info("metadata, id: " + metadataColumn.getId() + " , name: " + metadataColumn.getName() +  " , special: " + 	metadataColumn.getSpecial());

	}

	/**
	 * Updates data in remote DB - one SQL UPDATE is executed on each row update 
	 * 
	 * Update SQL query: UPDATE new_table_test_autoincrement SET name = ?, year = ? WHERE  (id = ?) 
	 * 
	 * Execution rules: 
	 *  - server and table must exists before update starts
	 *  - info carrier ColumnDataUpdate needs to have at least 1 entry
	 *  - table must have PK defined and there has to be PK column in request (checked for oldValue and metadata Type)  
	 *  - ColumnDataUpdate must have newColumnValue entries 
	 *  - after newColumnValue data type is converted to the type defined in column metadata, 
	 *    if dataType cannot be determined AND column cannot be null => irregular situation 
	 *  - handle special columns : if table being updated contains special columns: 
	 *  retrieve either username or current date with proper type conversion and and set as values  
	 *   	
	 *  
	 * @param updateRequest
	 * @return
	 */
	//@Auditable TODO
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateRecords(UpdateDataRequest updateRequest ) {
		logger.info("start column update");

		Table table = repositoryManager.getTableRepository().findOne(updateRequest.getTableId());
		Server server = table.getServer();
		updateRequest.setServerId(server.getId());
		logger.info("server and table set");

		if( null == server || null == table){
			logger.error("Error while update: server or table does not exist!");
			return -1; //TODO 
		}

		//TODO check conditions : does column need to be active...
		List<Column> activeColumns = repositoryManager.getColumnRepository().findByTableIdAndActiveTrueAndDisplayedTrue(table.getId());
		table.setColumns(new LinkedHashSet(activeColumns));//IF BIDIRECTION IS TO BE REMOVED - to change this and pass colums to utility method themselves




		List<List<ColumnDataUpdate>> columnsUpdate = updateRequest.getUpdates();

		/**/
		logger.info("*******TODO DELETE *************");
		List<List<ColumnDataUpdate>> updates = columnsUpdate;
		for (List<ColumnDataUpdate> list : updates) {
			for (ColumnDataUpdate columnDataUpdate : list) {
				logger.info(columnDataUpdate.getColumnId() + "  "  + columnDataUpdate.getOldColumnValue() + "  " + columnDataUpdate.getNewColumnValue());
			}

		}
		logger.info("********TODO DELETE ***********");
		/**/

		DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
		logger.info("transactionManager obtained");

		int countUpd = (Integer) txTemplate.execute(
				new org.springframework.transaction.support.TransactionCallback() {

					@Override
					public Object doInTransaction(
							org.springframework.transaction.TransactionStatus status) {

						logger.info("0: doInTransaction" +  status + " , start");

						int countUpdated = 0;

						for (List<ColumnDataUpdate> list : columnsUpdate) {//will perform multiple SQL UPDATEs row by row

							List<Column> columns = new ArrayList<Column>();
							final List parameters = new ArrayList();
							List keys = new ArrayList();

							if (CollectionUtils.isEmpty(list)) {
								throw new InvalidDataAccessResourceUsageException("Update list is empty");
							}

							for (ColumnDataUpdate columnUpdate : list) {
								logger.info("column list reading");
								//Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
								Column column = repositoryManager.getColumnRepository().findOne(columnUpdate.getColumnId());
								logger.info("2: columnUpdateId: " + columnUpdate.getColumnId() + " val old :" + columnUpdate.getOldColumnValue() + 
										" , new val: " + columnUpdate.getNewColumnValue()  
										+ " and local column found with name: " + column.getName() + " and id: " + column.getId() + 
										" and type : " + column.getColumnTypeString());

								if (column.isPrimarykey()) {
									logger.info("3: skip, add PK at the end");
									//columns.add(column);
									//keys.add(SQLUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));
									//parameters.add(SQLUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));
								} else {
									logger.info("3: column is NOT PK!");

									if (columnUpdate.getNewColumnValue() != null) {
										logger.info("4: column, not PK, has NEW value set (but can be null?)");
										columns.add(column);
										Object obj = SQLUtil.convertToType(columnUpdate.getNewColumnValue(), column.getColumnType());

										if (obj == null && !column.isNullable()) {
											throw new ServiceException("Column " + column.getName() + " can not be set to NULL and must have a value");
										}

										parameters.add(obj);

										if (obj == null){
											logger.info("5: obj of data type is null - problem with data type conversion?");
										} else {
											logger.info("5: New value " + obj.toString());
										}
									}
								}
							} //internal for end


							//ADD PKs at the end of both colums collection and key at : parameters.addAll(keys); //so they match positions
							for (ColumnDataUpdate columnUpdate : list) {
								Column column = repositoryManager.getColumnRepository().findOne(columnUpdate.getColumnId());
								if (column.isPrimarykey()) {
									logger.info("6: column IS PK! Getting old value from request and type from metadata");
									columns.add(column);
									keys.add(SQLUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));

								} 
							} //internal for end


							if (CollectionUtils.isEmpty(parameters)) {
								throw new InvalidDataAccessResourceUsageException("No update values found!");
							}

							if (null == keys || keys.isEmpty()) {
								throw new InvalidDataAccessResourceUsageException("Update Not possible as no Table Primary Key Set. Please contact your administrator");
							}
							logger.info("6: before handling special columns");
							//TODO !!!

							parameters.addAll(keys); //add keys at the end because they are always last in:  
							// UPDATE new_table_test_autoincrement SET year = ?, name = ?, employment_date = ? WHERE  (id = ?) 
							//...now positions are matched for mapping by index and collections of columns and  parameters have the same size
							handleSpecialColumns(table.getColumns(), columns, parameters);

							String sql = SQLUtil.createUpdateStatement(server, table, columns);
							logger.info("Update SQL query: " + sql);
							logger.info("parameters: " + parameters);
							JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
							countUpdated += jdbcTemplate.update(sql, parameters.toArray());

						}//main for

						logger.info("doInTransaction() - end - return value=" + countUpdated);
						return countUpdated;
					}	

				});//inner interface impl end

		logger.info("updateRecords() - end - return value=" + countUpd);
		return countUpd;
	}



	/**
	 * Deletes selected multiple records from Column Data view
	 * PKs must be used and send from UI
	 * 
	 * SQLs to be executed to delete 2 rows from table with PK named 'id'
					 	DELETE FROM new_table_test_autoincrement WHERE  (id = ?) 
 						DELETE FROM new_table_test_autoincrement WHERE  (id = ?) 
	 */
	//@Auditable TODO//
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int deleteRecords(UpdateDataRequest updateRequest) {
		logger.info("start delete records");

		Table table = repositoryManager.getTableRepository().findOne(updateRequest.getTableId());
		Server server = table.getServer();
		updateRequest.setServerId(server.getId());
		logger.info("server and table set");

		if( null == server || null == table){
			logger.error("Error while delete: server or table does not exist!");
			return -1; //TODO 
		}

		List<List<ColumnDataUpdate>> columnsUpdate = updateRequest.getUpdates();

		for (List<ColumnDataUpdate> list : columnsUpdate) {
			for (ColumnDataUpdate columnDataUpdate : list) {
				logger.info("dumy col Id: " + columnDataUpdate.getColumnId() + " old val: " + columnDataUpdate.getOldColumnValue() + " new val:" + columnDataUpdate.getNewColumnValue());
			}
		}

		DataSourceTransactionManager transactionManager = dataSourcePool.getTransactionManager(server);
		TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
		logger.info("transactionManager obtained");

		int countDel = (Integer) txTemplate.execute(new org.springframework.transaction.support.TransactionCallback() {

			@Override
			public Object doInTransaction(
					org.springframework.transaction.TransactionStatus status) {
				logger.info("doInTransaction");
				int countDeleted = 0;
				for (List<ColumnDataUpdate> list : columnsUpdate) {

					List<Column> columns = new ArrayList<Column>();
					final List keys = new ArrayList();

					if (CollectionUtils.isEmpty(list)) {
						throw new InvalidDataAccessResourceUsageException("Cannot delete records as this table does not have a primary key set");
					}

					for (ColumnDataUpdate columnUpdate : list) {
						logger.info("1: column list itteration");
						//Column column = gdmaFacade.getColumnDao().get(columnUpdate.getColumnId());
						Column column = repositoryManager.getColumnRepository().findOne(columnUpdate.getColumnId());
						columns.add(column);
						if (column.isPrimarykey()) {
							logger.info("2: Pk detected");
							keys.add(SQLUtil.convertToType(columnUpdate.getOldColumnValue(), column.getColumnType()));
						}
					}

					final String sql = SQLUtil.createDeleteStatement(server, table, columns);
					logger.info("Delete SQL used: " + sql);
					logger.info("Keys-values used : " + keys);

					/*	
					 	DELETE FROM new_table_test_autoincrement WHERE  (id = ?) 
 						DELETE FROM new_table_test_autoincrement WHERE  (id = ?) 
					 */

					JdbcTemplate jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());
					countDeleted += jdbcTemplate.update(sql, keys.toArray());

				}//for
				logger.info("count deleted in itteration" + countDeleted);
				return countDeleted;
			}

		});//inner iterface impl. end

		logger.info("count all deleted" + countDel);
		return countDel;
	}


	/*get just data for table
	 * 
	[1, France, Nantes, Carine , 44000, 1370, 103, Atelier graphique, 40.32.2555, 54, rue Royale, 21000.0, null, Schmitt, null]
	[2, USA, Las Vegas, Jean, 83030, 1166, 112, Signal Gift Stores, 7025551838, 8489 Strong St., 71800.0, null, King, NV]
	[3, Australia, Melbourne, Peter, 3004, 1611, 114, Australian Collectors, Co., 03 9520 4555, 636 St Kilda Road, 117300.0, Level 3, Ferguson, Victoria]
	[4, France, Nantes, Janine , 44000, 1370, 119, La Rochelle Gifts, 40.67.8555, 67, rue des Cinquante Otages, 118200.0, null, Labrune, null]
	[5, Norway, Stavern, Jonas , 4110, 1504, 121, Baane Mini Imports, 07-98 9555, Erling Skakkes gate 78, 81700.0, null, Bergulfsen, null]

	 * */
	@Override
	public List getTableData(Table table, Server server, Column orderByColumn,
			List<Filter> filters,
			String orderDirection, int startIndex,int length) {


		logger.info("getTableData");
		String sql = SQLUtil.createSelect(server, table, orderByColumn, orderDirection, filters);
		logger.info("sql created: " + sql);

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);

		declareSqlParameters(psc, filters, server);

		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);

		final List<Object> params = convertFiltersToSqlParameterValues(filters);
		logger.info("params: " +  params);

		/*
		List records =  (List)jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
				startIndex, length));

		logger.info("list content:");
		for (int i = 0; i < records.size(); i++) {
			Object object = records.get(i);
			logger.info(object.toString());
		}
		logger.info("list content emd");


		//todo in metadata service after returning List<Entity>
		//paginatedResponse.setTotalRecords(getCount(server, table, paginatedRequest.getFilters()));
		//paginatedResponse.setStartIndex(paginatedRequest.getRecordOffset());
		//paginatedResponse.setKey("" + paginatedRequest.getSortedByColumnId());
		//paginatedResponse.setSortDir(paginatedRequest.getDir());

			//return paginatedResponse;


		return records;
		 */


		List<TableRowDTO> rows =(List<TableRowDTO>) jdbcTemplate.query(psc.newPreparedStatementCreator(params), 
				new PagedResultSetExtractor(new TableDataRowMapper(),
						startIndex,length));

		logger.info("list new content:");
		for (TableRowDTO tableRowDTO : rows) {

			logger.info("row number:" + tableRowDTO.getRowNumber().intValue());

			List<TableColumn> columns = tableRowDTO.getColumns();
			for (TableColumn column : columns) {
				logger.info("columnName: " + column.getColumnName() + " , val: " + (column.getVal()==null ? "" : column.getVal()) );
			}
		}

		return rows;


	}




	/*return column metadata extended with values for selected Table in Data module*/
	/* if generic original version is used only data is returned: 
	 * 
	 * {  
   "data":[  
      {  
         "id":628,
         "table":null,
         "name":"country",
         "columnType":12,
         "columnTypeString":"VARCHAR",
         "dropDownColumnDisplay":null,
         "dropDownColumnStore":null,
         "displayed":true,
         "allowInsert":true,
         "allowUpdate":true,
         "nullable":false,
         "primarykey":false,
         "special":"N",
         "minWidth":null,
         "maxWidth":null,
         "orderby":null,
         "columnSize":50,
         "active":true,
         "alias":"country",
         "columnValues":{  	//NEW STRUCTURE
            "1":"France",
            "2":"USA",
            "3":"Australia",
	 */		

	@Override 
	public List getTableDataWithColumnMetadata(Table table, Server server, Column orderByColumn,
			List<Filter> filters,
			String orderDirection, int startIndex,int length) {


		logger.info("getDataForTable");
		String sql = SQLUtil.createSelect(server, table, orderByColumn, orderDirection, filters);
		logger.info("sql created: " + sql);

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);

		declareSqlParameters(psc, filters, server);

		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);

		final List<Object> params = convertFiltersToSqlParameterValues(filters);
		logger.info("params: " +  params);


		List<TableRowDTO> rows =(List<TableRowDTO>) jdbcTemplate.query(psc.newPreparedStatementCreator(params), 
				new PagedResultSetExtractor(new TableDataRowMapper(),
						startIndex,length));

		logger.info("list new content:");
		for (TableRowDTO tableRowDTO : rows) {

			logger.info("row number:" + tableRowDTO.getRowNumber().intValue());

			List<TableColumn> columns = tableRowDTO.getColumns();
			for (TableColumn column : columns) {
				logger.info("columnName: " + column.getColumnName() + " , val: " + (column.getVal()==null ? "" : column.getVal()) );
			}
		}

		//return rows;


		//set values for every column
		for(Column col: table.getColumns()){
			addValuesToColumnMetadata(rows, col);
		}

		//load display values for dropdowns if any
		//resolveLookupTables(table.getColumns());

		//remove table and other parent object to make smaller JSON repsponse
		for (Column column : table.getColumns()) {
			column.setTable(null);
		}

		//return tableDTOList;
		List<Column> colListWithValues = new ArrayList<Column>(table.getColumns());

		return colListWithValues;

	}

	/*used for : getTableDataWithColumnMetadata() to add 
	 *  "columnValues":{  	//NEW STRUCTURE
            "1":"France",
            "2":"USA",
            "3":"Australia",*/
	private void addValuesToColumnMetadata(List<TableRowDTO> rows, Column metadataColumn) {

		/*----------*/
		logger.info("addValuesToColumns");
		Map<Integer,String> valMap= new LinkedHashMap<Integer, String>();
		//List<String> columnValues = new ArrayList<String>();

		for (TableRowDTO tableRowDTO : rows) {
			BigInteger rowNumber = tableRowDTO.getRowNumber();
			logger.info("rownumber: " + rowNumber.intValue());


			List<TableColumn> columns = tableRowDTO.getColumns();
			for (TableColumn column : columns) {
				logger.info("columnName: " + column.getColumnName() + " , val: " + (column.getVal()==null ? "" : column.getVal()) );

				if(metadataColumn.getName().equalsIgnoreCase(column.getColumnName())){
					//columnValues.add(column.getVal() == null ? "" : column.getVal().toString());

					valMap.put(rowNumber.intValue(), column.getVal() == null ? "" : column.getVal().toString());
				}

			}
		}
		//metadataColumn.setColumnValues(columnValues);
		metadataColumn.setColumnValues(valMap);
	}





	/* 

{  
   "data":[  
      {  
         "rowNumber":1,
         "columns":[  
            {  
               "columnName":"country",
               "val":"France"
            },
            {  
               "columnName":"city",
               "val":"Nantes"
            },
            {  
               "columnName":"contactFirstName",
               "val":"Carine "
            },
            {  
               "columnName":"membership_id",
               "val":{  
                  "value":"101",
                  "did":2255,
                  "sid":2254,
                  "dropdownOptions":[  
                     [  
                        0,
                        103,
                        "gold"
                     ],
                     [  
                        1,
                        101,
                        "ordinary"
                     ],
                     [  
                        2,
                        102,
                        "silver"
                     ]
                  ]
               }
            },
            {  
               "columnName":"postalCode",
		...
		   ],
   "draw":0,
   "recordsTotal":122,
   "recordsFiltered":122
	 */
	@Override
	public List getTableDataWithColumnNamesAndDropdowns(Table table, Server server, Column orderByColumn,
			List<Filter> filters,
			String orderDirection, int startIndex,int length) {


		logger.info("getTableDataWithColumnNamesAndDropdowns");
		String sql = SQLUtil.createSelect(server, table, orderByColumn, orderDirection, filters);
		logger.info("sql created: " + sql);

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);

		declareSqlParameters(psc, filters, server);

		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server);

		final List<Object> params = convertFiltersToSqlParameterValues(filters);
		logger.info("params: " +  params);


		List<TableRowDTO> rows =(List<TableRowDTO>) jdbcTemplate.query(psc.newPreparedStatementCreator(params), 
				new PagedResultSetExtractor(new TableDataRowMapper(),
						startIndex,length));


		logger.info("RESULT: list data with column names:");
		for (TableRowDTO tableRowDTO : rows) {

			logger.info("row number:" + tableRowDTO.getRowNumber().intValue());

			List<TableColumn> columns = tableRowDTO.getColumns();
			for (TableColumn column : columns) {
				logger.info("columnName: " + column.getColumnName() + " , val: " + (column.getVal()==null ? "" : column.getVal()) );
			}
		}

		logger.info("detecting Lookup columns...");
		for(Column col: table.getColumns()){
			if(col.getDropDownColumnDisplay() != null && col.getDropDownColumnStore() != null){
				replaceFKvaluesWithDropdown(rows, col);	
			}

		}
		return rows;
		//return TableDataFormatUtil.createPlainList(rows);
		//return TableDataFormatUtil.createListWithMap(rows);
	}



	/*locate Lookup columns : one that have that have metadataColumn.getDropDownColumnDisplay and  metadataColumn.getDropDownColumnStore() defined
	 * for each such column create 1 SQL to load ALL remote Lookup data by calling: getDropDownData()
	 *  a) iterrate over Table data matrix, in each row locate column of this type 
	 *  	 column contains FK value (membership_id = 101) 
	 *  b)  replace value in each column with DropDownColumn.java object structure
	 *    
	 *
	 * 
   "data":[  
      {  
         "rowNumber":1,
         "columns":[  
            {  
               "columnName":"country",
               "val":"France"
            },
            {  
               "columnName":"city",
               "val":"Nantes"
            },
            {  
               "columnName":"contactFirstName",
               "val":"Carine "
            },
            {  
               "columnName":"membership_id",
               "val":{  
                  "value":"101",
                  "did":2255,
                  "sid":2254,
                  "dropdownOptions":[  
                     [  
                        0,
                        103,
                        "gold"
                     ],
                     [  
                        1,
                        101,
                        "ordinary"
                     ],
                     [  
                        2,
                        102,
                        "silver"
                     ]
                  ]
               }
            },   
	 *
    } 
	 * */
	private void replaceFKvaluesWithDropdown(List<TableRowDTO> rows, Column metadataColumn) {
		logger.info("replaceFKvaluesWithDropdown");
		logger.info("column:" + metadataColumn.getName().toUpperCase() + " has Lokup columns defined: " 
				+ " display: " + metadataColumn.getDropDownColumnDisplay().getName().toLowerCase() + " , store:" + metadataColumn.getDropDownColumnStore().getName().toUpperCase());

		//fetch Lookup table - all values	
		List<List<Object>> dropDownDataRows = getDropDownData(metadataColumn.getDropDownColumnDisplay(), metadataColumn.getDropDownColumnStore());

		/*
		sql created: SELECT gender.id, gender.gender_name FROM gender ORDER BY gender.gender_name asc

		 RESULT: [0, 23, Female], [1, 22, Male], [2, 25, Not Aplicable], [3, 24, Unkown]
		 */

		/*
		for (List<Object> ddRows : dropDownDataRows) {
			for (Object row : ddRows) {
				logger.info(row.toString()); // 0 23 FEMALE 
			}

		}
		 */

		/*----------*/
		logger.info("form DropDownColumn object for each Lookup value");


		//List<TableRowDTO> rows
		for (TableRowDTO tableRowDTO : rows) {
			logger.info("rownumber: " + tableRowDTO.getRowNumber().intValue());

			List<TableColumn> columns = tableRowDTO.getColumns();
			for (TableColumn columnDTO : columns) {
				logger.info("columnName: " + columnDTO.getColumnName() + " , val: " + (columnDTO.getVal()==null ? "" : columnDTO.getVal()) );

				//match metadata column name with result set column name
				if(metadataColumn.getName().equalsIgnoreCase(columnDTO.getColumnName())){
					logger.info("Lookup column: "  +metadataColumn.getName().toUpperCase() + " detected, replacing value with DropDownColumn object");
					DropDownColumn dropdownColumn = new DropDownColumn();
					dropdownColumn.setValue( (columnDTO.getVal() == null ? "" : columnDTO.getVal().toString()) );
					dropdownColumn.setDid(metadataColumn.getDropDownColumnDisplay().getId());
					dropdownColumn.setSid(metadataColumn.getDropDownColumnStore().getId());
					dropdownColumn.setDropdownOptions(dropDownDataRows);

					//replace FK value (membership_id = 101) with DropDownColumn
					columnDTO.setVal(dropdownColumn); //instead of concrete value e.g. 24 put complete Drodwon object
				}

			}//columns
		}//rows


	}

	//TODO test TRANSACTIONS, test NULL values, test non-autoinc, test value with quotes, test mysql/other DB vendors, test more columns in header than in body
	//this code will INSERT partially new data id some row in CSV contains bad data - check if needs to be done in transaction
	@Override
	public int bulkImport(Server server, Table table, Set<Column> columns,	MultipartFile file) {

		int counter = 0;

		InputStream inputStream = null;
		com.opencsv.CSVReader rdr = null;

		try {
			inputStream = file.getInputStream();
			rdr = new com.opencsv.CSVReader(new InputStreamReader(inputStream));
			//READ HEADERS
			final String[] headers = rdr.readNext();
			logger.info("Headers starting with: " + headers[0]);

			if (headers != null) {
				//UPDATE
				final List<Integer> keysPosition = new ArrayList<Integer>();
				final List<SqlParameter> updateParametersTypeList = createUpdateParametersTypeAndKeysPositionList(columns, headers,
						keysPosition);
				final String updateStatement = extractUpdateStatement(server, table, columns, headers);
				final PreparedStatementCreatorFactory updateStatementCreatorFactory = new PreparedStatementCreatorFactory(updateStatement.toString(), updateParametersTypeList);
				
				//INSERT
				StringJoiner patternListJoiner = new StringJoiner(","," VALUES (",")");
				final List<SqlParameter> insertParametersTypeList = new ArrayList<SqlParameter>();
				StringJoiner insertColumnNameListJoiner = server.getConnectionUrl().contains("mysql") ? new StringJoiner(",","(",")") :  new StringJoiner("\",\"", "(\"", "\") ");
				for (String header : headers) {
					header = header.trim();
					Column insertColumn = null;
					for (Column column : columns) {
						if (header.equals(column.getAlias()) || header.equals(column.getName())) {
							insertColumn = column;
							break;
						}
					}
					if (insertColumn == null){
						throw new IOException("The column \"" + header + "\" does not exist in " + table.getName());
					}

					logger.info("Column is of type " + insertColumn.getColumnTypeString());
					patternListJoiner.add("?");
					insertColumnNameListJoiner.add(insertColumn.getName());

					// using varchar because all values from CSV are strings
					//	params.add(new SqlParameter(Types.VARCHAR));
					//bh changing this as it was breaking date types
					insertParametersTypeList.add(new SqlParameter(insertColumn.getColumnType()));
				}

				final StringBuilder insertStatement = new StringBuilder("INSERT INTO ");
				if(server.getPrefix().length() > 0){
					insertStatement.append(server.getPrefix());
					insertStatement.append(".");
				}
				insertStatement.append(table.getName());
				insertStatement.append(insertColumnNameListJoiner.toString());
				insertStatement.append(patternListJoiner.toString());
				logger.info("Preparing sql: [" + insertStatement + "]");
				final PreparedStatementCreatorFactory pscFactory = new PreparedStatementCreatorFactory(insertStatement.toString(), insertParametersTypeList);
				
				//START READING DATA
				String[] row = rdr.readNext();
				final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());
				while (row != null) {
					try {
						logger.info("Row starting with: " + row[0] );
						jdbcTemplate.update(insertStatement.toString(), pscFactory.newPreparedStatementSetter(row));
						counter++;
						
					}catch (DataIntegrityViolationException e) {
						logger.error(e.getMessage());
						try {
							counter += executeUpdateFromFileRow(keysPosition, updateStatement,
									updateStatementCreatorFactory, row, jdbcTemplate);
						} catch (Exception e2) {
							logger.error(e2.getMessage());
						}
					} catch (DataAccessException ex) {
						// throw new ServiceException("ERROR: cannot INSERT new data into table",ex);
                        // throw new ServiceException(ex.getMessage(),ex);
						//throw new IOException("Could not import data:" + ex.getMessage());
						logger.error(ex.getMessage());
					} finally {
						row = rdr.readNext();
					}
				}
			}
			return counter;
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace(); //TODO

		} finally{
			try {
				rdr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return counter;
	}

	private List<SqlParameter> createUpdateParametersTypeAndKeysPositionList(Set<Column> columns, final String[] headers,
			final List<Integer> keysPosition) {
		final List<SqlParameter> updateParametersTypeList = new ArrayList<SqlParameter>();
		final List<SqlParameter> updateKeyParametersTypeList = new ArrayList<SqlParameter>();
		int columnPosition = -1;
		for (String header : headers) {
			columnPosition++;
			header = header.trim();
			for (Column column : columns) {
				if (header.equals(column.getAlias()) || header.equals(column.getName())) {
					if (column.isPrimarykey()) {
						keysPosition.add(columnPosition);
						updateKeyParametersTypeList.add(new SqlParameter(column.getColumnType()));
					} else {
						updateParametersTypeList.add(new SqlParameter(column.getColumnType()));
					}
					break;
				}
			}
		}
		updateParametersTypeList.addAll(updateKeyParametersTypeList);
		return updateParametersTypeList;
	}

	private String extractUpdateStatement(Server server, Table table, Set<Column> columns, final String[] headers) {
		final List<Column> updateColumnsList = new ArrayList<Column>();
		for (String header : headers) {
			header = header.trim();
			for (Column column : columns) {
				if (header.equals(column.getAlias()) || header.equals(column.getName())) {
					updateColumnsList.add(column);
					break;
				}
			}
		}
		final String updateStatement = SQLUtil.createUpdateStatement(server, table, updateColumnsList);
		return updateStatement;
	}

	private int executeUpdateFromFileRow(List<Integer> keysPosition, final String updateStatement,
			final PreparedStatementCreatorFactory updateStatementCreatorFactory, String[] row,
			final JdbcTemplate jdbcTemplate) {
		if (null == keysPosition || keysPosition.isEmpty()) {
			logger.error("Update Not possible as no Table Primary Key Present in the headers. Please contact your administrator");
		} else {
			List updateKeysValues = new ArrayList();
			List updateValues = new ArrayList();
			for (int i = 0; i < row.length; i++) {
				if(keysPosition.contains(i)){
					updateKeysValues.add(row[i]);		
				} else {
					updateValues.add(row[i]);
				}
			}
			if (null == updateKeysValues || updateKeysValues.isEmpty()) {
				logger.error("Update Not possible as no Table Primary Key Value Set. Please contact your administrator");
			} else {
				updateValues.addAll(updateKeysValues); //add keys at the end because they are always last in statement
				//TODO handleSpecialColumns
				//handleSpecialColumns(table.getColumns(), columns, updateValues);
				logger.info("Update SQL query: " + updateStatement);
				logger.info("parameters: " + updateValues);
				return jdbcTemplate.update(updateStatement, updateStatementCreatorFactory.newPreparedStatementSetter(updateValues));
			}
		}
		return 0;
	}



}
