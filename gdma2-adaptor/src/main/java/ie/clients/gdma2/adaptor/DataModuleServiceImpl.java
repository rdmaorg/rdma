package ie.clients.gdma2.adaptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ColumnDataUpdate;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.UpdateDataRequestDummy;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.DataTableDropDown;
import ie.clients.gdma2.domain.ui.Filter;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.ServiceException;
import ie.clients.gdma2.spi.interfaces.DataModuleService;
import ie.clients.gdma2.util.CsvDownloader;
import ie.clients.gdma2.util.EntityUtils;
import ie.clients.gdma2.util.TableRowDTO;

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

		List<Column> activeTableList = repositoryManager.getColumnRepository().findByTableIdAndActiveTrueAndDisplayedTrue(tableId);

		//remove tables and all parent objects
		//Daniel Serva 04/08/2017: WHY????
//		for (Column column : activeTableList) {
//			column.setTable(null);
//			if(column.getDropDownColumnDisplay() != null){
//				column.getDropDownColumnDisplay().setTable(null);
//			}
//			if(column.getDropDownColumnStore() != null){
//				column.getDropDownColumnStore().setTable(null);
//			}
//		}

		return activeTableList;
	}
	
	public List<Column> getTableMetadata(Integer tableId) {
		List<Column> tableMetadata = getActiveColumns(tableId);
		populateDatatableEditorOptions(tableMetadata);
		return tableMetadata;
	}

	private List<Column> populateDatatableEditorOptions(List<Column> tableMetadata){
		tableMetadata.forEach(metadataColumn->{
			if(metadataColumn.getDropDownColumnDisplay() != null && metadataColumn.getDropDownColumnStore() != null){
				//fetch Lookup table - all values	
				List<DataTableDropDown> datatableEditorFieldOptions = new ArrayList<DataTableDropDown>();
				List<List<Object>> dropDownDataRows = dynamicDAO.getDropDownData(metadataColumn.getDropDownColumnDisplay(), metadataColumn.getDropDownColumnStore());
				for (List<Object> list : dropDownDataRows) {
					DataTableDropDown dataTableDropDown = new DataTableDropDown();
					dataTableDropDown.setValue(list.get(1).toString());
					dataTableDropDown.setLabel(list.get(2).toString());
					datatableEditorFieldOptions.add(dataTableDropDown);
				}
				logger.info("columnName: " + metadataColumn.getName());
				metadataColumn.setDatatableEditorFieldOptions(datatableEditorFieldOptions);
			}
		});
		return tableMetadata;
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
		//SINGLE PK TEST
		//UpdateDataRequest updateDataRequest = dummyDelete.createDummyDeleteRecordsForAutoIncrementTable_new_table_test_autoincrement(6, 136);

		//COMPOSITE PK TEST (make sure DB table classicmodels.voting is used and metadata is fetched in admin module)

		/*tableId = 4189
		 * 
				Columns
				2260	personID	
				2259	questionID	
				2261	vote	
				2262	votenumber

				 PRIMARY KEY (`questionID`,`personID`)
		 */

		UpdateDataRequest updateDataRequest = dummyDelete.createDummyDeleteRecordsForVote(6, 4189);

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


	@Override
	public List<UserAccess> getUserAccessForUserOnTable(Integer tableId) {
		logger.info("getUserAccessForUserOnTable" );
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		return repositoryManager.getUserAccessRepository().getUserAccessForUserOnTable(userContextProvider.getLoggedInUserName(), tableId);
	}


	/*based on file type initiate proper file type creator
	 * load metadata column names to created HEADER
	 * iterate over paginated result and add BODY*/
	@Override
	public <T> String dataExport(Integer tableId, String extension,	PaginatedTableResponse<T> resp) {

		Table table = repositoryManager.getTableRepository().findOne(tableId);
		if (null == table){
			throw new ServiceException("Error! Table with id:" + tableId + "does not exists!");
		}

		//make sure order of column in Header match order of column data in Body (paginated response)
		List<Column> activeColumns = repositoryManager.getColumnRepository().findByTableIdAndActiveTrueAndDisplayedTrue(tableId);
		List<String> headers = new ArrayList<String>();
		for (Column column : activeColumns) {
			headers.add(column.getName());
		}

		List<TableRowDTO> rows = (List<TableRowDTO>) resp.getData();
		//TODO based on 'extension' call ExcelDownloader or PDFDwonloader
		return CsvDownloader.createCSV(headers, rows);

	}

	@Override
	public UpdateDataRequest extractDataRequest(Integer serverId, Integer tableId, Map<String, String> reqParams) {
		UpdateDataRequest dataRequest = new UpdateDataRequest();
		List<ColumnDataUpdate> row = new ArrayList<ColumnDataUpdate>();
		dataRequest.setServerId(serverId);
		dataRequest.setTableId(tableId);
		reqParams.remove("action");
		final List<Column> columnsMetadata = getActiveColumns(tableId);
		columnsMetadata.removeIf(c -> !c.isDisplayed());
		List<String> extractedRowKeyList = extractRowKeyList(reqParams);
		for (String rowKey : extractedRowKeyList) {
			int i = 0,j = 0;
			for (Column columnMetadata : columnsMetadata) {
				for (String key: reqParams.keySet()) {
					if(key.startsWith(rowKey)){
						if(i == j){
							ColumnDataUpdate newColumn = new ColumnDataUpdate();
							newColumn.setColumnId(columnMetadata.getId());
							newColumn.setNewColumnValue(reqParams.get(key));
							newColumn.setOldColumnValue(reqParams.get(key.replace("[columns]", "[oldValues]")));
							row.add(newColumn);
						}
						j++;
					}
				}
				j=0;
				i++;
			}
			dataRequest.getUpdates().add(row);
			
		}
		return dataRequest;
	}
	
	@Override
	public List<UpdateDataRequest> extractUpdateDataRequestList(Integer serverId, Integer tableId, Map<String, String> reqParams, List<Filter> filterList) {
		List<UpdateDataRequest> updateDataRequestList = new ArrayList<UpdateDataRequest>();
		reqParams.remove("action");
		final List<Column> columnsMetadata = getActiveColumns(tableId);
		columnsMetadata.removeIf(c -> !c.isDisplayed());
		List<String> extractedRowKeyList = extractRowKeyList(reqParams);
		for (String rowKey : extractedRowKeyList) {
			UpdateDataRequest dataRequest = new UpdateDataRequest();
			List<ColumnDataUpdate> row = new ArrayList<ColumnDataUpdate>();
			dataRequest.setServerId(serverId);
			dataRequest.setTableId(tableId);
			int i = 0,j = 0;
			for (Column columnMetadata : columnsMetadata) {
				for (String key: reqParams.keySet()) {
					//checking key does not contain "[oldValues]" to avoid unnecessary loops
					if(key.startsWith(rowKey) && !key.contains("[oldValues]")){
						if(i == j){
							ColumnDataUpdate newColumn = new ColumnDataUpdate();
							newColumn.setColumnId(columnMetadata.getId());
							newColumn.setNewColumnValue(reqParams.get(key));
							newColumn.setOldColumnValue(reqParams.get(key.replace("[columns]", "[oldValues]")));
							if(columnMetadata.isPrimarykey() || columnMetadata.isAllowUpdate() || !"N".equalsIgnoreCase(columnMetadata.getSpecial())){
								row.add(newColumn);
							}
							if(columnMetadata.isPrimarykey()){
								Filter filter = new Filter();
								filter.setColumnId(columnMetadata.getId());
								filter.setColumnName(columnMetadata.getName());
								filter.setFilterValue(reqParams.get(key));
								filter.setColumnType(columnMetadata.getColumnType());
								filterList.add(filter);
							}
						}
						j++;
					}
				}
				j=0;
				i++;
			}
			dataRequest.getUpdates().add(row);
			updateDataRequestList.add(dataRequest);
		}
		return updateDataRequestList;
	}
	
	
	private List<String> extractRowKeyList(Map<String, String> reqParams) {
		List<String> rowKeyList = new ArrayList<String>();
		Set<String> keySet = reqParams.keySet();
		for (String fullKey : keySet) {
			String rowKey = fullKey.split("]")[0];
			rowKey = rowKey.concat("]");
			if(!rowKeyList.contains(rowKey)){
				rowKeyList.add(rowKey);	
			}
		}
		return rowKeyList;
	}

}
