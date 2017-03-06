package ie.clients.gdma2.rest;

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

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

@RestController
@RequestMapping("rest/server")
public class ServerResource extends BaseDataTableResource {

	private static Logger logger = LoggerFactory.getLogger(ServerResource.class);
	
	@RequestMapping("list")
	public List<Server> getallServers(){
		return serviceFacade.getMetadataService().getAllServers();
	}
	
	@RequestMapping("table")
	public PaginatedTableResponse<Server> getServerPaginatedTable(@RequestParam Map<String, String> params) {
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
		}

		PaginatedTableResponse<Server> resp = serviceFacade.getMetadataService().getServers(getSearchValue(params), orderByColumn, getOrderByDirection(params),
				getStartIndex(params), getLength(params));

		resp.setDraw(getDraw(params));

		return resp;

	}
	
	@RequestMapping(value="save", method = RequestMethod.POST)
	public void saveServer(@RequestBody Server server){
		serviceFacade.getMetadataService().saveServer(server);
	}

	
	@RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
	public void deleteServer(@PathVariable("id") Integer id){
		serviceFacade.getMetadataService().deleteServer(id);
	}
	
	
}
