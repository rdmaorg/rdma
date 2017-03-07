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

	/*paginated table list for selected server*/
	/*TODO fix URI template*/
	@RequestMapping(value = "/server/{id}/table/list")
	public PaginatedTableResponse<Table> getTablesPaginatedTable(@PathVariable("id") String serverId,
			@RequestParam Map<String,String> params){
		logger.debug("getTablesPaginatedTable()");
		/*order[0][column]:4*/

		Integer srvId = Integer.parseInt(serverId);	//TODO handle empty ServerId int val = Integer.parseInt(reqParams.get(QUERY_PARAM_DRAW) == null... 

		String orderByColumn = "id";
		switch(getOrderByColumn(params)){
		case 0: //by id
			orderByColumn = "id";
			break;
		case 1: //by name
			orderByColumn = "name";
			break;
		case 2: //by active
			orderByColumn = "active";
			break;
		case 3: 
			orderByColumn = "server.name";
			break;
		}
		
		logger.debug("orderBy column: " + orderByColumn) ;

		PaginatedTableResponse<Table> resp = serviceFacade.getMetadataService().
				getTablesForServer(srvId, getSearchValue(params), orderByColumn, getOrderByDirection(params),getStartIndex(params), getLength(params));

		resp.setDraw(getDraw(params));
		return resp;

	}
	
	/*List of ACTIVE tables for selected server*/
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


}
