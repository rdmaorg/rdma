package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.AuditRecord;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

public interface LoggingService {

	
	public PaginatedTableResponse<AuditRecord> getPaginatedAuditLogs(
			String searchValue, String orderByColumn, String orderByDirection,int startIndex, int length);
	/*	ACTIVITY LOGGING - Logging that is not related to data manipulation, e.g, logins, changing configurations etc*/
	
	public void log(String clientIP, String activity, String performedBy);
	public void log(String clientIP, String activity);
	public void log(String activity);
}
