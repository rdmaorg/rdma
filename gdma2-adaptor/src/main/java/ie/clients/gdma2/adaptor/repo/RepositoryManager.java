package ie.clients.gdma2.adaptor.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryManager {

	@Autowired
	ConnectionTypeRepository connectionTypeRepository;
	
	@Autowired
	private ServerRepository serverRepository;

	@Autowired
	private TableRepository tableRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ColumnRepository columnRepository;
	
	@Autowired
	private UserAccessRepository userAccessRepository;
	
	@Autowired
	private AuditHeaderRepository auditHeaderRepository;
	
	@Autowired
	private AuditRecordRepository auditRecordRepository;
	
	public ConnectionTypeRepository getConnectionTypeRepository() {
		return connectionTypeRepository;
	}

	public void setConnectionTypeRepository(
			ConnectionTypeRepository connectionTypeRepository) {
		this.connectionTypeRepository = connectionTypeRepository;
	}

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

	public UserAccessRepository getUserAccessRepository() {
		return userAccessRepository;
	}

	public void setUserAccessRepository(UserAccessRepository userAccessRepository) {
		this.userAccessRepository = userAccessRepository;
	}

	public AuditHeaderRepository getAuditHeaderRepository() {
		return auditHeaderRepository;
	}

	public void setAuditHeaderRepository(AuditHeaderRepository auditHeaderRepository) {
		this.auditHeaderRepository = auditHeaderRepository;
	}

	public AuditRecordRepository getAuditRecordRepository() {
		return auditRecordRepository;
	}

	public void setAuditRecordRepository(AuditRecordRepository auditRecordRepository) {
		this.auditRecordRepository = auditRecordRepository;
	}
	
}
