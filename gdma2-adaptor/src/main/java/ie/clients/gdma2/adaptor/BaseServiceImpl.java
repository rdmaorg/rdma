package ie.clients.gdma2.adaptor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import ie.clients.gdma2.adaptor.repo.RepositoryManager;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.interfaces.UserContextProvider;

public abstract class BaseServiceImpl {

	@Autowired
	protected RepositoryManager repositoryManager;
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

}
