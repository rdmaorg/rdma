package ie.clients.gdma2.spi.interfaces;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

import java.util.List;

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
	
	public Long countTablesForServer(Integer serverId);
	
	public PaginatedTableResponse<Table> getTablesForServer(Integer serverId, String matching, String orderBy,
			String orderDirection, int startIndex, int length );

	/*User*/
	public List<User> getAllUsers();
}
