package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.Filter;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**corresponds to GdmaAjaxFacade in GMDA I, contains operations for working with DATA module, not Admin module
 * GDMA I : before execution each operation calls authecticateUser() to check user in Session
 * GDMA II: CAS used for authentication, every operation will simply call UserContextProvider to get authenticated user*/

public interface DataModuleService {

		
		/* in GDMA 1, this was: getServerTableList
		 * Here we use 3 separate actions executed one after one: get servers, get tables for server, get columns for table*/
		public List<Server> getActiveServers();
		public List<Table> getActiveTables(Integer serverId);
		public List<Column> getActiveColumns(Integer tableId);

		/*are this to needed???*/
		public UserAccess getUserAccessDetails(Long serverId, Long tableId);
		public List<Server> getTableDetails(Long serverId, Long tableId);
		
		/*
		public PaginatedResponse getData(PaginatedRequest paginatedRequest);
		
		public void addRecord(UpdateRequest updateRequest);
		
		public int deleteRecords(UpdateRequest updateRequest);
		
		public int updateRecords(UpdateRequest updateRequest);
		*/ 

		
		
		public List getDropdownData(Integer displayColumnId, Integer storeColumnId);
		

		/*add/upadate/delete Records - for Columns */
		public void addRecord(Integer tableId);
		public void addRecord(UpdateDataRequest dataRequest);
		
		public int updateRecords(Integer tableId);
		public int updateRecords(UpdateDataRequest dataRequest);
		
		public int deleteRecords(Integer tableId);
		public int deleteRecords(UpdateDataRequest dataRequest);
		
		//bulk import of remote DB Table data 
		public int bulkImport(Integer tableId, MultipartFile file);

		//data export - download
		public <T> String dataExport(Integer tableId, String extension, PaginatedTableResponse<T> resp);
	
		
		//user access on Table for logged in user
		public List<UserAccess> getUserAccessForUserOnTable(Integer tableId);
		
		public UpdateDataRequest extractDataRequest(Integer serverId, Integer tableId, Map<String, String> reqParams);
		
		public List<UpdateDataRequest> extractUpdateDataRequestList(Integer serverId, Integer tableId,
				Map<String, String> reqParams, List<Filter> filterList);
		
		public List<Column> getTableMetadata(Integer tableId);
		
	
}
