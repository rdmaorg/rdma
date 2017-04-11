package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.UserAccess;

import java.util.List;

/**corresponds to GdmaAjaxFacade in GMDA I, contains operations for working with DATA module, not Admin module
 * GDMA I : before execution each operation calls authecticateUser() to check user in Session*/

public interface DataModuleService {

		public void authenticateUser();
		
		public List<Server> getServerTableList();

		public UserAccess getUserAccessDetails(Long serverId, Long tableId);
			
		public List<Server> getTableDetails(Long serverId, Long tableId);
		
		/*
		public PaginatedResponse getData(PaginatedRequest paginatedRequest);
		
		public void addRecord(UpdateRequest updateRequest);
		
		public int deleteRecords(UpdateRequest updateRequest);
		
		public int updateRecords(UpdateRequest updateRequest);
		*/ 

		public List getDropDownData(Column display, Column store); 
		
			
	
	
}
