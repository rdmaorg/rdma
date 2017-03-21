package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * called from GdmaAdminAjaxFacade
 * @author Avnet
 *
 */
public class ServerUtils {

	private static Logger logger = LoggerFactory.getLogger(ServerUtils.class);

	/**
	 * dynamically creates javax.sql.DataSource, based on DB data : registered Server and ConnectionType 
	 *  url (containing database name) : 	jdbc:pgsql://localhost:5432/gdma20
	 *  username, password 
	 *  jdbc Driver name :  DB column 'connection_types_gdma2.connection_class' e.g. "org.postgresql.Driver"
	 * @param server
	 * @return DataSource
	 */
	public static DataSource getDataSourceForServer(Server server){

		logger.info("getDataSourceForServer()");
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

		dataSourceBuilder.url(server.getConnectionUrl());
		dataSourceBuilder.username(server.getUsername());
		dataSourceBuilder.password(server.getPassword());
		dataSourceBuilder.driverClassName(server.getConnectionType().getConnectionClass());
		
		return dataSourceBuilder.build();   
	}


	/**
	 * Queries DB column 'connection_types_gdma2.select_get_tables' e.g. 'SHOW TABLES' for MySQL
	 * result must be single column containing all DB Table names on Server 
	 * @param jdbcTemplate
	 * @param SqlGetTables
	 * @return List of Table names on Server
	 */
	public static List<String> getSqlGetTables(JdbcTemplate jdbcTemplate, String SqlGetTables){
		logger.info("getSqlGetTables(): " + SqlGetTables);
		
		List<String> tableList  = jdbcTemplate.queryForList(SqlGetTables, String.class);
		for (String table : tableList) {
			logger.info("tableName: "  + table);
		}
		return tableList;
	}
	
	/**
	 * Uses metadata to fetch DB Columns for given DB Table
	 * maps column metadata to Column Entity
	 * creates Set of Column Entities to represent all Columns of Table
	 * @param jdbcTemplate
	 * @param table
	 * @return 
	 */
	public static Set<Column> getTableColumns(JdbcTemplate jdbcTemplate, String table){
		logger.info("getTableColumns for table: " + table);
		String sqlTableMetadata = "select * from " + table + " where 1 = 0";
		Set<Column> columns = jdbcTemplate.query(sqlTableMetadata, new ResultSetExtractorColumns());
		
		for (Column column : columns) {
			logger.info(column.getName());
		}
		return columns;
		
	}
	
	/*
	  private DataSourcePool dataSourcePool;
      private ServerDao serverDao;
      private TableDao tableDao;
	  private UserAccessDao userAccessDao;
	 */

	/**
	 * Called from
	 * 	GdmaAdminAjaxFacade:
	 * 	  
	 * 		This method will get the list of tables from the database and then	iterate over the list of tables form the server object.
	 *  	It will see if any are missing,
	 *  		 If there is, then it will create a new table object and save it.
	 * 				
	 * 				public Set<Table> getTablesForServer(Long serverId) {
	 * 						... 
	 * 
	 * This method will get the list of tables from the database and then
	 * iterate over the list of tables form the server object. It will see if
	 * any are missing,. If there is, then it will create a new table object and
	 * save it.
	 * 
	 * @param server
	 */
	@SuppressWarnings("null")
	public void resyncTableList(Server server) {

	}




	/** Called from 
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
	 * @param table
	 */
	public void resyncColumnList(Server server, Table table) {

	}


}

