package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

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
	 * 
	 * /** Called from 
	 * 	 GdmaAdminAjaxFacade:
	 * 
	 * 		Only called from admin and it's a special case. We need to re-sync the
	 * 		columns before calling it, just to ensure that the list is current
	 * 		
	 * 				 public Set<Column> getColumnsForTable(Long serverId, Long tableId) {
	 * 						
	 * 					...
	 * 
	 * Connect to server, get requested table,  get the list of columns from the database
	 *  and then iterate over the list to see if any are missing,. 
	 *  If there is, then it will create a new column object and save it.
	 * 
	 * 
	 * Maintaining list of Tables on Server. 

		
		FIRST TIME: 
		When Admin user connects to ‘Maintained’ server for the first time, there are no Tables for that Server in ‘Metadata’ yet. 
		App logic is executed – all Tables and their Columns are scanned from ‘Maintained’ DB and Stored in ‘Metadata’ DB. <BR>
		
		AFTER FIRST TIME: 
		After Initial usage, ‘Maintained’ DB can be changed : 
		-	NO CHANGE 
		Biz rule: Tables are in synch if Table names are equal and Table in ‘Metadata’ DB is ACTIVE.
		If tables names are the same but Table in ‘Metadata’ DB is NOT ACTIVE, then 
		1.	Change table name – add Timestamp suffix : TABLENAME_timestamp and change ACTIVE flag to TRUE <BR>
		
		-	NEW tables are added
		Biz rule: Create a new Table record in ‘Metadata’ DB, set ACTIVE = true <BR>
		
		-	DELETED table in ‘Maintained’ DB (still existing in ‘Metadata’ DB) 
		BIZ Rule: If the table has been DELETED from the 'maintained' database then:
		1.	make it inactive in GDMA, so that it is unavailable to users but remains in the database for auditing purposes.
		2.	Delete records in link table USER_ACCESS for that tableId
		3.	Go through Column list and check FKs
	*/
	
	/*
	public void resyncTableList(Server server) {
		//TODO define in paretn interface??
		
		Set<Table> tablesGDMA = server.getTables();
		List<String> tableNameListMaintainedDB = getSqlGetTables(server, server.getConnectionType().getSqlGetTables());
		
		for (String tableName : tableNameListMaintainedDB) { //check if all DELETED
			
			for(Table table : tablesGDMA){
				
				if(table.getName().equalsIgnoreCase(tableName) && table.isActive()){
					
				} 
				
				if(table.getName().equalsIgnoreCase(tableName) && !table.isActive()){
					
				}
				
			}
			
		}
	}
	*/
	
}
