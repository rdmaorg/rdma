package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;

import java.util.List;

/**corresponds to GdmaAjaxFacade in GMDA I, contains operations for working with DATA module, not Admin module
 * GDMA I : before execution each operation calls authecticateUser() to check user in Session
 * GDMA II: CAS used for authentication, every operation will simply call UserContextProvider to get authenticated user*/

public interface DataModuleService {

		
		/* in GDMA 1, this was: getServerTableList
		 * 3 actions executed one after one: Return list of all ACTIVE servers 
		 * where there are ACTIVE tables that user has been granted access by Admin (as precondition)*/
		public List<Server> getActiveServers();
		public List<Table> getActiveTables(Integer serverId);
		public List<Column> getActiveColumns(Integer tableId);

		public UserAccess getUserAccessDetails(Long serverId, Long tableId);
			
		public List<Server> getTableDetails(Long serverId, Long tableId);
		
		/*
		public PaginatedResponse getData(PaginatedRequest paginatedRequest);
		
		public void addRecord(UpdateRequest updateRequest);
		
		public int deleteRecords(UpdateRequest updateRequest);
		
		public int updateRecords(UpdateRequest updateRequest);
		*/ 

		
		
		public List<Column> getDropdownData(Integer displayColumnId,
				Integer storeColumnId);
		
		//public List getDropDownData(Column display, Column store);



		 
		
			
	
	
}
