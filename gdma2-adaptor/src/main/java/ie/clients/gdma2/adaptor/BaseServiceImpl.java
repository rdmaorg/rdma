package ie.clients.gdma2.adaptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.avnet.cs.commons.utils.StringUtil;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.AuditActivity;
import ie.clients.gdma2.domain.ui.PaginatedEditableTableResponse;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.BusinessException;
import ie.clients.gdma2.spi.ServiceException;
import ie.clients.gdma2.spi.interfaces.UserContextProvider;
import ie.clients.gdma2.util.DynamicDAO;

public abstract class BaseServiceImpl {

	@Autowired
	protected RepositoryManager repositoryManager;

	@Autowired
	protected DynamicDAO dynamicDAO;

	@Autowired
	protected UserContextProvider userContextProvider;
	
	@Autowired(required=false)
	private HttpServletRequest req;

	protected void logActivity(String activity) {
		logActivity("", activity);
	}

	protected void logActivity(String clientIP, String activity) {
		logActivity(clientIP, activity, "");
	}

	protected void logActivity(String clientIP, String activity, String performedBy) {
		final String UNKNOWN_LOCATION = "UNKNOWN LOCATION";
		final int MAX_ACTIVITY_LENGTH = 2030;

		if (StringUtils.isNotBlank(activity)) {
			AuditActivity a = new AuditActivity();
			a.setActivityOn(new Date());
			a.setActivityBy(StringUtils.isBlank(performedBy) ? userContextProvider.getLoggedInUserName() : performedBy);
			a.setActivityFrom(StringUtils.isBlank(clientIP) ? req!=null ? req.getRemoteAddr(): UNKNOWN_LOCATION : clientIP);
			a.setActivity(StringUtil.abbreviateString(activity, MAX_ACTIVITY_LENGTH));
			
			
			repositoryManager.getAuditActivityRepository().save(a);
		}
	}

	protected PageRequest getPagingRequest(String orderBy, String orderDirection, int startIndex, int length,
			long totalRecords) {

		List<Order> order = new ArrayList<Order>();

		order.add(new Order(Direction.fromString(orderDirection), orderBy));

		Sort sort = new Sort(order);

		int page = 0;
		int size = (int) totalRecords;

		if (length >= 1) {
			page = startIndex / length;
			size = length;
		}

		return new PageRequest(page, size, sort);
	}

	protected <T> PaginatedTableResponse<T> getPaginatedTableResponse(List<T> data, long totalRecords,
			long filteredRecords) {
		PaginatedTableResponse<T> resp = new PaginatedTableResponse<T>();
		resp.setData(data);
		resp.setRecordsTotal(totalRecords);
		resp.setRecordsFiltered(filteredRecords);

		return resp;
	}
	
	protected <T,K,V> PaginatedEditableTableResponse<T,K,V> getPaginatedEditableTableResponse(List<T> data, long totalRecords,
			long filteredRecords, Map<K,List<V>> optionList) {
		PaginatedEditableTableResponse<T,K,V> resp = new PaginatedEditableTableResponse<T,K,V>();
		resp.setData(data);
		resp.setRecordsTotal(totalRecords);
		resp.setRecordsFiltered(filteredRecords);
		resp.setOptions(optionList);

		return resp;
	}

	@SuppressWarnings("all")
	protected void throwException(Throwable e) {
		if (e instanceof BusinessException) {
			throw (BusinessException) e;
		}

		if (e instanceof NullPointerException) {
			throw new RuntimeException(e.getMessage(), e);
		}

		if (e.getCause() != null && e.getCause() instanceof IOException) {
			throw new BusinessException("Internal service communication error. Please try again later");
		}

		String message = e.getMessage() == null ? "" : e.getMessage();
		String faultCode = "";
		String faultDesc = "";
		try {
			Method getFaultInfo = e.getClass().getMethod("getFaultInfo", null);
			Object faultInfo = getFaultInfo.invoke(e, null);

			Method getSOAFaultCode = faultInfo.getClass().getMethod("getSOAFaultCode", null);
			Method getFaultDescription = faultInfo.getClass().getMethod("getFaultDescription", null);

			faultCode = (String) getSOAFaultCode.invoke(faultInfo, null);
			faultDesc = (String) getFaultDescription.invoke(faultInfo, null);

		} catch (Exception e1) {
		}

		throw new ServiceException(message, faultCode, faultDesc);
	}
}
