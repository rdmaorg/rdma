package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.UserAccess;

import org.springframework.data.repository.PagingAndSortingRepository;

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

    

    
	
}
