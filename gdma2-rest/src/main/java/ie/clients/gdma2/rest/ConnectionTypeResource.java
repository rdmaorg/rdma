package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.ConnectionType;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/connection")
public class ConnectionTypeResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(ConnectionTypeResource.class);
	
	@RequestMapping(value = "list")
	public List<ConnectionType> getAllConnectionTypes(){
		logger.debug("getAllConnectionTypes");
		return serviceFacade.getMetadataService().getAllConnectionTypes();
		
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
