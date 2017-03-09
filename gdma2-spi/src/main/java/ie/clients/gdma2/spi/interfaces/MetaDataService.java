package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;
import java.util.Set;

public interface MetaDataService {
	
	/*Server*/
	public List<Server> getAllServers();
	public PaginatedTableResponse<Server> getServers(String matching, String orderBy,
			String orderDirection, int startIndex, int length);

	public void saveServer(Server server);
	public void deleteServer(int id);
	public void deleteServer(Server server);
	
	/*Tables*/
	public Server findOne(Integer serverId);
	public List<Table> getAllTables();
	public List<Table> findTablesByServerId(Integer serverId);
	public List<Table> findByServerIdAndActiveTrue(Integer serverId);
	public Long countTablesForServer(Integer serverId);
	public PaginatedTableResponse<Table> getTablesForServer(Integer serverId, String matching, String orderBy,
			String orderDirection, int startIndex, int length );

	public void saveTable(Table table);
	public void deleteTable(int id);
	
	/*User*/
	public List<User> getAllUsers();
	public List<User> getAllActiveUsers();
	public List<User> findByUserNameIgnoreCase(String userName);
	
	public List<User> saveUsers(List<User> userList);
	/*TODO check is saving single user is needed or can be done via saveUsers()*/
	public void saveUser(User user);
	public void deleteUser(int id);

	public PaginatedTableResponse<User> getUsers(String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	
	/*Column*/
	public List<Column> getAllColumns();//TODO delete, just for initail repos testing
	public List<Column> findByTableIdAndActiveTrue(Integer tableId);
	
	
	public PaginatedTableResponse<Column> getColumnsForTable(Integer tableId, String matching, String orderBy,
			String orderDirection, int startIndex, int length);
	
	public void deleteColumn(int id);
	public void deleteColumn(Column column); /*TODO check if needed*/ 
	
	public List<Column>saveColumns(List<Column> columnList);
	/*TODO check is saving single user is needed or can be done via saveColums()*/
	
	/* public Set<Column> getColumnsForTable(Long serverId, Long tableId); //see GdmaAdminAjaxFacade.getColumnsForTable */
	
	
	/*UserAccess*/
	/*Server*/
	public List<UserAccess> getAllUserAccess();
	
	/*
	public PaginatedTableResponse<UserAccess> getUserAccessForTable(Integer tableId, String matching, String orderBy,
			String orderDirection, int startIndex, int length);

	public void saveUserAccess(UserAccess userAccess);
	public void deleteUserAccess(int id);
	public void deleteUserAccess(UserAccess userAccess);
	*/
	
	
}
