package ie.clients.gdma2.adaptor.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryManager {

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private TableRepository tableRepository;
	
	public ServerRepository getServerRepository() {
		return serverRepository;
	}

	public void setServerRepository(ServerRepository serverRepository) {
		this.serverRepository = serverRepository;
	}

	public TableRepository getTableRepository() {
		return tableRepository;
	}

	public void setTableRepository(TableRepository tableRepository) {
		this.tableRepository = tableRepository;
	}
	
	
}
