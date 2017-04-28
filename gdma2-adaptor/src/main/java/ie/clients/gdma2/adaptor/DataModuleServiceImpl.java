package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.spi.interfaces.DataModuleService;
import ie.clients.gdma2.util.ColumnDataUpdate;
import ie.clients.gdma2.util.UpdateDataRequest;
import ie.clients.gdma2.util.UpdateDataRequestDummy;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * all implemented operations will use userContextProvider to get authenticated User  
 * @author Avnet
 *
 */
@Service
public class DataModuleServiceImpl extends BaseServiceImpl implements DataModuleService{

	private static final Logger logger = LoggerFactory.getLogger(DataModuleServiceImpl.class);


	@Override
	public List<Server> getActiveServers() {
		logger.info("getActiveServers" );
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		List<Server> servers = repositoryManager.getServerRepository().activeServersWithActiveTablesForUser(userContextProvider.getLoggedInUserName());
		return servers;
	}



	@Override
	public List<Table> getActiveTables(Integer serverId) {
		logger.info("getActiveTables");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		return repositoryManager.getTableRepository().activeTablesOnActiveServerForUser(userContextProvider.getLoggedInUserName(), serverId);

	}

	@Override
	public List<Column> getActiveColumns(Integer tableId) {
		logger.info("getActiveColumns");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		return repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(tableId);

	}


	@Override
	public UserAccess getUserAccessDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public List<Server> getTableDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub

		return null;
	}


	@Override
	public List<Column> getDropdownData(Integer displayColumnId,Integer storeColumnId) {

		logger.info("getDropdownData");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		Column displayColumn = repositoryManager.getColumnRepository().findOne(displayColumnId);
		Column storeColumn = repositoryManager.getColumnRepository().findOne(storeColumnId);

		List dropDownData = dynamicDAO.getDropDownData(displayColumn, storeColumn);
		//TODO
		return null;
	}



	/*using dummy logic until we define how data is transfered from UI to BE*/
	@Override
	public void addRecord(Integer tableId) {
		logger.info("addRecord");
		logger.info("user: " + userContextProvider.getLoggedInUserName());

		//create UpdateRequest
		//UpdateDataRequest updateReq = new UpdateDataRequest();
		//updateReq.setServerId(serverId);
		//updateReq.setTableId(tableId);
		// TODO coming from UI updateReq.setUpdates(List<List<ColumnDataUpdate>> updates);
		
		//TODO change to real call 
		
		
		dynamicDAO.addRecord(createDummyUpdateReq());

	}

	
	/**
	 * 
	 * @return dummy UpdateDataRequest
	 */
	private UpdateDataRequest createDummyUpdateReq(){
	
		UpdateDataRequestDummy dummy = new UpdateDataRequestDummy();
		//UpdateDataRequest udr = dummy.createDummyAddSingleRecordForTableName_new_table_test(6, 43);
		UpdateDataRequest udr = dummy.createDummyAddRecordsForAutoIncrementTable_new_table_test_autoincrement(6, 83);
		
		logger.info("createDummyUpdateReq: ");
		List<List<ColumnDataUpdate>> updates = udr.getUpdates();
		for (List<ColumnDataUpdate> list : updates) {
			 for (ColumnDataUpdate columnDataUpdate : list) {
				 logger.info("" + columnDataUpdate.getColumnId());
				 logger.info(columnDataUpdate.getNewColumnValue());
				 logger.info(columnDataUpdate.getOldColumnValue());
				
			}
		}
		return udr;
	}
}
