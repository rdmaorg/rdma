package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Table;
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
@RequestMapping("rest/table/")
public class TableResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(TableResource.class);


	@RequestMapping(value="list", method = RequestMethod.GET)
	public List<Table> getAllTables(){
		logger.debug("getAllTables()");
		return serviceFacade.getMetadataService().getAllTables();
	}

	/*METADATA - fetch Tables from remote DB and save to local, return ALL saved Tables to UI (not just ACTIve), NO pagination
	 * http://localhost/gdma2/rest/table/6/metadata?length=100 */
	@RequestMapping(value = "/{id}/metadata")
	public List<Table> getRemoteServerTableMetadata(@PathVariable("id") String serverId,
			@RequestParam Map<String,String> params){

		logger.info("getRemoteServerTableMetadata, serverId: " + serverId);
		Integer srvId = Integer.parseInt(serverId); 
		return serviceFacade.getMetadataService().getRemoteServerTableMetadata(srvId);
	}


	/*ACTIVE table list for selected server - paginated from local DB
	 * 		 http://localhost/gdma2/rest/table/server/6  	*/
	/*		 https://localhost/gdma2/rest/table/server/6?order[0][column]=1&search[value]=ord      */
	@RequestMapping(value = "/server/{id}")
	public PaginatedTableResponse<Table> getActiveLocalTablesForServer(@PathVariable("id") String serverId,
			@RequestParam Map<String,String> params){
		logger.info("getActiveLocalTablesForServer: " + serverId);

		Integer srvId = Integer.parseInt(serverId);	//TODO handle empty ServerId int val = Integer.parseInt(reqParams.get(QUERY_PARAM_DRAW) == null... 

		String orderByColumn = "id";
		switch(getOrderByColumn(params)){
		case 0: //by id
			orderByColumn = "id";
			break;
		case 1: //by name
			orderByColumn = "name";
			break;
		case 2: //by alias
			orderByColumn = "alias";
			break;
		case 3:  //by active
			orderByColumn = "active";
			break;
		}

		logger.info("orderBy column: " + orderByColumn) ;

		PaginatedTableResponse<Table> resp = serviceFacade.getMetadataService().getActiveLocalTablesForServer(srvId,
				getSearchValue(params), orderByColumn, getOrderByDirection(params),	getStartIndex(params), getLength(params));

		resp.setDraw(getDraw(params));
		return resp;

	}


	/*Get active tables for server*/
	@RequestMapping(value = "/server/{id}/active",method = RequestMethod.GET)
	public List<Table> findByServerIdAndActiveTrue(@PathVariable("id") Integer serverId){
		logger.debug("*** findByServerIdAndActiveTrue(), serverId: " +  serverId);
		return serviceFacade.getMetadataService().findByServerIdAndActiveTrue(serverId);
	}

	@RequestMapping(value="save", method = RequestMethod.POST)
	public void saveTable(@RequestBody Table table){
		logger.debug("*** saveTable: " +  table.getName());
		serviceFacade.getMetadataService().saveTable(table);
	}


	@RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
	public void deleteTable(@PathVariable("id") Integer tableId){
		logger.debug("*** deleteTable with Id: " +  tableId);
		serviceFacade.getMetadataService().deleteTable(tableId);
	}


	/*DATA module*/
	
	/*	http://localhost/gdma2/rest/table/data/server/6	*/
	@RequestMapping(value = "/data/server/{id}")
	public List<Table> activeTablesOnActiveServerForUser(@PathVariable("id") Integer serverId){
		logger.info("activeTablesOnActiveServerForUser, serverId: " + serverId);
		return serviceFacade.getDataModuleService().getActiveTables(serverId);
	}

}
