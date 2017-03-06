package ie.clients.gdma2.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ie.clients.gdma2.domain.User;

@RestController
@RequestMapping("rest/user")
public class UserResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@RequestMapping("list")
	public List<User> getAllUsers(){
		logger.debug("*** getAllUsers(");
		return serviceFacade.getMetadataService().getAllUsers();
	}
}
