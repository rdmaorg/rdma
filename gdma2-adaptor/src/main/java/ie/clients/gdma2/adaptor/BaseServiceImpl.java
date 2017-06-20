package ie.clients.gdma2.adaptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
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
