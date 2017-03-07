package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.Server;

public interface ServerRepository extends PagingAndSortingRepository<Server, Integer> {
	
	@Query("select count(s) from Server s where upper(s.name) like ?1 or upper(s.username) like ?1 or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public long getCountMatching(String matching);

	@Query("select s from Server s where upper(s.name) like ?1 or upper(s.username) like ?1 or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public List<Server> getMatchingServers(String matching, Pageable pageable);

	/* TODO
	ServerDao
	public Server getByColumn(Long columnId);
    public List<Server> getServerTableList(String username);
    public List<Server> getServerTableColumnList();
    public List<Server> getServerTableColumnList(Long serverId, Long tableId);
	public List<Server> getServerTableColumnListForDDDropdown();
	*/

}
