package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Server;
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
@RequestMapping("rest/server")
public class ServerResource extends BaseDataTableResource {

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);

	@RequestMapping("list")
	public List<Server> getallServers(){
		logger.debug("getallServers()");
		return serviceFacade.getMetadataService().getAllServers();
	}
	
	
	/*find all active order by server name*/
	@RequestMapping("/list/active")
	public List<Server> getAllActiveServers(){
		logger.debug("getAllActiveServers()");
		return serviceFacade.getMetadataService().getAllActiveServers();
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
		case 3:// connectionUrl
			orderByColumn = "connectionUrl";
			break;
		case 4:// connectionType.name
			orderByColumn = "connectionType.name";
			break;
		case 5:// by prefix
			orderByColumn = "prefix";
			break;
		case 6:// by active
			orderByColumn = "active";
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
		logger.info("saveServer " + server.getName() );
		serviceFacade.getMetadataService().saveServer(server);
	}


	/*not to be used - server is to be deactivated only. 
	 * Deleting server would lead to deleting children: Tables, UserAccess, Columns...*/
	
	/*
	@RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
	public void deleteServer(@PathVariable("id") Integer id){
		logger.debug("delete server: " + id);
		serviceFacade.getMetadataService().deleteServer(id);
	}
	*/

	@RequestMapping(value = "{id}")
	public Server getServer(@PathVariable("id") Integer serverId ){
		logger.debug("getServer: " + serverId);
		return serviceFacade.getMetadataService().findOne(serverId);
	}

	/*METADATA section*/

	/*  Get all table and columns metadata for server - all in one initial load 
	 will fetch all METADATA for tables and columns from remote DB and store to local GDMA2 DB,
	All tables and columns ACTIVE,	Columns : primary keys determined.
	
		https://localhost/gdma2/rest/server/metadata/6

	Another approach(see TableResource and ColumnResource) is to : fetch Tables from remote DB and save to local,
	 and then table by table, call columns fetch */
	@RequestMapping(value = "/metadata/{id}")
	public List<Table> getMetadata(@PathVariable("id") Integer serverId){
		logger.info("getMetadata:" + serverId);
		return serviceFacade.getMetadataService().getMetadata(serverId);
	}


	/*DATA section*/

	/*Regular user: list all active servers for user after login.
	 * User needs to have userAccess to Active tables on this server to get result: 
	 * 		http://localhost/gdma2/rest/server/data/tables 
	 * 
	 * ...After this call user can calls a list of active tables on one of the servers: 
	 *	 	http://localhost/gdma2/rest/table/data/server/6
	 * ...and then user can call for a column list of table  
	 * 		http://localhost/gdma2/rest/column/data/table/124
	 * ....
	 */
	@RequestMapping(value = "/data/tables")
	public List<Server> getActiveServersContainingActiveTablesForUser(@RequestParam Map<String, String> params){
		return serviceFacade.getDataModuleService().getActiveServers();
	}
}
