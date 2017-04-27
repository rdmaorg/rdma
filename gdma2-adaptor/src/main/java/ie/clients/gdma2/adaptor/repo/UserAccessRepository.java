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

	/*
	UserAccessDAO {
	
	public List get(); //used for test only
    
    public void save(UserAccess userAccess); //used in GdmaAdminAjaxFacade.getAccessListForTable() for loading UA
    										//also used in GdmaAdminAjaxFacade.saveAccessList - to save individual UA (not list) per User for choosen table
    
    public void delete(UserAccess UserAccess); //used in ServerUtil.resyncTableList (as part of loading Table List for Server in Admin module)
    
        
    public UserAccess get(Long tableId);	//getTablesFOrServer => resynchTableList 
    
    public List<UserAccess> get(Long tableId); //	List UserAccess = getHibernateTemplate().find("from UserAccess userAccess where userAccess.tableId = ? ", tableId); 
    											// used in resyncTableList(Server server)			
    
    
    public UserAccess get(Long tableId, Long userId); // // from UserAccess userAccess where userAccess.tableId = ? and userAccess.userId = ?", params);
    	//called from: 
    //1. GdmaAdminAjaxFacade.getAccessListForTable()
    //2. public UserAccess getUserAccessDetails(Long serverId, Long tableId) {
    // 3. public UserAccess getUserAccessForTable(Long tableId, Long userId) {
    //4.   public void saveAccessList(UserAccess userAccess) {
    */

	
		
	/*count all UserAccess entries for table*/
	@Query("select count(ua) from UserAccess ua where ua.table.id = ?1")
	public long countUserAccessForTable(Integer id);

	
//	@Query("SELECT c FROM Child c WHERE c.parent.id=:parentId")
//	public Page<Child> findByParentId(@Param("parentId") Long parentId, Pageable page);
	@Query("select ua from UserAccess ua where ua.table.id = ?1")
	public List<UserAccess> findPaginatedUserAccessByTable(Integer tableId, Pageable pageable);
	
	@Query("select count(ua) from UserAccess ua where upper(ua.user.userName) like ?1")
	public long getCountMatching(String matching);

	@Query("select ua from UserAccess ua where (upper(ua.user.userName) like ?1 or upper(ua.user.firstName) like ?1 or CAST(ua.id as text) like ?1) and ua.table.id = ?2")
	public List<UserAccess> getMatchingUserAccesses(String match, Integer tableId, Pageable pageable);
	
	@Query("select ua from UserAccess ua where ua.table.id = ?1")
	public List<UserAccess> findByTableId(int tableId);
	
	
	/*single user access for user on table (tableId, userId) is unique*/
	public UserAccess findByTableIdAndUserId(int tableId, int userId);

	/*delete all userAccess on all tables for given user
	 * called from : deleteUser(userId) and from saveUsers(List<User>) if some user was deactivated*/
	@Modifying
	@Query("delete from UserAccess ua where ua.user.id = ?1")
	public void deleteForUser(int id);
	
	/*
	@Query("select s from Server s where upper(s.name) like ?1 or upper(s.username) like ?1 or upper(s.connectionUrl) like ?1 or upper(s.connectionType.name) like ?1 or upper(s.prefix) like ?1 ")
	public List<Server> getMatchingServers(String matching, Pageable pageable);
	*/
	
	
	
	
    

    
	
}
