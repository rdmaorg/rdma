package ie.clients.gdma2.adaptor.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryManager {

	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private TableRepository tableRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ColumnRepository columnRepository;
	
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

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public ColumnRepository getColumnRepository() {
		return columnRepository;
	}

	public void setColumnRepository(ColumnRepository columnRepository) {
		this.columnRepository = columnRepository;
	}
	
	
	
}
