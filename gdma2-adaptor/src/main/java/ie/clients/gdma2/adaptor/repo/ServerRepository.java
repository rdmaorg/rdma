package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.Server;

public interface ServerRepository extends PagingAndSortingRepository<Server, Integer> {
	
	@Query("select count(s) from Server s where upper(l.name) like ?1 or upper(s.username) like ?1 or upper(s.connection_url) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public long getCountMatching(String matching);

	@Query("select s from Server s where upper(l.name) like ?1 or upper(s.username) like ?1 or upper(s.connection_url) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public List<Server> getMatchingServers(String matching, Pageable pageable);

}
