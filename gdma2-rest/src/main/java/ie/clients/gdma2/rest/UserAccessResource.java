package ie.clients.gdma2.rest;

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
@RequestMapping("rest/access")
public class UserAccessResource extends BaseDataTableResource {
	
	private static Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@RequestMapping("list")
	public List<UserAccess> getAllUserAccess(){
		logger.info("getAllUserAccess");
		return serviceFacade.getMetadataService().getAllUserAccess();
	}
	
	/*double clicking on Table in GDMA1- load complete list of UserAccess for all Users on selected Table 
	 * see:   GdmaAdminAjaxFacade.getAccessListForTable(Long tableId) {*/
	@RequestMapping("table/{id}")
	public PaginatedTableResponse<UserAccess> getAccessListForTable(@PathVariable("id") Integer tableId, @RequestParam Map<String, String> reqParams) {
		logger.info("getAccessListForTable: " + tableId);
		
		String orderByColumn = "id";
		
		switch(getOrderByColumn(reqParams)) {
		case 0: 
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "user.userName";
			break;
		}
		
		logger.debug("orderBy column: " + orderByColumn) ;
		
		PaginatedTableResponse<UserAccess> resp = serviceFacade.getMetadataService().getUserAccessForTable(
				tableId,
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams));

		resp.setDraw(getDraw(reqParams));
		return resp;
		
	}
	
	/*TEST ONLY - we will not SAVE single UserAccess, initial saving is done through Load see getAccessListForTable() above*/
	@RequestMapping(value="save", method=RequestMethod.POST)
	void saveUserAccess(@RequestBody UserAccess userAccess){
		logger.debug("saveUserAccess for table ans user: " + userAccess.getTable().getName() + ", " + userAccess.getUser().getUserName());
		serviceFacade.getMetadataService().saveUserAccess(userAccess);
	}
	
	/*update multiple UserAccess entities*/
	@RequestMapping(value="update", method = RequestMethod.POST)
	public List<UserAccess> updateUserAccess(@RequestBody List<UserAccess> userAccessList){
		logger.debug("updatedUserAccess: list of " + userAccessList.size());
		return serviceFacade.getMetadataService().saveUserAccessList(userAccessList);
	}
	
	/*TEST ONLY - deletion of UserAccess can happen only during table RESYNCH see resolveDeletedTables() in DynamicDAO*/
	@RequestMapping(value="delete/{id}", method = RequestMethod.DELETE)
	void deleteUserAccess(@PathVariable("id") Integer id){
		logger.debug("deleteUserAccess: " + id);
		serviceFacade.getMetadataService().deleteUserAccess(id);
	}
	

	
}
