package ie.clients.gdma2.util;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
	 * @param String table
	 * @return Set of Column Entities
	 */
	@Override
	public Set<Column> getTableColumns(Server server, String table) {
		logger.info("getTableColumns for table: " + table);
		String sqlTableMetadata = "select * from " + table + " where 1 = 0";
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
	@Override
	public List<Table> getTablesForServerAfterSynch(Server server) {
		logger.info("getTablesForServerAfterSynch: " + server.getName());
		// TODO use an AOP trigger for this
		logger.info("starting table SYNCH");
		
		//Comment the next line out if moving resynch functionality to the Refresh button
		resyncTableList(server);
		logger.info("...table SYNCH ended");

		//reload table list after synch
		//TODO OPEN Q - after saving TABLE - updating server that contains it
		List<Table> synchedTableList = repositoryManager.getTableRepository().findByServerId(server.getId());
		
		//tables.removeIf((Table table) -> !table.isActive()); // JAVA 8
		
		for (Iterator<Table> tablesIterator = synchedTableList.iterator(); tablesIterator.hasNext();) {
			Table table = tablesIterator.next();
			if(!table.isActive()){
				tablesIterator.remove();
			}
		}
		
		return synchedTableList;
		
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

			1.1 TABLE NAMES MATCH

			Tables are in synch if Table names are equal and Table in ‘GDMA’ DB is ACTIVE.
				1. a)	Add back existing GDMA table to the result Set of Tables

				1. b) If tables names are the same but Table in ‘GDMA’ DB is NOT ACTIVE, then make 2 changes : 

			 		b1) UPDATE Inactive GDMA table name - add Timestamp suffix : TABLENAME_timestamp and change ACTIVE flag to TRUE <BR>
					b2) Add back existing GDMA table to the result Set of Tables (now with UPDATEed name)

					b3) Create new Entity Table using name of Remote DB, set serverId, set active to TRUE
					b4) Add New Table to result Set of Tables 

			1.1	NO MATCH - 
				c)  Add New Table to result Set of Tables - Create new Entity Table using name of Remote DB, set serverId, set active to TRUE

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
	public void resyncTableList(Server server) {
		
		logger.info("resyncTableList: " + server.getName());
		//TODO define in parent interface??

		//RESULT of synch   
		Set<Table> tablesSynchResult = new HashSet<Table>(); 
		
		//GDMA tables list
		Set<Table> tablesGDMA = server.getTables();
		logger.info("tablesGDMA.size(): " + tablesGDMA.size());

		//Remote server table list
		logger.info("remote server table list:");
		List<String> tableNameListRemoteDB = getSqlGetTables(server, server.getConnectionType().getSqlGetTables());

		//LOOP remote DB table names
		for (String tableNameRemote : tableNameListRemoteDB) {
			logger.info("tableNameRemote:" + tableNameRemote);
			boolean nameFound = false;

			for(Table tableGDMA : tablesGDMA){
				logger.info("        tableGDMA:" + tableGDMA.getName());
				//1.1 TABLE NAMES MATCH
				if(tableGDMA.getName().equalsIgnoreCase(tableNameRemote)){
					logger.info("	tableNameRemote:" + tableNameRemote + " is found on local");
					nameFound = true; 
					resolveTablesWithSameNames(tablesSynchResult, tableGDMA, tableNameRemote, server);
					break;
				}
			}//for2
		
			//1.1	NO MATCH - remote DB name not found on local 
			if(!nameFound){
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
		repositoryManager.getTableRepository().save(tablesSynchResult);
	}

	
	private void printSynchResult(Set<Table> tablesSynchResult) {
		logger.info("printSynchResult...");
		for (Table table : tablesSynchResult) {
			logger.info("synched table name: " + table.getName());
		}
	}

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

	/**
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

	

}
