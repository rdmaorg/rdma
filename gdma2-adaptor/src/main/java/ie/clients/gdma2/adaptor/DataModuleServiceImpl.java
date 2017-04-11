package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.spi.interfaces.DataModuleService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataModuleServiceImpl extends BaseServiceImpl implements DataModuleService{
	
	private static final Logger logger = LoggerFactory.getLogger(DataModuleServiceImpl.class);
	
	@Override
	public void authenticateUser() {
		// TODO Auto-generated method stub
		logger.info("authenticateUser");
		
	}
	/*precondition - get user and userName from user registration service*/
	@Override
	public List<Server> getServerTableList() {
		logger.info("getServerTableList" );
		authenticateUser(); //TODO
		logger.info("provide proper userName first" );
		String userNameDummy = "Elizabeth.H.Anderson@mailinator.com";
		logger.info("using TEST user from local DB: " + userNameDummy );
		return repositoryManager.getServerRepository().activeServersWithActiveTablesForUser(userNameDummy);
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
