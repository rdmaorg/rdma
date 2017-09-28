package ie.clients.gdma2.adaptor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ie.clients.gdma2.domain.AuditActivity;
import ie.clients.gdma2.domain.AuditRecord;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.interfaces.LoggingService;

@Service
public class LoggingServiceImpl extends BaseServiceImpl implements LoggingService {

	private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);

	@Override
	public PaginatedTableResponse<AuditRecord> getPaginatedAuditLogs(String searchValue, String orderByColumn,
			String orderByDirection, int startIndex, int length) {
		long total = repositoryManager.getAuditRecordRepository().count();
		long filtered = 0;
		List<AuditRecord> auditRecords;
		if(StringUtils.isBlank(searchValue)){
			filtered = total;
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			Page<AuditRecord> auditRecordPages = repositoryManager.getAuditRecordRepository().findAll(pagingRequest);
			auditRecords = auditRecordPages.getContent();
		} else {
			String match = "%" +searchValue.trim().toUpperCase() + "%";
			filtered = repositoryManager.getAuditRecordRepository().getCountMatching(match);
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			auditRecords =  repositoryManager.getAuditRecordRepository().getMatchingAuditRecords(match, pagingRequest);
		}
		
		return getPaginatedTableResponse(auditRecords != null ? auditRecords : new ArrayList<AuditRecord>(), total, filtered);
	}
	
	@Override
	public PaginatedTableResponse<AuditActivity> getPaginatedActivityLogs(String searchValue, String orderByColumn,
			String orderByDirection, int startIndex, int length) {
		long total = repositoryManager.getAuditActivityRepository().count();
		long filtered = 0;
		List<AuditActivity> activityLogs = null;
		if(StringUtils.isBlank(searchValue)){
			filtered = total;
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			Page<AuditActivity> activityLogPages = repositoryManager.getAuditActivityRepository().findAll(pagingRequest);
			activityLogs = activityLogPages.getContent();
		} else {
			String match = "%" +searchValue.trim().toUpperCase() + "%";
			filtered = repositoryManager.getAuditActivityRepository().getCountMatching(match);
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			activityLogs =  repositoryManager.getAuditActivityRepository().getMatchingAuditActivity(match, pagingRequest);
		}
		return getPaginatedTableResponse(activityLogs != null ? activityLogs : new ArrayList<AuditActivity>(), total, filtered);
	}

	@Transactional
	@Override
	public void log(String activity) {
		super.logActivity(activity);
	}

	@Transactional
	@Override
	public void log(String clientIP, String activity) {
		super.logActivity(clientIP, activity);
	}

	@Transactional
	@Override
	public void log(String clientIP, String activity, String performedBy) {
		super.logActivity(clientIP, activity, performedBy);
	}



}
