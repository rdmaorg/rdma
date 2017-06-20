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

import ie.clients.gdma2.domain.ConnectionType;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

@RestController
@RequestMapping("rest/connection")
public class ConnectionTypeResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(ConnectionTypeResource.class);

	@RequestMapping(value = "list")
	public List<ConnectionType> getAllConnectionTypes(){
		logger.debug("getAllConnectionTypes");
		return serviceFacade.getMetadataService().getAllConnectionTypes();

	}

	@RequestMapping(value="table")
	public PaginatedTableResponse<ConnectionType> getConnectionTypeTable(@RequestParam  Map<String, String> reqParams){
		logger.debug("getConnectionTypeTable");
		
		
		String orderByColumn = "id";

		switch (getOrderByColumn(reqParams)){
		case 0: //by id
			orderByColumn = "id"; 
			break;
		case 1: //by name
			orderByColumn = "name";
			break;
		case 2: //by SQL get table query
			orderByColumn = "connectionClass";
			break;
		case 3: //by connection class
			orderByColumn = "sqlGetTables";
			break;
		}

		logger.debug("orderByColumn: " + orderByColumn);

		PaginatedTableResponse<ConnectionType> resp = serviceFacade.getMetadataService().getConnectionTypeTable(
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams)
				);

		resp.setDraw(getDraw(reqParams));
		return resp;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public void saveConnectionType(@RequestBody ConnectionType connectionType){
		logger.debug("saveConnectionType: " + connectionType.getName());
		serviceFacade.getMetadataService().saveConnectionType(connectionType);
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
	public void deleteConnectionType(@PathVariable("id") Integer id){
		serviceFacade.getMetadataService().deleteConnectionType(id);
	}

	
}
