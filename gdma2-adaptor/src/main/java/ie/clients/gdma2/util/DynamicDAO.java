package ie.clients.gdma2.util;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


public interface DynamicDAO {

	public void setDataSourcePool(DataSourcePool dataSourcePool);
	
	public DataSourcePool getDataSourcePool();

	
	/*Get table names from Remote server - metadata */
	public List<String> getSqlGetTables(Server server, String sqlGetTables);
	
	/*Get columns from Table on remote Server - metadata */
	public Set<Column> getTableColumns(Server server, String tableName);
	
	/*Synch local Tables for Server by calling Remote Server - special case called from Admin module ONLY*/
	public void synchTablesForServer(Server server, List<Table> tableList);
	
	/*synch local Colulmns for the Table - special case called from Admin module ONLY and after table synch*/
	public void synchColumnsForTable(Server server, Table table, Set<Column> columns);
	
	 
	
	/*DATA module*/
		
	/*  OLD CODE - GDMA 1*/


	

	/*count data for table with/without filterw in WHERE clause*/
	public Long getCount(Server server, Table table, List<Filter> filters);
	
	public List getTableData(Table table, Server server, Column orderByColumn,
			List<Filter> filters, String orderDirection, int startIndex,
			int length);

	

	
	
	/*
	public PaginatedResponse get(PaginatedRequest paginatedRequest);

	public void addRecord(UpdateRequest updateRequest);
	*/
	/*DATA module - add record to the table */
	public void addRecord(UpdateDataRequest updateRequest);
	
	/**
	 * Update one or more records
	 * 
	 * @param updateRequest
	 * @return the numebr of records updated
	 */
	public int updateRecords(UpdateDataRequest updateRequest);

	/**
	 * Delete one or more records
	 * 
	 * @param updateRequest
	 * @return the number of records delete
	 */
	public int deleteRecords(UpdateDataRequest updateRequest);

	/**
	 * Get a list of drop down values and text for a column
	 * 
	 * @param display
	 *            the display column
	 * @param store
	 *            the column to store
	 * @return
	 */
	public List getDropDownData(Column display, Column store);

	
	
	

	
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
	public int bulkImport(Server server, Table table, Set<Column> columns, MultipartFile file);

}
