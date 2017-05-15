package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.UpdateDataRequest;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
@Controller
@RequestMapping("rest/datatable")
*/
@RestController
@RequestMapping("rest/datatable")
public class DataTableResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(DataTableResource.class);
	
	/*paginated active columns DATA for : Active server, active table Table, logged in user with UserAccess.allowDisplay = true  */
	/* https://localhost/gdma2/rest/column/data/read/table/133?length=20  */
	@RequestMapping("/table/{id}")
	public PaginatedTableResponse<Column> tableData(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		
		logger.debug("tableData: " + tableId);
		
		List<Object> filtersTODO = new ArrayList<Object>();
		//  order[0][column]:1
		//String orderByColumn = "id";
		
		//values are not 0,1,2... but 0, column1 PK, column2 PK like : 0,12,14,478
		int orderByColumnID = getOrderByColumn(reqParams);
		
		/*
		String returnJson = "{" + "\"data\":" + convertToAjaxResponse(systemConfigurationProvider.findAll()) + ","
				+ "\"options\": []," + "\"files\": []" + "}";
		return returnJson;
		*/
		
		logger.info("orderByColumn, column PK: " + orderByColumnID);
		PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getTableData(
				tableId, 
				filtersTODO,
				//getSearchValue(reqParams), //NO SEARCH HERE
				orderByColumnID,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));
		
		resp.setDraw(getDraw(reqParams));
		logger.debug("getColumnsPaginatedTable ended");
		return resp;
		
		
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
	
	
	
	@RequestMapping(value = "/update/table/{id}", method = RequestMethod.POST)
	public int updateTableData(@RequestBody UpdateDataRequest dataRequest){
		//TODO - change return type if we need to refresh and return latest data after update
		logger.info("update Table Data: " + dataRequest.getTableId() );
		return serviceFacade.getDataModuleService().updateRecords(dataRequest);
	}
	
	@RequestMapping(value = "/delete/table/{id}", method = RequestMethod.DELETE)
	public int deleteTableData(@RequestBody UpdateDataRequest dataRequest ){
		logger.info("delete Table ata, table:  " + dataRequest.getTableId());
		return serviceFacade.getDataModuleService().deleteRecords(dataRequest);
	}
	
	

}
