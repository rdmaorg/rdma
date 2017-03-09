package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.UserAccess;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/access")
public class UserAccessResource extends BaseDataTableResource {
	
	private static Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@RequestMapping("list")
	public List<UserAccess> getAllUserAccess(){
		logger.info("getAllUserAccess");
		return serviceFacade.getMetadataService().getAllUserAccess();
	}
	
	

	
}
