package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserAccessRepository extends PagingAndSortingRepository<UserAccess, Integer> {
		
	/*count all UserAccess entries for table*/
	@Query("select count(ua) from UserAccess ua where ua.table.id = ?1")
	public long countUserAccessForTable(Integer id);

	@Query("select ua from UserAccess ua where ua.table.id = ?1")
	public List<UserAccess> findPaginatedUserAccessByTable(Integer tableId, Pageable pageable);
		
	@Query("select count(ua) from UserAccess ua where (upper(ua.user.userName) like ?1 or upper(ua.user.firstName) like ?1) and ua.table.id = ?2")
	public long getCountMatchingByTable(String matching, Integer tableId);

	@Query("select ua from UserAccess ua where (upper(ua.user.userName) like ?1 or upper(ua.user.firstName) like ?1) and ua.table.id = ?2")
	public List<UserAccess> getMatchingUserAccessesByTable(String match, Integer tableId, Pageable pageable);
	
	@Query("select ua from UserAccess ua where ua.table.id = ?1")
	public List<UserAccess> findByTableId(int tableId);
	
	
	/*single user access for user on table (tableId, userId) is unique*/
	public UserAccess findByTableIdAndUserId(int tableId, int userId);

	/*delete all userAccess on all tables for given user
	 * called from : deleteUser(userId) and from saveUsers(List<User>) if some user was deactivated*/
	@Modifying
	@Query("delete from UserAccess ua where ua.user.id = ?1")
	public void deleteForUser(int id);
	
	
	/*user access for User on table*/
	@Query("select ua from UserAccess ua "
			+ " where UPPER(ua.user.userName) = UPPER(:userName) " 
			+ " and  ua.table.active = TRUE "  
			+ " and ua.table.id =:tableId")
	public List<UserAccess> getUserAccessForUserOnTable(@Param("userName") String userName, @Param("tableId") Integer tableId);
		
}
