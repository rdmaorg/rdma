package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ConnectionType;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.interfaces.MetaDataService;
import ie.clients.gdma2.util.EntityUtils;
import ie.clients.gdma2.util.HashUtil;
import ie.clients.gdma2.util.SQLUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetaDataServiceImpl extends BaseServiceImpl implements MetaDataService {

	private static final Logger logger = LoggerFactory.getLogger(MetaDataServiceImpl.class);


	/*Connection type section*/
	@Override
	public List<ConnectionType> getAllConnectionTypes() {
		return IteratorUtils.toList(repositoryManager.getConnectionTypeRepository().findAll().iterator());
	}

	@Override
	public PaginatedTableResponse<ConnectionType> getConnectionTypeTable(String searchValue, String orderByColumn, String orderByDirection,
			int startIndex, int length) {

		logger.debug("getConnectionTypeTable");
		long total = repositoryManager.getConnectionTypeRepository().count();
		long filtered = 0;

		List<ConnectionType> connectionTypes = null;

		if(StringUtils.isBlank(searchValue)){
			filtered = total;
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			Page<ConnectionType> connectionTypePages = repositoryManager.getConnectionTypeRepository().findAll(pagingRequest);
			connectionTypes = connectionTypePages.getContent();
		} else {
			String match = "%" +searchValue.trim().toUpperCase() + "%";
			filtered = repositoryManager.getConnectionTypeRepository().getCountMatching(match);
			PageRequest pagingRequest = getPagingRequest(orderByColumn, orderByDirection, startIndex, length, total);
			connectionTypes =  repositoryManager.getConnectionTypeRepository().getMatchingConnectionTypes(match, pagingRequest);
		}

		logger.debug("Search ConnectionType: Search: " + searchValue + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + connectionTypes.size());

		return getPaginatedTableResponse(connectionTypes != null ? connectionTypes : new ArrayList<ConnectionType>(), total, filtered);
	}


	@Transactional
	@Override
	public void saveConnectionType(ConnectionType connectionType) {
		repositoryManager.getConnectionTypeRepository().save(connectionType);
	}

	@Transactional
	@Override
	public void deleteConnectionType(Integer id) {
		repositoryManager.getConnectionTypeRepository().delete(id);

	}


	/*Server section*/
	@Override
	public List<Server> getAllServers() {
		List<Server> servers = IteratorUtils.toList(repositoryManager.getServerRepository().findAll().iterator());
		EntityUtils.emptyPasswordForServers(servers);
		return servers;
	}


	@Override
	public PaginatedTableResponse<Server> getServers(String matching, String orderBy, String orderDirection,
			int startIndex, int length) {
		logger.info("getServers");
		List<Server> servers = null;
		long total = 0;
		long filtered = 0;

		if (StringUtils.isBlank(matching)) {
			total = repositoryManager.getServerRepository().count();
			filtered = total;
			servers = repositoryManager.getServerRepository()
					.findAll(getPagingRequest(orderBy, orderDirection, startIndex, length, total)).getContent();

		} else {
			total = repositoryManager.getServerRepository().count();
			filtered = repositoryManager.getServerRepository()
					.getCountMatching("%" + matching.trim().toUpperCase() + "%");

			servers = repositoryManager.getServerRepository().getMatchingServers(
					"%" + matching.trim().toUpperCase() + "%",
					getPagingRequest(orderBy, orderDirection, startIndex, length, total));

		}
		EntityUtils.emptyPasswordForServers(servers);
		
		logger.debug("Search Servers:: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + servers.size());

		return getPaginatedTableResponse(servers != null ? servers : new ArrayList<Server>(), total, filtered);
	}


	/**
	 * a) on initial server creation
	 * 		/rest/server/save
	 *  - Modal is opened and password field is empty and mandatory. 
	 * User enters password value and submits modal.
	 * if SAVING simply save all values to DB with password value.
	 *   
	 * b) reading initially saved Server with password value: 
	 *    /rest/server/{id}
	 *   in server list - password column is removed and will not be displayed but if user needs to update password,
	 *    but when [Edit] is clicked and Modal is opened - password field is present. 
	 *   Backend will read value from DB but will mask password (********) and send this value to UI so user knows that value exists
	 *   
	 *   Now when Admin, changes value in Modal and Submits - UPDATE server request is sent 
	 *    - if it still contains masked password - do now update, use old pass
	 *      else take new value and update password 
	
	 */
	@Transactional
	@Override
	public void saveServer(Server server) {
		logger.info("saving/update server");
		int serverId = server.getId(); 
		logger.info("serverId: " +  serverId);
		//check if user is INSERT/UPDATE (-1 is for INSERT)
		//INSERT
		if(serverId < 0 ){
			logger.info("INSERT new server");
		} else {
			//UPDATE
			logger.info("UPDATE existing server");
			Server serverBeforeUpdate = repositoryManager.getServerRepository().findOne(serverId);
			if(EntityUtils.PASSWORD_MASK.equals(server.getPassword())){
				logger.info("no change - keep all password");
				//do not save masked value, keep the same old pass
				server.setPassword(serverBeforeUpdate.getPassword());
				//else just save new updated pass value  
			} else{
				logger.info("password is changed and will be udpated");
			}
		}				
		repositoryManager.getServerRepository().save(server);
	}


	@Transactional
	@Override
	public void deleteServer(int id) {
		repositoryManager.getServerRepository().delete(id);
	}

	@Transactional
	@Override
	public void deleteServer(Server server) {
		repositoryManager.getServerRepository().delete(server);
	}


	/*TABLE section*/

	@Override
	public Server findOne(Integer serverId) {
		Server server = repositoryManager.getServerRepository().findOne(serverId);
		return  EntityUtils.emptyServerPassword(server);
	}

	@Override
	public List<Table> getAllTables() {
		//return IteratorUtils.toList(repositoryManager.getServerRepository().findAll().iterator());
		return IteratorUtils.toList(repositoryManager.getTableRepository().findAll().iterator());

	}

	@Override
	public List<Table> getRemoteServerTableMetadata(Integer serverId) {
		//call synnch logic, perform complete synch and transactional save to local DB, then load all previously inserted
		synhronizeTablesForServer(serverId);
		return IteratorUtils.toList(repositoryManager.getTableRepository().findAll().iterator());
	}


	/*TODO decide if synch is going to be call only initially or always - on each search attempt*/
	@Override
	public PaginatedTableResponse<Table> getActiveLocalTablesForServer(Integer serverIdParam,
			String matching, String orderBy, String orderDirection,
			int startIndex, int length) {

		logger.debug("getActiveSynchedTablesForServer: " + serverIdParam);

		//synch tables first
		//synhronizeTablesForServer(serverIdParam); THIS IS DONE SEPARATELLY

		logger.debug("...get Active Synched Tables now");

		Server server = null;
		List<Table> tables = null;
		long total = 0;
		long filtered = 0;

		server = repositoryManager.getServerRepository().findOne(serverIdParam);
		logger.info("server.getTables().size():" + server.getTables().size());
		int serverId = server.getId();
		//TODO if(null == server)

		//empty search, select all active tables for server
		if (StringUtils.isBlank(matching)) {
			total = repositoryManager.getTableRepository().countActiveTablesForServer(serverId);
			logger.debug("Total active tables, no search:" + total);
			filtered = total;

			logger.debug("findALL...getPagingRequest():");

			tables = repositoryManager.getTableRepository().getActivePagableTables(serverId,
					getPagingRequest(orderBy, orderDirection, startIndex, length, total));

		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			total = repositoryManager.getTableRepository().countActiveTablesForServer(serverId);
			logger.debug("Total, with search:" + total);
			filtered = repositoryManager.getTableRepository().getCountMatching(match, serverId);

			tables = repositoryManager.getTableRepository().getActiveMatchingTables(
					match,serverId,
					getPagingRequest(orderBy, orderDirection, startIndex, length, total));

			logger.debug("tables found: " + tables.size());

		}

		logger.debug("Search Tables: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered	+ ", Result Table Count: " + tables.size());

		return getPaginatedTableResponse(tables != null ? tables : new ArrayList<Table>(), total, filtered);	
	}


	@Override
	public List<Table> findTablesByServerId(Integer serverId) {
		return repositoryManager.getTableRepository().findByServerId(serverId);
	}

	@Override
	public Long countTablesForServer(Integer serverId) {
		return repositoryManager.getTableRepository().countTablesForServer(serverId);
	}

	@Override
	public List<Table> findByServerIdAndActiveTrue(Integer serverId) {
		return repositoryManager.getTableRepository().findByServerIdAndActiveTrue(serverId);
	}

	@Transactional
	@Override
	public Table saveTable(Table table) {
		return repositoryManager.getTableRepository().save(table);
	}

	@Transactional
	@Override
	public void deleteTable(int id) {
		repositoryManager.getTableRepository().delete(id);

	}


	/*USER*/

	//Emptying the passwords
	private List<User> emptyPasswords(List<User> users){
		if( users != null && !users.isEmpty()){
			for(User u: users){
				logger.debug("emptyPasswords executed");
				u.setPassword("");
			}
		}
		return users; 
	}

	private User emptyPassword(User user){
		if( user != null) {
			logger.debug("emptyPassword executed");
			user.setPassword("");
		}
		return user; 
	}



	@Override
	public List<User> getAllUsers() {
		List<User> users =  IteratorUtils.toList(repositoryManager.getUserRepository().findAll().iterator());
		return emptyPasswords(users);
	}

	@Override
	public List<User> getAllActiveUsers() {
		List<User> users =  repositoryManager.getUserRepository().findByActiveTrue();
		return emptyPasswords(users);
	}

	@Override
	public PaginatedTableResponse<User> getUsers(String matching,
			String orderBy, String orderDirection, int startIndex, int length) {

		logger.info("*** getUsers()");
		List<User> users = null;
		long total = 0;
		long filtered = 0;

		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getUserRepository().count();
			filtered = total;
			logger.info("Total, no search:" + total);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			Page<User> userPages = repositoryManager.getUserRepository().findAll(pagingRequest);
			users = userPages.getContent();
		} else{
			total = repositoryManager.getUserRepository().count();
			String match = "%" + matching.trim().toUpperCase() + "%";
			filtered = repositoryManager.getUserRepository().getCountMatching(match);
			logger.info("Total, with search:" + total + ", filtered: " + filtered);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			users = repositoryManager.getUserRepository().getMatchingUsers(match, pagingRequest);
		}
		//Emptying the password
		emptyPasswords(users);

		logger.info("Search Users: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + users.size());

		return getPaginatedTableResponse( users != null ? users : new ArrayList<User>(), total, filtered);

	}


	@Override
	public List<User> findByUserNameIgnoreCase(String userName) {
		List<User> users = IteratorUtils.toList(repositoryManager.getUserRepository().findByUserNameIgnoreCase(userName).iterator());
		return emptyPasswords(users);
	}


	/**
	 * save / update users
	 * if user is updated and deactivated, delete orphan UserAccess entries on all tables for that user 
	 * scenarios :
	 *  S1: create new inactive user - INSERT, skip orphan delete
	 *  S2: create new active user - INSERT, skip orphan delete
	 *  S3: update existing user - UPDATE:
	 *  	S3-1: if user activated - DELETE orphans	
	 *  	S3-2: 	else any other UPDATE - skip orphan delete
	 *  
	 *  S4 authentication section: 
	 *   if internal DB authentication provider is used :
	 *    - User View will contain additional column in UI - userPassword
	 *    - on backend there is additional Entity field User.password and in DB : users_gdma2.user_password
	 *    
	 *   S4.1 On INSERT, we need to encode password taken from UI and save to DB
	 *    Saved password is never to be show on UI - TODO : make all loading passwords in all methods BLANK
	 *    
	 *    If User is UPDATEed in UI and userPassword field is UPDATEed then backend must: 
	 *   S4-2:	 check if password is blank, if not  re-encode new password and store it
	 *   S4-3 :							    else skip password rencoding  
	 *   
	 *   at the end save all users and return user list with empty passwords
	 *    
	 */
	@Transactional
	@Override
	public List<User> saveUsers(List<User> userList) {
		logger.info("saving/updating users");
		for(User user: userList){
			int userId = user.getId(); 
			logger.info("userId: " +  userId);
			//check if user is INSERT/UPDATE (-1 is for INSERT)

			//TODO: Uncomment the following to set the SHA-1 encoded password for the user
			//DB will always contain hashed password
			//Handle the user update scenario in which if password is not changed, then it should not be re-encoded
			//This is to allow local storage of password when not connecting to external authentication provider like LDAP or Active Directory


			//INSERT
			if(userId < 0 ){
				//S4.1 INSERT
				logger.info("INSERTing new user");
				if(StringUtils.isNotBlank(user.getPassword())){
					logger.info("creating hash password");
					user.setPassword(HashUtil.hash(user.getPassword()));
				}	
			} else {
				//UPDATE
				logger.info("UDPATing existing user");
				User userBeforeUpdate = repositoryManager.getUserRepository().findOne(userId);

				//S3 check - existing user is deactivated
				if(userBeforeUpdate.isActive() == true && user.isActive() == false){
					logger.info("user is deactivated, deleting user access");
					repositoryManager.getUserAccessRepository().deleteForUser(userId);
				}

				//S4-2 and S4-3
				if(StringUtils.isNotBlank(user.getPassword())){
					if(userBeforeUpdate.getPassword().equals(user.getPassword())){
						logger.info("skip password re-encoding");
					} else {
						logger.info("password was updated: re-encoding new pass: ");
						//						logger.info("password was updated: re-encoding new pass: " + user.getPassword());
						user.setPassword(HashUtil.hash(user.getPassword()));
						//						logger.info("password was updated: re-encoded new pass: " + user.getPassword());
					}

				}	
			}
		}

		List<User> savedUsers = IteratorUtils.toList(repositoryManager.getUserRepository().save(userList).iterator());
		//We can't empty the password until the transaction is over, otherwise empty password is stored in DB.
		//The solution is either empty the password in UserResource() or return a deep copy of the list.
		//To create a deep copy, we can use BeanTransformation project which uses dozer as underlying framework.
		//Here without emptying the password, this method is returning hashed password.
		//This isn't so bad either, because the hashed password can only be seen by the user who saved the password.
		//In subsequent calls, the password will be empty anyway.
		//		 return emptyPasswords(savedUsers); 
		return savedUsers;
	}


	@Transactional
	@Override
	public void deleteUser(int id) {
		//DELETE USER_ACCESS first or FK constraint will be violeted for all existing UI.user_id = id of deleted user
		repositoryManager.getUserAccessRepository().deleteForUser(id);
		repositoryManager.getUserRepository().delete(id);

	}

	/*COLUMN section*/

	@Override
	public List<Column> getRemoteTableColumnsMetadata(Integer tableId) {
		synhronizeColumnsForTable(tableId); 
		return IteratorUtils.toList(repositoryManager.getColumnRepository().findByTableId(tableId).iterator());
	}


	@Override
	public List<Column> getAllColumns() {
		return IteratorUtils.toList(repositoryManager.getColumnRepository().findAll().iterator());
	}


	/**
	 * called from UI to UPDATE columns
	 * called from resynh logic to UPDATE columns while resolving deleted FK relations
	 * called from metadata initail load
	 */
	@Transactional
	@Override
	public List<Column> saveColumns(List<Column> columnList) {
		for (Column column : columnList) {
			EntityUtils.applyColumnRules(column);
		}
		return IteratorUtils.toList(repositoryManager.getColumnRepository().save(columnList).iterator());
	}



	@Transactional
	@Override
	public void deleteColumn(int id) {
		repositoryManager.getColumnRepository().delete(id);

	}

	@Override
	public List<Column> findByTableIdAndActiveTrue(Integer tableId) {
		return IteratorUtils.toList(repositoryManager.getColumnRepository().findByTableIdAndActiveTrue(tableId).iterator());

	}

	/*paginated Columns for table, special case for ADMIN, see: GdmaAdmin.getColumnsForTable */
	@Override
	public PaginatedTableResponse<Column> getActiveLocalColumnsForTable(
			Integer tableId, String matching, String orderBy, String orderDirection,int startIndex, int length) {
		logger.info("getActiveSynchedColumnsForTable : " + tableId);

		//synch tables first
		//synhronizeColumnsForTable(tableId); IS NOW PERFORMED SEPARATELLY

		logger.debug("...get ACTIVE Synched Columns now");

		Table table = null;
		List<Column> columns = null;
		long total = 0;
		long filtered = 0;

		table = repositoryManager.getTableRepository().findOne(tableId);
		//TODO if(null == table)

		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getColumnRepository().countActiveForTable(table.getId());
			logger.debug("Total Active columns for table, no search:" + total);
			filtered = total;

			logger.debug("find all Active columns by table:");
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			columns = repositoryManager.getColumnRepository().findActiveforTable(table.getId(), pagingRequest);
			logger.debug("columns found: " + (null != columns ? columns.size() : "0"));
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			logger.debug("searching for: " +  match);
			total = repositoryManager.getColumnRepository().countActiveForTable(table.getId());
			logger.debug("Total active count columns for table, with search:" + total);
			filtered = repositoryManager.getColumnRepository().countActiveAndMatchingForTable(match, table.getId());
			logger.debug("filtered : " + filtered + ", for match: " + match);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			columns = repositoryManager.getColumnRepository().findActiveAndMatchingforTable(match, table.getId(), pagingRequest);
		}

		logger.debug("Search Columns: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + ((columns != null) ? columns.size() : "0"));

		return getPaginatedTableResponse(columns != null ? columns : new ArrayList<Column>(), total, filtered);
	}



	@Override
	public void deleteColumn(Column column) {
		// TODO Auto-generated method stub

	}


	/*UserAccess section*/

	@Override
	public List<UserAccess> getAllUserAccess() {
		return IteratorUtils.toList(repositoryManager.getUserAccessRepository().findAll().iterator());
	}

	/*GENERATE and LOAD UserAccess list per table: paginated table of UserAccess by tableId*/
	@Override
	public PaginatedTableResponse<UserAccess> getUserAccessForTable(
			Integer tableId, String matching, String orderBy,String orderDirection, int startIndex, int length) {

		logger.info("getUserAccessForTable with id: " + tableId);


		//getAccessListForTable(tableId);
		generateUserAccessListForTable(tableId);

		Table table = null;
		List<UserAccess> userAccessListForTable = null;
		long total = 0;
		long filtered = 0;

		table = repositoryManager.getTableRepository().findOne(tableId);
		//TODO if(null == table)
		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getUserAccessRepository().countUserAccessForTable(table.getId());
			logger.debug("Total count UserAccess for tableId:" + tableId  + ", no search:" + total);
			filtered = total;
			logger.debug("findALL...getPagingRequest():");
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);

			userAccessListForTable  = repositoryManager.getUserAccessRepository().findPaginatedUserAccessByTable(tableId, pagingRequest);
			logger.debug("userAccess entries found: " + (null != userAccessListForTable ? userAccessListForTable.size() : "0"));
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			total = repositoryManager.getUserAccessRepository().countUserAccessForTable(table.getId());
			logger.debug("Total count UserAccess for table, with search:" + total);
			filtered = repositoryManager.getUserAccessRepository().getCountMatching(match);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			userAccessListForTable =  repositoryManager.getUserAccessRepository().getMatchingUserAccesses(match, tableId,pagingRequest);

		}

		logger.debug("Search UserAccess: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + ((userAccessListForTable != null) ? userAccessListForTable.size() : "0"));

		return getPaginatedTableResponse(userAccessListForTable, total, filtered);


	}

	/**
	 * LOADING(SAVING)/UPDATING USER_ACESS table
	 * 
	 *  a) LOADING (SAVING default)
	 *
	 * UserAccess list represents list of all Active users on system and their privileges for selected Active table.
	 * 
	 * RULE: Entries are NEVER created manually (like use Add button to save 1 UA)
	 * RULE: Loading - DISPLAY - is Saving/generating NEW UA at the same time
	 * RULE: Once displayed, if edited, on submit UA list is UPDATED 
	 * 
	 * In new version of GDMA, we will create users view to add new and activate/deactivate existing users. 
	 * 
	   UA view is never empty - it will always create UA for active users and display then for selected table
	 LOADING is 2 step process :
	  - All active Users must be displayed in UA view - so create new UA if needed and saveUA  
	  - loadUAforTable(talbeId)  - read UA table using parent_id (Table.id)

	 Initiated from table view. 
	 TODO: check if there are any Active users in system - if not reject request to display UA and inform Admin to create and Active at least 1 user

	 If there is at least 1 Active user: start INITIAL LOAD. it will be expensive - we need to create default UA for every active user, saveAll(UA) 
	 and then findUAforTable(tableId). Display result to end user as list of all active users for table with all privileges default to FALSE;

	 2nd Load
	 After INITIAL LOAD, some user can be deactivated some new may be added, some are as they were. 
	   OLD ACTIVE : Recognize previously created UA for active user and skip creating new (this will be just loaded in second step)
	   DEACTIVATED: TODO: On Deactivating user: define UA -  shall we delete all UA, or set all to FALSE or keep as they are.
	   NEW ACTIVE: 	Create new UA, set all to false and save. In second step this will be loaded too as active


	  INITIAL LOAD:
	  If NEW Active tables is added on server (tableId = 77) and there are e.g. 40 Active users: UserAccess list would present:
	   40 user access rows with all values default to FALSE.

	  LATER LOAD for existing tables and new users:
	  Once Administrator select Active table and asks for list of UserAccess for all Active users
	 (assuming that several users can be added/activated meanwhile):
	   	 this logic will load existing user access relations but new one for new users needs to be created, set default privileges to FALSE
	   	 and return with already existing once.      

		RULE:	We always iterate trough complete list of registered ACTIVE users. 

		We check if UserAccess(tableId, UserId) already exists,
		if not :  new empty one is created, this means every user on system will have empty user_access child record to table with selected tableId..

		( Here make sure there is constraint set on unique pair (tableId, userId) on every record in UserAccess Table. )

		If UserAccess exists ;
		 	- skip 
		Else (new one) : 	
			- create new by using parent key pair and set all privilege properties to FALSE 
			- load Table and add it to child emptyUserAccess.setTable(table);
			- add new UA to result list
			- save complete list save(userAccessList) at the end;	

	b) UPDATING 
		After UA view is loaded and Admin edit values in list, on submit List of UA is sent to be UPDATED
		use separate REST method to:  POST @RequestBody List<UserAccess> userAccessList

	 * @param tableId
	 * @return
	 */
	@Transactional
	public void generateUserAccessListForTable(Integer tableId) {
		logger.info("generateUserAccessListForTable");

		long  total = repositoryManager.getUserAccessRepository().countUserAccessForTable(tableId);
		logger.info("count UserAccess ForTable: " + total);

		//result list, add new UA and save at the end
		List<UserAccess> userAccessList = new ArrayList<UserAccess>();

		logger.info("loading all active users...");
		List<User> userList = repositoryManager.getUserRepository().findByActiveTrue();
		Table table = repositoryManager.getTableRepository().findOne(tableId);

		if(userList == null || userList.isEmpty()){
			logger.info("WARING: There are not ACTIVE users. Create and activated a user before trying to create UserAccess to this table!");
			return;
		}

		for(User user : userList)	{
			UserAccess userAccess = repositoryManager.getUserAccessRepository().findByTableIdAndUserId(tableId, user.getId());
			if(userAccess != null){
				logger.info("user access exists for user: " + user.getId());
			} else {
				//userAccess == null, create new
				logger.info("user access does NOT exist for user: " + user.getId() + ", creating new one");

				UserAccess emptyUserAccess = new UserAccess();

				emptyUserAccess.setUser(user);
				emptyUserAccess.setTable(table);

				emptyUserAccess.setAllowDisplay(false);
				emptyUserAccess.setAllowUpdate(false);
				emptyUserAccess.setAllowInsert(false);
				emptyUserAccess.setAllowDelete(false);

				userAccessList.add(emptyUserAccess);
			}

		}//for

		logger.info("There have been : " + userAccessList.size() + " users, created/activated before last check");
		if(!userAccessList.isEmpty()){
			repositoryManager.getUserAccessRepository().save(userAccessList);	
		}

	}


	@Transactional
	@Override
	public void saveUserAccess(UserAccess userAccess) {
		repositoryManager.getUserAccessRepository().save(userAccess);

	}

	@Transactional
	@Override
	public List<UserAccess> saveUserAccessList(List<UserAccess> userAccessList) {
		return IteratorUtils.toList(repositoryManager.getUserAccessRepository().save(userAccessList).iterator());
	}


	@Transactional
	@Override
	public void deleteUserAccess(Integer id) {
		repositoryManager.getUserAccessRepository().delete(id);
	}


	/* get all tables and columns metadata from remote DB, save it to local DB
	 * then get saved table list for server*/
	@Override
	public List<Table> getMetadata(Integer serverId) {
		if (getAllTablesAndColumnsMetadata(serverId)){
			return repositoryManager.getTableRepository().findByServerId(serverId);
		} else {
			return null; //TODO
		}

	}


	/**
	1. Using proper DriverClass from ConnectioType user connects to server (Column : connection_types_gdma2.connection_class)  
		Using this registered data from Server and ConnectionType - DataSource is created and JdbcTemplate using it
	2. Once connected user needs to execute 'SHOW TABLES' SQL and obtain Set of DB Table names from remote DB server
 	3. Then iteration over Set of Table Names and use ResultSet to get 'Metadata' on DB Columns 
 	4. Creating Set of Columns per each Table with specifics : PK or not, Size, ...othe constraints. 
 	5. Iterate over Table Name set. Save Table to Medatada DB, then use saved Table to save Columns (all in TRANSATION) 
    6. apply PK and special rules on columns before saving columns 
	 */
	@Transactional
	public boolean getAllTablesAndColumnsMetadata(Integer serverId) {
		logger.info("getAllTablesAndColumnsMetadata for server : "  + serverId);

		boolean isMetadaFetchSuccess = false;

		Server server = repositoryManager.getServerRepository().findOne(serverId);
		if(server == null){
			logger.error("server: " + serverId +  " does not exist!");
			return isMetadaFetchSuccess; //TODO define how to return unexpected values to UI 
		}

		String sqlGetTables = server.getConnectionType().getSqlGetTables(); 

		/*GET TABLES FOR SERVER*/
		List<String> tableNames = dynamicDAO.getSqlGetTables(server, sqlGetTables);

		if(tableNames == null || tableNames.isEmpty()){
			logger.error("server: " + serverId +  " does not containt any tables!");
			return isMetadaFetchSuccess; 
		}

		for (String tableName : tableNames) {
			/*GET COLUMNS FOR TABLE*/
			Set<Column> tableColumns = dynamicDAO.getTableColumns(server, tableName); 
			logger.info("columns fetched, creating and saving table: " + tableName + "  and it's columns");
			//persist new Table Entity with Set<Column>
			Table table = new Table();
			table.setServer(server);
			table.setName(tableName);
			table.setAlias(tableName);//initial creation of alias
			table.setColumns(tableColumns);
			// table.setActive(active); SET to ACTIVE by default

			//Table savedTable = saveTable(table);
			Table savedTable = repositoryManager.getTableRepository().save(table);

			//persist columns
			for (Column column : tableColumns) {
				column.setTable(savedTable);
				EntityUtils.applyColumnRules(column);
			}
			List<Column> columnList = new ArrayList<Column>(tableColumns);
			//saveColumns(columnList);
			repositoryManager.getColumnRepository().save(columnList);

		}
		isMetadaFetchSuccess =  true;
		logger.info("metadata fetch is success");
		return isMetadaFetchSuccess;

	}


	/**
	 * Admin only: synch and return tables for server after synch 
	 * <BR>
	 *  Transactional changes regarding : 
	 * - after synch there can be: INSERT of new remote table in local GDMA table 
	 * - Renaming existing tables in local DB - if found inactive
	 * - for deleted Table and it column that can be FK :  reference getDropDownColumnDisplay and getDropDownColumnStore must be removed from all columns from all Tables on server
	 * <BR> 
	 * TROUBLESHOOT @Transient dependency usage: 
	 * loading server will not load Set<Tables> for it. 
	 * When tables are loaded, be extra careful: do not server.setTable(loaded table set) 
	 *  because of two-way dependency : 
	 *  - logger.info("STACK : servet.getTable(): would cause STACK OVERFLOW  : Server.toString()
	 *  - also, later, in caller getActiveSynchedTablesForServer(), when
	 *  			server = repositoryManager.getServerRepository().findOne(serverIdParam);
	 *    ..you try to load Server again, it will NOT query DB, but it will use cache and return sever with set of tables (see:  hibernate session.load() vs hibernate session.get()) 
	 *    ...so you will end up with
	 *    	 server->set of tables-> each table -> server -> set of tables...infinity...
	 *    ...so response sent to client will be invalid 
	 *   Always check in app log that if DB call is made  
	 * @param serverId
	 */
	@Transactional
	private void synhronizeTablesForServer(Integer serverId) {
		logger.info("synhronizeTablesForServer: " + serverId);

		//loading only server and connection type, no tables
		Server server = repositoryManager.getServerRepository().findOne(serverId);
		List<Table> tableList = repositoryManager.getTableRepository().findByServerId(server.getId());
		dynamicDAO.synchTablesForServer(server, tableList); 
	}


	/**
	 * Transactional changes regarding : 
	 * - after synch there can be: INSERT of new remote columns in local GDMA table 
	 * - renaming existing columns in local DB - if found inactive
	 * - for deleted column  - reference getDropDownColumnDisplay and getDropDownColumnStore must be removed from all columns from all Tables on server
	 * @param tableId
	 */
	@Transactional
	private void synhronizeColumnsForTable(Integer tableId){
		logger.info("synhronizeColumnsForTable: " + tableId);

		Table table = repositoryManager.getTableRepository().findOne(tableId); //loading table, no columns
		Server server = table.getServer(); //loading only server and connection type, no tables
		Set<Column> columns = repositoryManager.getColumnRepository().findByTableId(tableId);
		dynamicDAO.synchColumnsForTable(server, table, columns);

	}

	@Override
	public User findOneUser(int id) {
		User user = repositoryManager.getUserRepository().findOne(id);
		return emptyPassword(user);
	}


	/*DATA module section - getting DISPLAYABLE columns DATA for selected table*/

	@Override
	public PaginatedTableResponse<Column> getColumnData(Integer tableId, String matching, int orderByColumnID, String orderDirection,
			int startIndex, int length) {


		logger.info("getColumnData : " + tableId);

		Table table = null;
		List<Column> columns = null;
		long total = 0;
		long filtered = 0;

		table = repositoryManager.getTableRepository().findOne(tableId);

		//TODO if(null == table)

		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getColumnRepository().countActiveForTable(table.getId());
			logger.info("Total Active columns for table, no search:" + total);
			filtered = total;

			logger.info("find all Active columns by table:");
			//PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			//columns = repositoryManager.getColumnRepository().findActiveforTable(table.getId(), pagingRequest);
			columns = dynamicDAO.getColumnData(tableId, matching, orderByColumnID, orderDirection,
					startIndex, length);


			logger.info("columns found: " + (null != columns ? columns.size() : "0"));
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			logger.info("searching for: " +  match);

			/*
				total = repositoryManager.getColumnRepository().countActiveForTable(table.getId());
				logger.debug("Total active count columns for table, with search:" + total);
				filtered = repositoryManager.getColumnRepository().countActiveAndMatchingForTable(match, table.getId());
				logger.debug("filtered : " + filtered + ", for match: " + match);
				PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
				columns = repositoryManager.getColumnRepository().findActiveAndMatchingforTable(match, table.getId(), pagingRequest);
			 */
		}

		logger.debug("Search Columns: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + ((columns != null) ? columns.size() : "0"));

		return getPaginatedTableResponse(columns != null ? columns : new ArrayList<Column>(), total, filtered);


	}





}
