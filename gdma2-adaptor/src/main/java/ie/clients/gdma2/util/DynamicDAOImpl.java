package ie.clients.gdma2.util;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class DynamicDAOImpl implements DynamicDAO{

	private static final Logger logger = LoggerFactory.getLogger(DynamicDAOImpl.class);

	@Autowired
	protected RepositoryManager repositoryManager;

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

		for (Column column : columns) {
			logger.info("remote table column: " + column.getName());
		}
		return columns;


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


	/*  DATA MODULE SECTION*/

	/* make sure if original is generic or just for columns
	 * TODO define and send FILTERs to this method
	 * decide where to initiate all Entities - here or in caller
	 * make sure what is orderByColumnID
	 * 
	 *  TODO filters
	 *  TODO pagination
	 *  
	 *  https://localhost/gdma2/rest/column/data/read/table/630*/

	@Override
	public List<Column> getColumnData(Integer tableId, String matching,
			int orderByColumnID, String orderDirection, int startIndex,
			int length) {


		Table table = repositoryManager.getTableRepository().findOne(tableId);
		logger.info("DynamicDaoIMPL: getColumnData");
		Server server2 = table.getServer();
		
		//TODO check conditions : does column need to be active...
		List<Column> activeColumns = repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(table.getId());
		table.setColumns(new HashSet(activeColumns));//IF BIDIRECTION IS TO BE REMOVED - to change this and pass colums to utility method themselves
		
		//table 
		Column sortedByColumnId = (orderByColumnID == 0 ? null : repositoryManager.getColumnRepository().findOne(orderByColumnID));
		//dir
		List<Filter> filters = new ArrayList<Filter>(); //TODO Open Q
		
		String sql = SQLUtil.createSelect(server2, table, sortedByColumnId, orderDirection, filters);
		//String sql = SqlUtil.createSelect(server, table, sortedByColumnId, dir, paginatedRequest.getFilters());

		logger.info("sql created: " + sql);

		PreparedStatementCreatorFactory psc = new PreparedStatementCreatorFactory(sql);

		declareSqlParameters(psc, filters, server2);

		psc.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		psc.setUpdatableResults(false);

		// don't need transacton manager for lookup
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getTransactionManager(server).getDataSource());
		JdbcTemplate jdbcTemplate = dataSourcePool.getJdbcTemplateFromDataDource(server2);

		//PaginatedResponse paginatedResponse = new PaginatedResponse();

		final List<Object> params = convertFiltersToSqlParameterValues(filters);
		logger.info("params: " +  params);

		//paginatedResponse.setRecords((List) jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
		//	paginatedRequest.getRecordOffset(), paginatedRequest.getRowsPerPage())));



		List records =  (List)jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
				startIndex, length));

		//getPaginatedTableResponse(records != null ? records : new ArrayList<ConnectionType>(), total, filtered);
		//getPaginatedTableResponse(records != null ? records : new ArrayList<ConnectionType>(), 10, 10);



		try{
			List records2 =  (List)jdbcTemplate.query(psc.newPreparedStatementCreator(params), new PagedResultSetExtractor(new RowMapper(),
					startIndex, length));
		}
		catch (Exception e){
			logger.error("*****Exception****** e = " + e);
		}

		/** todo in metadata service after returning List<Entity>
		paginatedResponse.setTotalRecords(getCount(server, table, paginatedRequest.getFilters()));
		paginatedResponse.setStartIndex(paginatedRequest.getRecordOffset());
		paginatedResponse.setKey("" + paginatedRequest.getSortedByColumnId());
		paginatedResponse.setSortDir(paginatedRequest.getDir());

		return paginatedResponse;
		 */

		return records;

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
				} /*else if (SqlUtil.isDateTime(filter.getColumnType())) {
                LOG.debug("DATETIME filter detected: " + filter.getFilterValue());
				try {
                    param = Formatter.parseDate(filter.getFilterValue());
					LOG.debug("DATETIME as parameter: " + param);
				} catch (Exception ex) {
					LOG.error("Could not parse the DATETIME: " + filter.getFilterValue(), ex);
				}
            	}*/else if (SQLUtil.isTime(filter.getColumnType())) {
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
	 * get Data for dropdown columns
	 * 
	 * in GDMA1 this is all 1 call when fetching DATA for columns of table:  
	 * GdmaAjax.getUserAccessDetails.dwr - Gets user access for Active table and logged in User

	  GdmaAjax.getTableDetails.dwr

	GdmaAjax.getDropDownData.dwr  (3 calls - 1 per each column)
	GdmaAjax.getDropDownData.dwr
	GdmaAjax.getDropDownData.dwr

	GdmaAjax.getData.dwr
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






}
