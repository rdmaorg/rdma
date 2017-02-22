package ie.clients.gdma2.rest;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

@RestController
@RequestMapping("rest/server")
public class ServerResource extends BaseDataTableResource {
	@RequestMapping("search")
	public PaginatedTableResponse<Server> searchPlanSwitchOrders(@RequestParam Map<String, String> params) {
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
}
