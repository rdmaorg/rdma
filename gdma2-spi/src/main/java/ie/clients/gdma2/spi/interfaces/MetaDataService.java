package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ConnectionType;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;

public interface MetaDataService {

	/*Connection Type*/
	public List<ConnectionType> getAllConnectionTypes();
	public PaginatedTableResponse<ConnectionType> getConnectionTypeTable(
			String searchValue, String orderByColumn, String orderByDirection,int startIndex, int length);
	public void saveConnectionType(ConnectionType connectionType);
	public void deleteConnectionType(Integer id);
	
	/*Server*/
	public List<Server> getAllServers();
	public PaginatedTableResponse<Server> getServers(String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	public void saveServer(Server server);
	public void deleteServer(int id);
	public void deleteServer(Server server);
	public Server findOne(Integer serverId);
	
	/*Tables*/
	
	public List<Table> getAllTables();
	public List<Table> findTablesByServerId(Integer serverId);
	public List<Table> findByServerIdAndActiveTrue(Integer serverId);
	public Long countTablesForServer(Integer serverId);
	public PaginatedTableResponse<Table> getTablesForServer(Integer serverId, String matching, String orderBy,
			String orderDirection, int startIndex, int length );
	
	public Table saveTable(Table table);
	public void deleteTable(int id);
	
	
	
	/*User*/
	public List<User> getAllUsers();
	public List<User> getAllActiveUsers();
	public List<User> findByUserNameIgnoreCase(String userName);
	public List<User> saveUsers(List<User> userList);
	public PaginatedTableResponse<User> getUsers(String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	public void saveUser(User user);
	public void deleteUser(int id);
	public User findOneUser(int id);
	
	
	/*Column*/
	public List<Column> getAllColumns();//TODO delete, just for initail repos testing
	public List<Column> findByTableIdAndActiveTrue(Integer tableId);
	public void deleteColumn(int id);
	public void deleteColumn(Column column); /*TODO check if needed*/ 
	
	public List<Column>saveColumns(List<Column> columnList);
	/*TODO check is saving single user is needed or can be done via saveColums()*/
	/* public Set<Column> getColumnsForTable(Long serverId, Long tableId); //see GdmaAdminAjaxFacade.getColumnsForTable */
	
	
	/*UserAccess*/
	public List<UserAccess> getAllUserAccess();
	public PaginatedTableResponse<UserAccess> getUserAccessForTable(Integer tableId, String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	public void saveUserAccess(UserAccess userAccess);
	public List<UserAccess> saveUserAccessList(List<UserAccess> userAccessList);
	public void deleteUserAccess(Integer id);

	
	/*DynamicDAO part*/
	
	/*TEST ONLY first Time Tables and Columns creating for selected servers*/
	public Server getTablesMetadataForServerTestOnly(Integer serverId);
	
	public PaginatedTableResponse<Table> getActiveSynchedTablesForServer(Integer serverId, String matching, String orderBy,
			String orderDirection, int startIndex, int length );
	
	public PaginatedTableResponse<Column> getActiveSynchedColumnsForTable(Integer tableId, String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	
}
