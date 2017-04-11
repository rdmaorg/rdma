package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.UserAccess;
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
@RequestMapping("rest/server")
public class ServerResource extends BaseDataTableResource {

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);
	
	@RequestMapping("list")
	public List<Server> getallServers(){
		logger.debug("getallServers()");
		return serviceFacade.getMetadataService().getAllServers();
	}
	
	@RequestMapping("table")
	public PaginatedTableResponse<Server> getServerPaginatedTable(@RequestParam Map<String, String> params) {
		logger.debug("getServerPaginatedTable");
		
		String orderByColumn = "id";

		switch (getOrderByColumn(params)) {
		case 0:// by id
			orderByColumn = "id";
			break;
		case 1:// by name
			orderByColumn = "name";
			break;
		case 2:// by username
			orderByColumn = "username";
			break;
		case 3:// by password
			orderByColumn = "password";
			break;
		case 4:// by connectionUrl
			orderByColumn = "connectionUrl";
			break;
		case 5:// by connectionType
			orderByColumn = "connectionType.name";
			break;
		case 6:// by prefix
			orderByColumn = "prefix";
			break;
		case 7:// by active
			orderByColumn = "active";
			break;
		case 8: //alias
			orderByColumn = "alias";
			break;
		}

		logger.debug("orderByColumn: " + orderByColumn);
		
		PaginatedTableResponse<Server> resp = serviceFacade.getMetadataService().getServers(getSearchValue(params), orderByColumn, getOrderByDirection(params),
				getStartIndex(params), getLength(params));

		resp.setDraw(getDraw(params));

		return resp;

	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	public void saveServer(@RequestBody Server server){
		logger.debug("saveServer " + server.getName() );
		serviceFacade.getMetadataService().saveServer(server);
	}

	
	@RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
	public void deleteServer(@PathVariable("id") Integer id){
		logger.debug("delete server: " + id);
		serviceFacade.getMetadataService().deleteServer(id);
	}
	
	@RequestMapping(value = "{id}")
	public Server getServer(@PathVariable("id") Integer serverId ){
		logger.debug("getServer: " + serverId);
		return serviceFacade.getMetadataService().findOne(serverId);
	}
	
	/*TEST ONLY : for initial table load and column metadata load for each table - all in one call 
	 * first Time Tables and Columns creating for selected servers - NO SYNCH NO ACTIVE! */
	@RequestMapping(value = "/metadata/{id}")
	public Server getTableMetadataForServerTestOnly(@PathVariable("id") Integer serverId){
		logger.info("getTableMetadataForServer:" + serverId);
		return serviceFacade.getMetadataService().getTablesMetadataForServerTestOnly(serverId);
	}

	/*list all active servers and active tables for registered user*/
	@RequestMapping(value = "/data/tables")
	public List<Server> getActiveTablesForActiveServer(@RequestParam Map<String, String> params){
		return serviceFacade.getDataModuleService().getServerTableList();
	}
}
