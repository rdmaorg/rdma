package ie.clients.gdma2.util;

import java.util.Set;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author Avnet
 *
 */
public class ServerUtils {

	private static Logger logger = LoggerFactory.getLogger(ServerUtils.class);

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
	 * This method will get the list of tables from the database and then  iterate over the list of tables form the server object. 
	 * It will see if any are missing,. If there is, then it will create a new table object and save it.
	 * 
	 * 
	 * 
	 * @param server
	 */
	@SuppressWarnings("null")
	public void resyncTableList(Server server) {
		
		
		
		//checking 'Maintened' DB for Table changes
		
		
		
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

