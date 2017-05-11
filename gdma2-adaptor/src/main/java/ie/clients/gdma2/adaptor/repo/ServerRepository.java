package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Server;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ServerRepository extends PagingAndSortingRepository<Server, Integer> {

	
	/*find all active */
	@Query("select s from Server s where s.active = TRUE")
	public List<Server> findActiveServers();
	
	@Query("select count(s) from Server s where upper(s.name) like ?1 or upper(s.username) like ?1 "
			+ " or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public long getCountMatching(String matching);

	@Query("select s from Server s where upper(s.name) like ?1 or upper(s.username) like ?1"
			+ " or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public List<Server> getMatchingServers(String matching, Pageable pageable);

	
	
	/*
	@Query("SELECT t FROM TopupType t join t.topupTypeCategoryPermissions c WHERE (c.enabled = 'true' and LOWER(c.topupCategory.name) = LOWER(:categoryName))")
	public List<TopupType> findByCategoryPermissionsName(@Param("categoryName") String categoryName);
	*/
	
	/*join from child to parent entities, don't use fetch because of error: 'owner of the fetched association was not present in the select list'*/
//	@Query("select s from UserAccess ua "
//			+ "	inner join ua.user us "
//			+ " inner join ua.table t "
//			+ " inner join t.server s "
//			+ " where UPPER(us.userName) = UPPER(:userName) "
//			+ " and ua.allowDisplay = TRUE "
//			+ " and t.active = TRUE and s.active = TRUE")
	@Query("select distinct ua.table.server from UserAccess ua "
			+ " where UPPER(ua.user.userName) = UPPER(:userName) "
			+ " and ua.allowDisplay = TRUE "
			+ " and ua.table.active = TRUE "
			+ " and ua.table.server.active = TRUE ")
	public List<Server> activeServersWithActiveTablesForUser(@Param("userName") String userName);
	
	
	/*part of getDropDown logic - get server by column*/
	@Query("select distinct c.table.server from Column c "
			+"  where c.table.active = TRUE "
			+ " and c.table.server.active = TRUE"
			+ " and c.id = ?1")
	public Server activeServerWithActiveTableForColumn(Integer columnId);
	
}
