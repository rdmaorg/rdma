package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.spi.interfaces.DataModuleService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * all implemented operations will use userContextProvider to get authenticated User  
 * @author Avnet
 *
 */
@Service
public class DataModuleServiceImpl extends BaseServiceImpl implements DataModuleService{
	
	private static final Logger logger = LoggerFactory.getLogger(DataModuleServiceImpl.class);
		
	
	@Override
	public List<Server> getActiveServers() {
		logger.info("getActiveServers" );
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		List<Server> servers = repositoryManager.getServerRepository().activeServersWithActiveTablesForUser(userContextProvider.getLoggedInUserName());
		return servers;
	}
	
	

	@Override
	public List<Table> getActiveTables(Integer serverId) {
		logger.info("getActiveTables");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		return repositoryManager.getTableRepository().activeTablesOnActiveServerForUser(userContextProvider.getLoggedInUserName(), serverId);
		
	}

	@Override
	public List<Column> getActiveColumns(Integer tableId) {
		logger.info("getActiveColumns");
		logger.info("user: " + userContextProvider.getLoggedInUserName());
		return repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(tableId);
		
	}

	
	@Override
	public UserAccess getUserAccessDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List<Server> getTableDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public List getDropDownData(Column display, Column store) {
		// TODO Auto-generated method stub
		
		return null;
	}






	
	
}
