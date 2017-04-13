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

@Service
public class DataModuleServiceImpl extends BaseServiceImpl implements DataModuleService{
	
	private static final Logger logger = LoggerFactory.getLogger(DataModuleServiceImpl.class);
	private static final String TEST_USERNAME = "Elizabeth.H.Anderson@mailinator.com";
	
	@Override
	public void authenticateUser() {
		// TODO Auto-generated method stub
		logger.info("authenticateUser");
		
	}
	
		
	/*precondition - get user and userName from user registration service*/
	@Override
	public List<Server> getActiveServers() {
		logger.info("getActiveServers" );
		authenticateUser(); //TODO
		logger.info("provide proper userName first" );
		logger.info("using TEST user from local DB: " + TEST_USERNAME );
		List<Server> servers = repositoryManager.getServerRepository().activeServersWithActiveTablesForUser(TEST_USERNAME);
		return servers;
	}
	
	/*precondition - get user and userName from user registration service
	 * precondition -  getActiveServers() executed with success before this step*/
	@Override
	public List<Table> getActiveTables(Integer serverId) {
		logger.info("getActiveTables");
		authenticateUser(); //TODO
		logger.info("provide proper userName first" );
		logger.info("using TEST user from local DB: " + TEST_USERNAME );
		return repositoryManager.getTableRepository().activeTablesOnActiveServerForUser(TEST_USERNAME, serverId);
		
	}

	/*precondition - get user and userName from user registration service
	 * precondition -  getActiveServers() getActiveTables and executed with success before this step*/
	@Override
	public List<Column> getActiveColumns(Integer tableId) {
		logger.info("getActiveColumns");
		authenticateUser(); //TODO
		logger.info("provide proper userName first" );
		logger.info("using TEST user from local DB: " + TEST_USERNAME );
		return repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(tableId);
		
	}

	
	@Override
	public UserAccess getUserAccessDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub
		authenticateUser() ;
		return null;
	}

	@Override
	public List<Server> getTableDetails(Long serverId, Long tableId) {
		// TODO Auto-generated method stub
		authenticateUser();
		return null;
	}

	@Override
	public List getDropDownData(Column display, Column store) {
		// TODO Auto-generated method stub
		authenticateUser();
		return null;
	}






	
	
}
