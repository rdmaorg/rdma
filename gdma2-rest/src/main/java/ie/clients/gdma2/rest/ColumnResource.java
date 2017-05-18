package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

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

@RestController
@RequestMapping(value = "/rest/column")
public class ColumnResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);

	@RequestMapping(value = "list")
	public List<Column> getAllColumns(){
		logger.debug("getAllColumns()");
		return serviceFacade.getMetadataService().getAllColumns();
	}
	
	
	/*METADATA - fetch Columns from remote DB Table and save to local, then
	 * return ALL saved columns to UI for that table (not just ACTIVE), NO pagination
	 * http://localhost/gdma2/rest/column/metadata/table/11?length=100 
	 * (precondition is that previous table fetch was done)*/
	@RequestMapping(value = "/metadata/table/{id}")
	public boolean getRemoteTableColumnsMetadata(@PathVariable("id") String tableId,
			@RequestParam Map<String,String> params){

		logger.info("getRemoteTableColumnsMetadata, tableId: " + tableId);
		Integer tabId = Integer.parseInt(tableId); 
		return serviceFacade.getMetadataService().getRemoteTableColumnsMetadata(tabId);
	}

	
	/*ACTIVE column list for selected ACTIVE table - Admin module : from local DB */
	/* 		 http://localhost/gdma2/rest/column/table/11  	*/
	/*		 http://localhost/gdma2/rest/column/table/11?order[0][column]=1&search[value]=addr      */
	@RequestMapping("/table/{id}")
	PaginatedTableResponse<Column> getActiveLocalColumnsForTable(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		logger.debug("getColumnsPaginatedTable");
		
		String orderByColumn = "id";
		
		switch (getOrderByColumn(reqParams)) {
		case 0:
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "name";
			break;
		case 2:
			orderByColumn = "alias";
			break;
		//case 3: 
			//orderByColumn = "columnType";
			//break;
		case 3: 
			orderByColumn = "columnTypeString";
			break;
		case 4:
			orderByColumn = "primarykey";
			break;
		case 5:
			orderByColumn = "displayed";
			break;
		case 6:
			orderByColumn = "allowInsert";
			break;
		case 7:
			orderByColumn = "allowUpdate";
			break;
		case 8:
			orderByColumn = "nullable";
			break;
		case 9: //order by column0_.dd_lookup_display asc limit ?    
			//if this is to be used at all consider : column0_.dd_lookup_display.name in columns.js and here
			orderByColumn = "dropDownColumnDisplay";  
			break;
		case 10:
			orderByColumn = "dropDownColumnStore";
			break;
		case 11:
			orderByColumn = "special";
			break;
		case 12:
			orderByColumn = "minWidth";
			break;
		case 13:
			orderByColumn = "maxWidth";
			break;
		case 14:
			orderByColumn = "columnSize";
			break;
		//case 15:
			//	orderByColumn = "orderby";
			//	break;	
		}
		
		logger.info("orderByColumn: " + orderByColumn);
		PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getActiveLocalColumnsForTable(
				tableId,
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));
		
		resp.setDraw(getDraw(reqParams));
		logger.debug("getColumnsPaginatedTable ended");
		return resp;
	}
	
	/*Get active columns for table order by column alias - use for DD display and DD store, after selecting active table from server*/
	@RequestMapping(value = "table/{id}/active")
	public List<Column> findByTableIdAndActiveTrue(@PathVariable("id") Integer tableId){
		logger.info("findByTableIdAndActiveTrue, tableId: " + tableId);
		return serviceFacade.getMetadataService().findByTableIdAndActiveTrue(tableId);
	}
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public List<Column> saveColumns(@RequestBody List<Column> columnList){
		logger.debug("saveColumns");
		return serviceFacade.getMetadataService().saveColumns(columnList);
		
	}
	
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public void deleteColumn(@PathVariable("id") Integer columnId){
		logger.debug("deleteColumn " + columnId);
		serviceFacade.getMetadataService().deleteColumn(columnId);
	}
	
	
	
	/*DATA MODULE*/
	
	
	
	/*paginated active columns DATA for : Active server, active table Table, logged in user with UserAccess.allowDisplay = true  */
	/* https://localhost/gdma2/rest/column/data/read/table/133?length=20  */
	@RequestMapping("/data/read/table/{id}")
	PaginatedTableResponse<Column> getColumnData(@PathVariable("id") Integer tableId,
			@RequestParam Map<String, String> reqParams){
		logger.debug("getColumnsPaginatedTable");
		
		//  order[0][column]:1
		//String orderByColumn = "id";
		
		//values are not 0,1,2... but 0, column1 PK, column2 PK like : 0,12,14,478
		int orderByColumnID = getOrderByColumn(reqParams);
		
		logger.info("orderByColumn, column PK: " + orderByColumnID);
		PaginatedTableResponse<Column> resp = serviceFacade.getMetadataService().getColumnData(
				tableId,
				getSearchValue(reqParams),
				orderByColumnID,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));
		
		resp.setDraw(getDraw(reqParams));
		logger.debug("getColumnsPaginatedTable ended");
		return resp;
	}
	
	/*	DISPLAY PURPOSE http://localhost/gdma2/rest/column/data/dropdown/display/608/store/609	
	 * FOR SAVING DD columns from UI just call existing : saveColumns(@RequestBody List<Column> columnList){ */
	@RequestMapping(value = "/data/dropdown/display/{did}/store/{sid}")
	public List getDropdownData(@PathVariable("did") Integer displayColumnId, @PathVariable("sid") Integer storeColumnId){
		logger.info("getDropdownData for display column: " + displayColumnId + ", and store column: " + storeColumnId);
		return serviceFacade.getDataModuleService().getDropdownData(displayColumnId, storeColumnId);
	}
	
	/*serverId = 6 tableId= 43 user is active and has userAccess to table with active columns
	 * 
	 * first READ: 			https://localhost/gdma2/rest/column/data/read/table/83 
	 * then try to add: 	https://localhost/gdma2/rest/column/data/add/table/83	*/
	@RequestMapping("/data/add/table/{id}")
	public void addColumnData(@PathVariable("id") Integer tableId, @RequestParam Map<String, String> reqParams){
		logger.info("addColumnData for table: " + tableId);
		
		//void methods not orderding no paginated response
		serviceFacade.getDataModuleService().addRecord(tableId);
	}
	
	/*	https://localhost/gdma2/rest/column/data/update/table/136 */
	@RequestMapping("/data/update/table/{id}")
	public int updateColumnData(@PathVariable("id") Integer tableId, @RequestParam Map<String,String> reqParams){
		//TODO - change return type if we need to refresh and return latest data after update
		logger.info("updateColumnData, table: " + tableId);
		return serviceFacade.getDataModuleService().updateRecords(tableId);
	}
	
	@RequestMapping("/data/delete/table/{id}")
	public int deleteColumnData(@PathVariable("id") Integer tableId, @RequestParam Map<String, String> reqParams){
		logger.info("deleteColumnData, table:  " + tableId);
		return serviceFacade.getDataModuleService().deleteRecords(tableId);
	}
}
