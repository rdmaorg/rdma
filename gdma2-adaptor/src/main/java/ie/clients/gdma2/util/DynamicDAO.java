package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

import java.util.List;
import java.util.Set;


public interface DynamicDAO {

	public void setDataSourcePool(DataSourcePool dataSourcePool);
	
	public DataSourcePool getDataSourcePool();

	
	/*GET TABLES FOR SERVER*/
	public List<String> getSqlGetTables(Server server, String sqlGetTables);
	
	/*GET COLUMNS FOR TABLE*/
	public Set<Column> getTableColumns(Server server, String tableName);
	
	/*GET ACTIVE TABLES FOR SERVER AFTER SYNCH - special case called from Admin module ONLY*/
	public List<Table> getTablesForServerAfterSynch(Integer serverId);
	
    /* GET ACTIVE Columns for table - re-sync the columns for resynched Table from previous step (getTablesForServerAfterSynch()
     * this call happens after tables have been synched and user select 1 synched Table - special case called from Admin module ONLY */ 
	public List<Column> getColumnsForTableAfterSynch(Integer serverId, Integer tableId);
	
	/*  OLD CODE - GDMA 1*/
	
	
	/*
	public PaginatedResponse get(PaginatedRequest paginatedRequest);

	public Long getCount(Server server, Table table, List<Filter> filters);

	public void addRecord(UpdateRequest updateRequest);
	*/
	
	/**
	 * Update one or more records
	 * 
	 * @param updateRequest
	 * @return the numebr of records updated
	 */
	//public int updateRecords(UpdateRequest updateRequest);

	/**
	 * Delete one or more records
	 * 
	 * @param updateRequest
	 * @return the number of records delete
	 */
	//public int deleteRecords(UpdateRequest updateRequest);

	/**
	 * Get a list of drop down values and text for a column
	 * 
	 * @param display
	 *            the display column
	 * @param store
	 *            the column to store
	 * @return
	 */
	//public List getDropDownData(Column display, Column store);

	/**
	 * Get the list of columns for a select through the SQL Console
	 * 
	 * @param serverId
	 * @param sql
	 * @return
	 */
	//public List<Column> executeSelectGetColumns(Long serverId, String sql);

	/**
	 * Execute a select from the SQL console
	 * 
	 * @param paginatedRequest
	 * @return
	 */
	//public PaginatedSqlResponse executeSelect(PaginatedSqlRequest paginatedSqlRequest);

	/**
	 * Perform bulk data import
	 * 
	 * @param server
	 * @param table
	 * @param data
	 * @return number of rows imported
	 * @throws IOException
	 */
	//public int bulkImport(Server server, Table table, InputStream data) throws IOException;

}
