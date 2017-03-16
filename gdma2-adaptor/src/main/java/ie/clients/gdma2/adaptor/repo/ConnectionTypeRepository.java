package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.ConnectionType;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ConnectionTypeRepository extends PagingAndSortingRepository<ConnectionType, Integer>{

	
	//public Iterable<ConnectionType> findAll();;
}
