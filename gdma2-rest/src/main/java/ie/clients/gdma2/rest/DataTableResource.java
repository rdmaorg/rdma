package ie.clients.gdma2.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ColumnDataUpdate;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.ui.DropDownColumn;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

/*
@Controller
@RequestMapping("rest/datatable")
*/
@RestController
@RequestMapping("rest/datatable")
public class DataTableResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(DataTableResource.class);
	
	
	
	/*METADATA PART*/
	
	/*Regular user: list all active servers for user after login.
	 * 
	 * User needs to have userAccess to Active tables on this server to get result: 
	 * 		https://localhost/gdma2/rest/datatable/servers 
	 * 
	 * ...After this call user can calls a list of active tables on one of the servers: 
	 *	 	https://localhost/gdma2/rest/datatable/tables/server/6
	 * ...and then user can call for a column list of table  
	 * 		https://localhost/gdma2/rest/datatable/columns/table/133
	 * ....
	 */
	
	/*	https://localhost/gdma2/rest/datatable/servers	*/
	@RequestMapping(value = "/servers")
	public List<Server> getActiveServersContainingActiveTablesForUser(@RequestParam Map<String, String> params){
		return serviceFacade.getDataModuleService().getActiveServers();
	}
	
	/*	https://localhost/gdma2/rest/datatable/tables/server/6	*/
	@RequestMapping(value = "/tables/server/{id}")
	public List<Table> activeTablesOnActiveServerForUser(@PathVariable("id") Integer serverId){
		logger.info("activeTablesOnActiveServerForUser, serverId: " + serverId);
		return serviceFacade.getDataModuleService().getActiveTables(serverId);
	}
	
	/*get all Active columns STRUCTURE (OR METADATA) for given Active table on Active server, based on userName
	 * 		https://localhost/gdma2/rest/datatable/columns/table/133  
	 * in GDMA1 : GdmaAjaxFacade.getTableDetails*/
	@RequestMapping(value = "/columns/table/{tableId}", method = RequestMethod.GET)
	public List<Column> getActiveColumns(@PathVariable("tableId") Integer tableId){
		logger.info("getActiveColumnsForActiveTableOnActiveServer");
		return serviceFacade.getDataModuleService().getActiveColumns(tableId);
	}
	
	
	/*DATA PART*/
	
	/*paginated active columns DATA for : Active server, active table Table, logged in user with UserAccess.allowDisplay = true  */
	/* 	https://localhost/gdma2/rest/datatable/table/133?length=22&order[0][column]=628   WITH LENGTH and OrderBY column CITY; use 629 for country 
	 * https://localhost/gdma2/rest/datatable/table/133?length=22&order[0][column]=6  WITH LENGTH and WITHOUT OrderBY column
	 * */
	@RequestMapping(value = "/table/{id}", method = RequestMethod.GET)
	public PaginatedTableResponse<Column> getTableData(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		
		logger.debug("tableData: " + tableId);
		
		List<Object> filtersTODO = new ArrayList<Object>(); //see Filter.java
		//  order[0][column]:1
		//String orderByColumn = "id";
		
		//values are not 0,1,2... but 0, column1 PK, column2 PK like : 0,12,14,478
		int orderByColumnPosition = getOrderByColumn(reqParams);
		
		/*
		String returnJson = "{" + "\"data\":" + convertToAjaxResponse(systemConfigurationProvider.findAll()) + ","
				+ "\"options\": []," + "\"files\": []" + "}";
		return returnJson;
		*/
		
		logger.info("orderByColumn, column Position: " + orderByColumnPosition);
		
		//PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getTableData(
		PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getTableDataWithColumnNamesAndDropdowns(
		tableId, 
				filtersTODO,
				//getSearchValue(reqParams), //NO SEARCH HERE
				orderByColumnPosition,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));
		
		resp.setDraw(getDraw(reqParams));
		logger.debug("getColumnsPaginatedTable ended");
		return resp;
		
		
	}
	
	@RequestMapping(value = "/tablewithdropdowntest/{id}", method = RequestMethod.GET)
	public PaginatedTableResponse<Object> getTableDataWithDropDownTest(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		
		logger.debug("getTableDataWithDropDownTest tableData: " + tableId);
		
		List<Object> rowA = new ArrayList<Object>();
		rowA.add(1);
		rowA.add("France");
		rowA.add("Nantes");
		rowA.add("Carine ");
		rowA.add("44000");
		DropDownColumn dropdownColumn = new DropDownColumn();
			dropdownColumn.setValue("1370");
			//dropdownColumn.setDid(15);
			//dropdownColumn.setSid(21);
			dropdownColumn.setDid(628);
			dropdownColumn.setSid(629);
			dropdownColumn.setDropdownOptions(getDropdownData(dropdownColumn.getDid(),dropdownColumn.getSid()));
		rowA.add(dropdownColumn);
		rowA.add(103);
		rowA.add("Atelier graphique");
		rowA.add("40.32.2555");
		rowA.add("54, rue Royale");
		rowA.add(21000);
		rowA.add(null);
		rowA.add("Schmitt");
		rowA.add(null);
		
		List<Object> rowB = new ArrayList<Object>();
		rowB.add(2);
		rowB.add("USA");
		rowB.add("Las Vegas");
		rowB.add("Jean");
		rowB.add("83030");
		DropDownColumn dropdownColumnB = new DropDownColumn();
		dropdownColumnB.setValue("1166");
		//dropdownColumn.setDid(15);
		//dropdownColumn.setSid(21);
		dropdownColumn.setDid(628);
		dropdownColumn.setSid(629);
		dropdownColumnB.setDropdownOptions(getDropdownData(dropdownColumn.getDid(),dropdownColumn.getSid()));
		rowB.add(dropdownColumnB);
		rowB.add(112);
		rowB.add("Signal Gift Stores");
		rowB.add("7025551838");
		rowB.add("8489 Strong St.");
		rowB.add(71800);
		rowB.add(null);
		rowB.add("King");
		rowB.add("NV");
		
		List<Object> rowC = new ArrayList<Object>();
		rowC.add(3);
		rowC.add("Australia");
		rowC.add("Melbourne");
		rowC.add("Peter ");
		rowC.add("3004");
		DropDownColumn dropdownColumnC = new DropDownColumn();
		dropdownColumnC.setValue("1611");
		//dropdownColumn.setDid(15);
		//dropdownColumn.setSid(21);
		dropdownColumn.setDid(628);
		dropdownColumn.setSid(629);
		dropdownColumnC.setDropdownOptions(getDropdownData(dropdownColumn.getDid(),dropdownColumn.getSid()));
		rowC.add(dropdownColumnC);
		rowC.add(114);
		rowC.add("Australian Collectors, Co.");
		rowC.add("03 9520 4555");
		rowC.add("636 St Kilda Road");
		rowC.add(117300);
		rowC.add("Level 3");
		rowC.add("Ferguson");
		rowC.add("Victoria");
		
		ArrayList<Object> rowList = new ArrayList<Object>();
		rowList.add(rowA);
		rowList.add(rowB);
		rowList.add(rowC);
		PaginatedTableResponse<Object> resp = new PaginatedTableResponse<Object>();
		resp.setData(rowList);
		resp.setDraw(getDraw(reqParams));
		resp.setRecordsTotal(3);
		logger.debug("getTableDataWithDropDownTest ended");
		return resp;
	}
	
	
	/*	 http://localhost/gdma2/rest/datatable/dropdown/display/628/store/629	 */
	@RequestMapping(value = "/dropdown/display/{did}/store/{sid}")
	public List getDropdownData(@PathVariable("did") Integer displayColumnId, @PathVariable("sid") Integer storeColumnId){
		logger.info("getDropdownData for display column: " + displayColumnId + ", and store column: " + storeColumnId);
		return serviceFacade.getDataModuleService().getDropdownData(displayColumnId, storeColumnId);
	}
	
	/*serverId = 6 tableId= 133 user is active and has userAccess to table with active columns
	 * 
	 * first READ: 			https://localhost/gdma2/rest/datatable/table/83 
	 * then try to add: 	https://localhost/gdma2/rest/datatable/add/table/83	*/
	@RequestMapping(value = "/add/table/{id}", method = RequestMethod.POST )
	public void addTableData(@RequestBody UpdateDataRequest dataRequest){
		logger.info("addTableData: " + dataRequest.getTableId());

		//void methods not orderding no paginated response
		serviceFacade.getDataModuleService().addRecord(dataRequest);
	
	}
	
	/**
	 * 
	 * Once a form has been submitted to the server, Editor expects a JSON object to be returned with the following parameters:
	 * data: the data that represents the new or updated rows in the database
	 * to see more infromation: https://editor.datatables.net/manual/server
	 * @param serverId
	 * @param tableId
	 * @param reqParams
	 * @return
	 */
	@RequestMapping(value = "/update/{serverId}/{tableId}", method = RequestMethod.POST,produces="application/json")
	public @ResponseBody Map<String,String> updateDataTableData(@PathVariable("serverId") Integer serverId, @PathVariable("tableId") Integer tableId, @RequestParam Map<String, String> reqParams){
		logger.info("updateDataTableData");
		UpdateDataRequest dataRequest = extractDataRequest(serverId, tableId, reqParams);
		
		/*---TODO DELETE*/
		List<List<ColumnDataUpdate>> updates = dataRequest.getUpdates();
		for (List<ColumnDataUpdate> list : updates) {
			for (ColumnDataUpdate columnDataUpdate : list) {
				logger.info(columnDataUpdate.getColumnId() + "  "  + columnDataUpdate.getOldColumnValue() + "  " + columnDataUpdate.getNewColumnValue());
			}
			
		}
		/*----TODO DELETE end */
		
		int updatedRecords = serviceFacade.getDataModuleService().updateRecords(dataRequest);
		logger.debug("updatedRecords : " + updatedRecords );
		return reqParams;
	}

	/**
	 * RequestMethod is POST because we have to infer what column is the PK at the server side. 
	 * @param serverId
	 * @param tableId
	 * @param reqParams
	 * @return
	 */
	@RequestMapping(value = "/delete/{serverId}/{tableId}", method = RequestMethod.POST,produces="application/json")
	public @ResponseBody List<Map<String,String>> deleteTableData(@PathVariable("serverId") Integer serverId, @PathVariable("tableId") Integer tableId, @RequestParam List<Map<String, String>> reqParams){
		UpdateDataRequest dataRequest = extractDataRequest(serverId, tableId, reqParams);
		int deletedRecords = serviceFacade.getDataModuleService().deleteRecords(dataRequest);
		logger.debug("deletedRecords: " +deletedRecords);
		return reqParams;
	}
	
	@RequestMapping(value = "/update/table/{id}", method = RequestMethod.PUT)
	public int updateTableData(@RequestBody UpdateDataRequest dataRequest){
		//TODO - change return type if we need to refresh and return latest data after update
		logger.info("update Table Data: " + dataRequest.getTableId() );
		return serviceFacade.getDataModuleService().updateRecords(dataRequest);
	}
	
	
	/*Upload CSV file for proper DB table to INSERT data*/
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Map<String,String> handleUpload(
			@RequestParam("file") MultipartFile file,
			@RequestParam("tableid") Integer tableId
			) throws IOException {

		logger.info("tableId: " + tableId);
		//System.out.println("HandleUpload");
		final Map<String, String> results = new HashMap<String, String>();
		logger.info("file: " + file.getOriginalFilename());

		if (!file.getOriginalFilename().endsWith(".csv")) {
			results.put("error", "Invalid file type");
		} else {
			logger.info("type: " + file.getContentType());
			logger.info("stream: " + file.getInputStream());
			logger.info("size: " + file.getSize());

			/*
			final JSONObject jsonObject = JSONObject.fromObject(bean.getTxtPaginatedRequest());
			final PaginatedRequest paginatedRequest = (PaginatedRequest) JSONObject.toBean(jsonObject, PaginatedRequest.class);
			final Server server = gdmaFacade.getServerDao().get(paginatedRequest.getServerId());
			final Table table = gdmaFacade.getTableDao().get(paginatedRequest.getTableId());
			*/
			
			//final int numRows = bulkImport(tableId, file.getInputStream());
			int bulkImportResult = serviceFacade.getDataModuleService().bulkImport(tableId, file);
			results.put("numRecords", "" + bulkImportResult);
			
		}
//	    if (!file.isEmpty()) {
//	            byte[] bytes = file.getBytes(); // alternatively, file.getInputStream();
//	            // application logic
//	    }
		return results;
	}
	

	//TODO - move this method to a Service class
	/*
	 * This method will receive the rowData from datatable and convert it to UpdateDataRequest
	 * 
	 *  data[11][columns][0][val]:Tseng
		data[11][columns][1][val]:Foon Yui
		
		the key is is "data[11][columns][0][val]" , the Value is "Tseng" 
	 */
	private UpdateDataRequest extractDataRequest(Integer serverId, Integer tableId, Map<String, String> reqParams) {
		UpdateDataRequest dataRequest = new UpdateDataRequest();
		List<ColumnDataUpdate> row = new ArrayList<ColumnDataUpdate>();
		dataRequest.setServerId(serverId);
		dataRequest.setTableId(tableId);
		reqParams.remove("action");
		final List<Column> columnsMetadata = getActiveColumns(tableId);
		columnsMetadata.removeIf(c -> !c.isDisplayed());
		int i = 0,j = 0;
		for (Column columnMetadata : columnsMetadata) {
			for (String key: reqParams.keySet()) {
				if(i == j){
					ColumnDataUpdate newColumn = new ColumnDataUpdate();
					newColumn.setColumnId(columnMetadata.getId());
					newColumn.setNewColumnValue(reqParams.get(key));
					newColumn.setOldColumnValue(reqParams.get(key));
					row.add(newColumn);
				}
				j++;
			}
			j=0;
			i++;
		}
		dataRequest.getUpdates().add(row);
		return dataRequest;
	}
	private UpdateDataRequest extractDataRequest(Integer serverId, Integer tableId, List<Map<String, String>> reqParams) {
		UpdateDataRequest dataRequest = null;
		for (Map<String, String> map : reqParams) {
			dataRequest = extractDataRequest(serverId, tableId, map); 
		}
		return dataRequest;
	}
}
