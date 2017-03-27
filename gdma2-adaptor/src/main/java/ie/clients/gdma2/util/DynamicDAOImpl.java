package ie.clients.gdma2.util;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
		for (String table : tableList) {
			logger.info("tableName: "  + table);
		}
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
			logger.info(column.getName());
		}
		return columns;


	}


	/**
	 * Synch Tables for selected Server (double clicked Server in Admin Module) between Remote DB server and GDMA DB server 
	 * After synch is done and new Table set save to DB, retreive that set and return only active ones
	 * calls helper method resyncTableList that performs synch 
	 */
	public List<Table> getTablesForServerAfterSynch(Integer serverId) {
		logger.info("getTablesForServerAfterSynch, serverId:  " + serverId);
		//loading only server and connection type
		Server server = repositoryManager.getServerRepository().findOne(serverId);
		//loading tables for server
		List<Table> tableList = repositoryManager.getTableRepository().findByServerId(server.getId());
		Set<Table> tableSet = new HashSet<Table>(tableList);
		server.setTables(tableSet);


		// TODO use an AOP trigger for this
		logger.info("starting table SYNCH");
		//Comment the next line out if moving resynch functionality to the Refresh button
		resyncTableList(server);
		logger.info("...table SYNCH ended");

		List<Table> activeTableList = repositoryManager.getTableRepository().findByServerIdAndActiveTrue(server.getId());
		//TODO set tables to server parent after synch ?
		return  activeTableList;

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

		2. TABLE NAMES COMPARE (GDMA -> REMOTE), Deleted from GDMA 

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

	 */
	private void resyncTableList(Server server) {

		logger.info("resyncTableList: " + server.getId());
		//TODO define in parent interface??

		//RESULT of synch   
		Set<Table> tablesSynchResult = new HashSet<Table>(); 

		//GDMA tables list
		Set<Table> tablesGDMA = server.getTables();
		logger.info("Number of tables in GDMA: " + tablesGDMA.size());

		//Remote server table list
		List<String> tableNameListRemoteDB = getSqlGetTables(server, server.getConnectionType().getSqlGetTables());
		printRemoteDBTableDetails(tableNameListRemoteDB);

		//LOOP remote DB table names
		for (String tableNameRemote : tableNameListRemoteDB) {
			logger.info("checking : tableNameRemote:" + tableNameRemote);

			Table tableGDMA = remoteTableAlreadyExistsInGDMA(tableNameRemote, tablesGDMA);
			if(tableGDMA != null) {
				//1.1	 TABLE NAMES MATCH  - remote DB name found lon local
				logger.info("	tableNameRemote:" + tableNameRemote + " is found on local");
				resolveTablesWithSameNames(tablesSynchResult, tableGDMA, tableNameRemote, server);
			} else {
				//1.2	NO MATCH - remote DB name not found on local 
				logger.info("tableNameRemote:" + tableNameRemote + " not found on local DB");
				createNewGDMATable(tablesSynchResult, tableNameRemote, server);
			}


		}//for1

		//previous double loop is ended, now compare in other direction to detect GDMA tables that don't exist on remote Server - Deleted
		logger.info("-----------------");
		resolveDeletedTables(tablesSynchResult, tablesGDMA, tableNameListRemoteDB, server );

		printSynchResult(tablesSynchResult);
		//serverDao.save(server);OLD CODE
		logger.info("saving tablesSynchResult");
		//save RESULT: 
		repositoryManager.getTableRepository().save(tablesSynchResult);
	}


	private Table  remoteTableAlreadyExistsInGDMA(String tableNameRemote, Set<Table> tablesGDMA){
		for (Table tableGDMA : tablesGDMA) {
			if(tableNameRemote.equalsIgnoreCase(tableGDMA.getName()))
				return tableGDMA;
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
			Set<Table> tablesGDMA, List<String> tableNameListRemoteDB, Server server) {
		logger.info("resolveDeletedTables");

		for(Table tableGDMA : tablesGDMA){
			boolean nameFound = false;

			for(String tableNameRemote : tableNameListRemoteDB){
				if(tableNameRemote.equalsIgnoreCase(tableGDMA.getName())){
					nameFound = true;
					break; //just skip and continue searching for non-existing
				}
			}//for2

			if(!nameFound){

				//deactivate tableGDMA
				tableGDMA.setActive(false);
				logger.info("table :" + tableGDMA.getName() + " is not in remote table list anymore, deactivating table");

				//add deactivated table to result table set
				tablesSynchResult.add(tableGDMA);

				//TODO ? Where to place this nad how to make TRANSACTION!?!
				logger.info("deleting UserAccess for table: " + tableGDMA.getId());
				List<UserAccess> userAcceseList = repositoryManager.getUserAccessRepository().findByTableId(tableGDMA.getId());
				repositoryManager.getUserAccessRepository().delete(userAcceseList);

				//resolve columns
				resolveColumnsForDeactivatedTable(tableGDMA, server);
			}	
		}//for1

	}


	/**
	 * Iterate over all columns on all tables on server,
	 *  and see if some column from Deactivated Table above is present there as DropDown column, if so : null them
	 * @param tableDeactiveGDMA
	 * @param server
	 */
	private void resolveColumnsForDeactivatedTable(Table tableDeactiveGDMA, Server server) {
		logger.info("resolveColumnsForDeactivatedTable");

		Set<Column> columnsDeactiveGDMA = tableDeactiveGDMA.getColumns();
		List<Integer> columnDeactiveIdList = new ArrayList<Integer>();
		for(Column columnGDMA: columnsDeactiveGDMA){
			columnDeactiveIdList.add(columnGDMA.getId());
		}


		Set<Table> tablesGDMA = server.getTables();

		for (Table tableGDMA : tablesGDMA) {
			Set<Column> columnsGDMA = tableGDMA.getColumns();
			for (Column col : columnsGDMA) {

				for(Integer colDeactiveId : columnDeactiveIdList){

					if (col.getDropDownColumnDisplay() != null && col.getDropDownColumnDisplay().getId() == colDeactiveId){
						col.setDropDownColumnDisplay(null);
						col.setDropDownColumnStore(null);
					}

					if(col.getDropDownColumnStore() != null && col.getDropDownColumnStore().getId() == colDeactiveId){
						col.setDropDownColumnDisplay(null);
						col.setDropDownColumnStore(null);
					}
					logger.info("Removed foreign key reference from column " + col.getName() + " in table: " + tableGDMA.getName());
				}//for3
			}//for2
		}//for1

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
	public List <Column> getColumnsForTableAfterSynch(Integer serverId, Integer tableId) {
		logger.info("getColumnsForTableAfterSynch, for server: " +  serverId + " and table: " + tableId);

		// TODO use an AOP trigger for this
		//transient dependecies - load table for server and columns for table, set columns on table
		Server server = repositoryManager.getServerRepository().findOne(serverId);
		Table tableGDMA = repositoryManager.getTableRepository().findOne(tableId);
		Set<Column> columnsGDMA = repositoryManager.getColumnRepository().findByTableId(tableId);
		tableGDMA.setColumns(columnsGDMA);

		logger.info("starting column SYNCH");
		resyncColumnList(server, tableGDMA);
		logger.info("...column SYNCH ended");

		//reload table list after synch
		//TODO OPEN Q - after saving columns - updating table that contains it ?
		List<Column> activeColumnList = repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(tableId);
		return activeColumnList;
	}



	
	/**
	 * Connect to server, get requested table,  get the list of columns from the remote database
	 *  and then iterate over the list to see if any are missing,. 
	 *  If there is, then it will create a new column object and save it to local GDMA table.
	 *  
	 * @param server, tableGMDA with loaded column list
	 * @param tableGDMA
	 */
	private void resyncColumnList(Server server, Table tableGDMA) {
		logger.info("resyncColumnList, for table: " + tableGDMA.getName());

		//RESULT of synch   
		Set<Column> columnsSynchResult = new HashSet<Column>(); 

		//GDMA column list
		Set<Column> columnsGDMA = tableGDMA.getColumns();
		logger.info("Number of columns in GDMA table: " + columnsGDMA.size());

		//Remote server table list
		//Remote server, column list for table
		Set<Column> columnsRemote = getTableColumns(server, tableGDMA.getName());
		logger.info("Number of columns in remote Table: " + columnsRemote.size() );
		//printRemoteDBTableDetails(tableNameListRemoteDB);
		for (Column column : columnsRemote) {
			logger.info("remote column: " + column.getName());
		}
		
	}//end
		
		
		
		/*

		//end 
		 tableDao.save(tableGDMA);
		 

		 
		
    	LOG.info("Calling resyncColumnList!!");
        // TODO tidy all this up !!
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            DataSource dataSource = dataSourcePool.getTransactionManager(server).getDataSource();
            // TODO make this better
            if (dataSource == null)
                return;
            connection = dataSource.getConnection();

            statement = connection.createStatement();

            Set<Column> columnsGDMA = tableGDMA.getColumns();
            // this is used to keep track of the names - so we can check at the
            // end if we have a column that doesn't exist anymore
            ArrayList<String> columnNamesRemote = new ArrayList<String>();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select * from ");
            if (StringUtils.hasText(server.getPrefix())) {
                stringBuilder.append(server.getPrefix());
                stringBuilder.append('.');
            }
            stringBuilder.append(tableGDMA.getName());
            stringBuilder.append("  where 1 = 0");
            resultSet = statement.executeQuery(stringBuilder.toString());

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                String columnNameRemote = resultSetMetaData.getColumnName(i);
                String tableNameString = resultSetMetaData.getTableName(i);
                
                columnNamesRemote.add(columnNameRemote);
                
                if (columnNameRemote != null) {
                    // see if it's already in the list
                    Column column = getColumn(columnNameRemote, columnsGDMA);
                    if (column == null) {
                        // no so create a new column record
                        column = new Column();
                        column.setName(columnNameRemote);
                        column.setColumnType(resultSetMetaData.getColumnType(i));
                        column.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                        column.setAllowInsert(true);
                        column.setAllowUpdate(true);
                        column.setDisplayed(true);
                        column.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                        column.setSpecial("N");

                        column.setActive(true);

                        column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));                        
                        columnsGDMA.add(column);
                    } else {                    	
                    	if (columnNameRemote.equals(column.getName()) && tableNameString.equals(tableGDMA.getName().trim()) && column.isActive()) {
                    		// update type - just in case
                            column.setColumnType(resultSetMetaData.getColumnType(i));
                            column.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                            column.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                            column.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                            column.setActive(true);
                        }else if(columnNameRemote.equals(column.getName()) && tableNameString.equals(tableGDMA.getName().trim()) && !column.isActive()){
                        	LOG.debug("Server:[" + server.getName() + "], Table:[" + tableGDMA.getName() + "], Column:[ " + column.getName() + " (inactive) found ");
                        	Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                        	String timestamp = currentTimestamp.toString();

                        	String inactiveColumnName = column.getName() + "_" + timestamp;
                        	String activeColumnName = column.getName();
                        	column.setName(inactiveColumnName);
                        	column.setActive(false);
                        	columnsGDMA.add(column);
                        	Column c = new Column();
                            c.setName(activeColumnName);
                            c.setTable(tableGDMA);
                            c.setActive(true);
                            c.setAllowInsert(true);
                            c.setAllowUpdate(true);
                            c.setDisplayed(true);
                            c.setColumnType(resultSetMetaData.getColumnType(i));
                            c.setColumnTypeString(resultSetMetaData.getColumnTypeName(i));
                            c.setNullable(resultSetMetaData.isNullable(i) == ResultSetMetaData.columnNullable);
                            c.setColumnSize(resultSetMetaData.getColumnDisplaySize(i));
                            c.setSpecial("N");

                            columnsGDMA.add(c);                            
                    	}
                    }
                }
            }
            //tableDao.save(table);
            // now sync with column names to make sure none were deleted & reset
            // the orderby
            int idx = 0;
            for (Column columnGDMA : columnsGDMA) {
                if (!columnNamesRemote.contains(columnGDMA.getName())) {
                    //columns.remove(column);
                	columnGDMA.setDisplayed(false);
                	columnGDMA.setActive(false);
                	Set<Table> allTables = server.getTables();    		
            		for(Table t: allTables){
            			Set<Column> tableColumns = t.getColumns();
            			for(Column c: tableColumns){
            				if(c.getDropDownColumnDisplay() != null && (c.getDropDownColumnDisplay().getId() == columnGDMA.getId())){
            					c.setDropDownColumnStore(null);
                				c.setDropDownColumnDisplay(null);
                				tableDao.save(t);
                				LOG.debug("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
                			}  
                			if(c.getDropDownColumnStore() != null && (c.getDropDownColumnStore().getId() == columnGDMA.getId())){
                				c.setDropDownColumnStore(null);
                				c.setDropDownColumnDisplay(null);
                				tableDao.save(t);
                				LOG.debug("Removed foreign key reference from column " + c.getName() + " in table " + t.getName());
                			}                 			            				
            			}
            		}
                } else {
                    columnGDMA.setOrderby(idx);
                    idx++;
                }
            }

            tableDao.save(tableGDMA);
        } catch (Throwable e) {
            LOG.error(e, e);
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }

    }
	*/
    
	private Column getColumn(String columnName, Set<Column> columns) {
        for (Column column : columns) {
            if (columnName.equals(column.getName())) {
                return column;
            }
        }
        return null;
		 

	}





}
