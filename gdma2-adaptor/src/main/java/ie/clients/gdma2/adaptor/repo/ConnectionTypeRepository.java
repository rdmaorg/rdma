package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.ConnectionType;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ConnectionTypeRepository extends PagingAndSortingRepository<ConnectionType, Integer>{

	
	@Query("select count(ct) from ConnectionType ct where upper(ct.name) like ?1 or upper(ct.sqlGetTables) like ?1 or upper(ct.connectionClass) like ?1")
	public long getCountMatching(String match);

	@Query("select ct from ConnectionType ct where upper(ct.name) like ?1 or upper(ct.sqlGetTables) like ?1 or upper(ct.connectionClass) like ?1")
	public List<ConnectionType> getMatchingConnectionTypes(String match, Pageable pageable);

	
	
}
