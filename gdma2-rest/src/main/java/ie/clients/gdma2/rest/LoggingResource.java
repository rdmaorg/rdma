package ie.clients.gdma2.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ie.clients.gdma2.domain.AuditActivity;
import ie.clients.gdma2.domain.AuditRecord;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

@RestController
@RequestMapping("rest/logs")
public class LoggingResource extends BaseDataTableResource{

	private static Logger logger = LoggerFactory.getLogger(LoggingResource.class);


	@RequestMapping(value="audit")
	public PaginatedTableResponse<AuditRecord> getAuditLogs(@RequestParam  Map<String, String> reqParams){
		logger.debug("getAuditLogs");
		
		String orderByColumn = "id";
		switch (getOrderByColumn(reqParams)){
		case 0:
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "auditHeader.tableID.name";
			break;
		case 2:
			orderByColumn = "auditHeader.type";
			break;
		case 3:
			orderByColumn = "auditHeader.modifiedBy";
			break;
		case 4:
			orderByColumn = "auditHeader.modifiedOn";
			break;
		case 5:
			orderByColumn = "columnID.name";
			break;
		case 6:
			orderByColumn = "oldValue";
			break;
		case 7:
			orderByColumn = "newValue";
			break;
		case 8:
			orderByColumn = "auditHeader.whereClause";
			break;
		default:
			orderByColumn = "id";
			break;
		}

		logger.debug("orderByColumn: " + orderByColumn);

		PaginatedTableResponse<AuditRecord> resp = serviceFacade.getLoggingService().getPaginatedAuditLogs(
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams)
				);

		resp.setDraw(getDraw(reqParams));
		return resp;
	}
	
	@RequestMapping(value="activity")
	public PaginatedTableResponse<AuditActivity> getActivityLogs(@RequestParam  Map<String, String> reqParams){
		logger.debug("getActivityLogs");
		
		String orderByColumn = "id";
		switch (getOrderByColumn(reqParams)){
		case 0:
			orderByColumn = "id";
			break;
		case 1:
			orderByColumn = "activity";
			break;
		case 2:
			orderByColumn = "activityFrom";
			break;
		case 3:
			orderByColumn = "activityBy";
			break;
		case 4:
			orderByColumn = "activityOn";
			break;
		default:
			orderByColumn = "id";
			break;
		}

		logger.debug("orderByColumn: " + orderByColumn);

		PaginatedTableResponse<AuditActivity> resp = serviceFacade.getLoggingService().getPaginatedActivityLogs(
				getSearchValue(reqParams),
				orderByColumn,
				getOrderByDirection(reqParams),
				getStartIndex(reqParams),
				getLength(reqParams)
				);

		resp.setDraw(getDraw(reqParams));
		return resp;
	}
}
