package ie.clients.gdma2.adaptor.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryManager {

	@Autowired
	private ServerRepository serverRepository;

	public ServerRepository getServerRepository() {
		return serverRepository;
	}

	public void setServerRepository(ServerRepository serverRepository) {
		this.serverRepository = serverRepository;
	}
	
	
}
