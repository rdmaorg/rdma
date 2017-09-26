package ie.clients.gdma2.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.ui.DataTableDropDown;
import ie.clients.gdma2.domain.ui.Filter;


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

	/**
	 * 
	 * @param table
	 * @param server
	 * @param orderByColumn
	 * @param filters
	 * @param orderDirection
	 * @param startIndex
	 * @param length
	 * @return
	 */
	public List getEditableTableData(Table table, Server server, Column orderByColumn, List<Filter> filters,
			String orderDirection, int startIndex, int length);

	/**
	 * 
	 * @param table
	 * @return
	 */
	public Map<String, List<DataTableDropDown>> getDatatableEditorOptions(Table table);

}
