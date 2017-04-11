package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.UserAccess;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ServerRepository extends PagingAndSortingRepository<Server, Integer> {
	
	@Query("select count(s) from Server s where upper(s.name) like ?1 or upper(s.username) like ?1 "
			+ " or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 or upper(s.alias) like ?1")
	public long getCountMatching(String matching);

	@Query("select s from Server s where upper(s.name) like ?1 or upper(s.username) like ?1"
			+ " or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 or upper(s.alias) like ?1")
	public List<Server> getMatchingServers(String matching, Pageable pageable);

	/* TODO
	ServerDao
	public Server getByColumn(Long columnId);
    public List<Server> getServerTableList(String username);
    public List<Server> getServerTableColumnList();
    public List<Server> getServerTableColumnList(Long serverId, Long tableId);
	public List<Server> getServerTableColumnListForDDDropdown();
	*/

	
	/*
	@Query("SELECT t FROM TopupType t join t.topupTypeCategoryPermissions c WHERE (c.enabled = 'true' and LOWER(c.topupCategory.name) = LOWER(:categoryName))")
	public List<TopupType> findByCategoryPermissionsName(@Param("categoryName") String categoryName);
	*/
	
	/*join from child to parent entities, don't use fetch because of error: 'owner of the fetched association was not present in the select list'*/
	@Query("select s from UserAccess ua "
			+ "	inner join ua.user us "
			+ " inner join ua.table t "
			+ " inner join t.server s "
			+ " where UPPER(us.userName) = UPPER(:userName) "
			+ " and ua.allowDisplay = TRUE "
			+ " and t.active = TRUE and s.active = TRUE")
	public List<Server> activeServersWithActiveTablesForUser(@Param("userName") String userName);
	
	
}
