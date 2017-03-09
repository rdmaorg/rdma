package ie.clients.gdma2.rest;

import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	

	
}
