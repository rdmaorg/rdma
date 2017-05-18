package ie.clients.gdma2.adaptor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;



import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ColumnDataUpdate;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.UpdateDataRequestDummy;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.spi.interfaces.DataModuleService;
import ie.clients.gdma2.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		EntityUtils.emptyPasswordForServers(servers);
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
	public List getDropdownData(Integer displayColumnId,Integer storeColumnId) {

		logger.info("getDropdownData");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		Column displayColumn = repositoryManager.getColumnRepository().findOne(displayColumnId);
		Column storeColumn = repositoryManager.getColumnRepository().findOne(storeColumnId);

		List dropDownDataList = dynamicDAO.getDropDownData(displayColumn, storeColumn);
		//TODO
		return dropDownDataList;
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

		dynamicDAO.addRecord(createDummyUpdateReqForAddRecord());

	}

	@Override
	public void addRecord(UpdateDataRequest dataRequest) {
		logger.info("addRecord");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		dynamicDAO.addRecord(dataRequest);

	}

	/**
	 * 
	 * @return dummy UpdateDataRequest
	 */
	private UpdateDataRequest createDummyUpdateReqForAddRecord(){

		UpdateDataRequestDummy dummy = new UpdateDataRequestDummy();
		//UpdateDataRequest udr = dummy.createDummyAddSingleRecordForTableName_new_table_test(6, 43);
		UpdateDataRequest udr = dummy.createDummyAddRecordsForAutoIncrementTable_new_table_test_autoincrement(6, 83);

		logger.info("createDummyUpdateReq: ");
		List<List<ColumnDataUpdate>> updates = udr.getUpdates();
		for (List<ColumnDataUpdate> list : updates) {
			for (ColumnDataUpdate columnDataUpdate : list) {
				logger.info("" + columnDataUpdate.getColumnId() + "old val:" + columnDataUpdate.getOldColumnValue()
						+ " , new val: " + columnDataUpdate.getNewColumnValue() );

			}
		}
		return udr;
	}

	/*using dummy logic until we define how data is transfered from UI to BE*/
	@Override
	public int updateRecords(Integer tableId) {
		logger.info("updateRecords");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		int updateRecords = dynamicDAO.updateRecords(createDummyUpdateReq());
		return updateRecords;

	}

	@Override
	public int updateRecords(UpdateDataRequest dataRequest) {
		logger.info("updateRecords");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		int updateRecords = dynamicDAO.updateRecords(dataRequest);
		return updateRecords;

	}


	/**
	 * select * from table_gdma2 where name = 'new_table_test_autoincrement'; 	id=136

	 * @return dummy UpdateDataRequest
	 */
	private UpdateDataRequest createDummyUpdateReq(){

		UpdateDataRequestDummy dummy = new UpdateDataRequestDummy();

		//UPDATE - no date
		//UpdateDataRequest udr = dummy.createDummyUpdateRequestForAutoIncrementTable_new_table_test_autoincrement(6, 136);

		//upadte witj date
		UpdateDataRequest udr = dummy.createDummyUpdateRequestForAutoIncrementTableWithDate_new_table_test_autoincrement(6, 136);

		logger.info("createDummyUpdateReq: ");
		List<List<ColumnDataUpdate>> updates = udr.getUpdates();
		for (List<ColumnDataUpdate> list : updates) {
			for (ColumnDataUpdate columnDataUpdate : list) {
				logger.info("" + columnDataUpdate.getColumnId() + ", old val: " + columnDataUpdate.getOldColumnValue() + 
						" , new val:" + columnDataUpdate.getNewColumnValue());
			}
		}
		return udr;
	}




	@Override
	public int deleteRecords(Integer tableId) {
		logger.info("deleteRecords");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		logger.info("createDummy DELETE Req: ");

		UpdateDataRequestDummy dummyDelete = new UpdateDataRequestDummy();
		UpdateDataRequest updateDataRequest = dummyDelete.createDummyDeleteRecordsForAutoIncrementTable_new_table_test_autoincrement(6, 136);

		return dynamicDAO.deleteRecords(updateDataRequest);
	}

	@Override
	public int deleteRecords(UpdateDataRequest dataRequest) {
		logger.info("deleteRecords");
		return dynamicDAO.deleteRecords(dataRequest);
	}

	@Override
	public int bulkImport(Integer tableId, MultipartFile file) {
		logger.info("bulkImport for table: " + tableId) ;

		Table table = repositoryManager.getTableRepository().findOne(tableId);
		Server server = table.getServer();
		final Set<Column> columns = repositoryManager.getColumnRepository().findByTableId(tableId);

		if(table == null || server == null || columns == null){
			logger.error("Error during bulk upload: server, table or columns are NULL!?");
			return -1; //TODO 
		}

		return dynamicDAO.bulkImport(server, table, columns, file);
		
	}




}
