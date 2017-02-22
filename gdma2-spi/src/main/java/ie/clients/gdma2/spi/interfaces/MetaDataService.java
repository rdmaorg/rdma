package ie.clients.gdma2.spi.interfaces;

import java.util.List;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;

public interface MetaDataService {
	public List<Server> getAllServers();
	public PaginatedTableResponse<Server> getServers(String matching, String orderBy,
			String orderDirection, int startIndex, int length);

	public void saveServer(Server server);
}
