package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.ConnectionType;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.UserAccess;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.interfaces.MetaDataService;
import ie.clients.gdma2.util.ServerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
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
		return IteratorUtils.toList(repositoryManager.getServerRepository().findAll().iterator());
	}


	@Override
	public PaginatedTableResponse<Server> getServers(String matching, String orderBy, String orderDirection,
			int startIndex, int length) {
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

		logger.debug("Search Servers:: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + servers.size());

		return getPaginatedTableResponse(servers != null ? servers : new ArrayList<Server>(), total, filtered);
	}

	@Transactional
	@Override
	public void saveServer(Server server) {
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
		return repositoryManager.getServerRepository().findOne(serverId);
	}

	@Override
	public List<Table> getAllTables() {
		//return IteratorUtils.toList(repositoryManager.getServerRepository().findAll().iterator());
		return IteratorUtils.toList(repositoryManager.getTableRepository().findAll().iterator());

	}

	@Override
	public PaginatedTableResponse<Table> getTablesForServer(Integer serverIdParam,
			String matching, String orderBy, String orderDirection,
			int startIndex, int length) {

		logger.debug("Param ServerId: " + serverIdParam);
		logger.debug("getTablesForServer");

		Server server = null;
		List<Table> tables = null;
		long total = 0;
		long filtered = 0;

		server = repositoryManager.getServerRepository().findOne(serverIdParam);
		int serverId = server.getId();
		//TODO if(null == server)

		//empty search, select all tables for server
		if (StringUtils.isBlank(matching)) {
			total = repositoryManager.getTableRepository().countTablesForServer(serverId);
			logger.debug("Total, no search:" + total);
			filtered = total;

			logger.debug("findALL...getPagingRequest():");
			tables = repositoryManager.getTableRepository().findAll(getPagingRequest(orderBy, orderDirection, startIndex, length, total)).getContent();
			logger.debug("tables found: " + tables.size());
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			total = repositoryManager.getTableRepository().countTablesForServer(serverId);
			logger.debug("Total, with search:" + total);
			filtered = repositoryManager.getTableRepository().getCountMatching(match, serverId);

			tables = repositoryManager.getTableRepository().getMatchingTables(
					match,serverId,
					getPagingRequest(orderBy, orderDirection, startIndex, length, total));

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

	@Override
	public List<User> getAllUsers() {
		return IteratorUtils.toList(repositoryManager.getUserRepository().findAll().iterator());

	}

	@Override
	public List<User> getAllActiveUsers() {
		return IteratorUtils.toList(repositoryManager.getUserRepository().findByActiveTrue().iterator());
	}

	@Override
	public PaginatedTableResponse<User> getUsers(String matching,
			String orderBy, String orderDirection, int startIndex, int length) {
		logger.debug("*** getUsers()");
		List<User> users = null;
		long total = 0;
		long filtered = 0;

		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getUserRepository().count();
			filtered = total;
			logger.debug("Total, no search:" + total);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			Page<User> userPages = repositoryManager.getUserRepository().findAll(pagingRequest);
			users = userPages.getContent();
		} else{
			String match = "%" + matching.trim().toUpperCase() + "%";
			total = repositoryManager.getUserRepository().getCountMatching(match);
			logger.debug("Total, with search:" + total);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			users = repositoryManager.getUserRepository().getMatchingUsers(match, pagingRequest);
		}
		logger.debug("Search Users: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + users.size());

		return getPaginatedTableResponse( users != null ? users : new ArrayList<User>(), total, filtered);

	}


	@Override
	public List<User> findByUserNameIgnoreCase(String userName) {
		return 	IteratorUtils.toList(repositoryManager.getUserRepository().findByUserNameIgnoreCase(userName).iterator());

	}


	@Transactional
	@Override
	public void saveUser(User user) {
		repositoryManager.getUserRepository().save(user);

	}

	@Transactional
	@Override
	public List<User> saveUsers(List<User> userList) {
		return IteratorUtils.toList(repositoryManager.getUserRepository().save(userList).iterator());		
	}

	@Transactional
	@Override
	public void deleteUser(int id) {
		repositoryManager.getUserRepository().delete(id);

	}

	/*COLUMN section*/

	@Override
	public List<Column> getAllColumns() {
		return IteratorUtils.toList(repositoryManager.getColumnRepository().findAll().iterator());
	}


	@Transactional
	@Override
	public List<Column> saveColumns(List<Column> columnList) {
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

	/*paginated Columns for table, special case for ADMIN, see: GdmaAdmin.getColumnsForTable*/
	@Override
	public PaginatedTableResponse<Column> getColumnsForTable(Integer tableId, String matching, String orderBy, String orderDirection,
			int startIndex, int length) {
		logger.info("getColumnsForTable : " + tableId);

		/*TODO as precondition, add logic from : public Set<Column> getColumnsForTable(Long serverId, Long tableId) {*/
		/*
		Server server = gdmaFacade.getServerDao().get(serverId);
        Table table = gdmaFacade.getTableDao().get(tableId);

        serverUtil.resyncColumnList(server, table); //SYNCH , after synch return PAGINATED table of ACTIVE columns for Table that was synched and saved
		 */

		Table table = null;
		List<Column> columns = null;
		long total = 0;
		long filtered = 0;

		table = repositoryManager.getTableRepository().findOne(tableId);
		//TODO if(null == table)

		if(StringUtils.isBlank(matching)){
			total = repositoryManager.getColumnRepository().countColumnsForTable(table.getId());
			logger.debug("Total count columns for table, no search:" + total);
			filtered = total;
			logger.debug("findALL...getPagingRequest():");
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			Page<Column> columnPages = repositoryManager.getColumnRepository().findAll(pagingRequest);
			columns = columnPages.getContent();
			logger.debug("columns found: " + (null != columns ? columns.size() : "0"));
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";

			total = repositoryManager.getColumnRepository().countColumnsForTable(table.getId());
			logger.debug("Total count columns for table, with search:" + total);
			filtered = repositoryManager.getColumnRepository().getCountMatching(match);
			logger.debug("filtered : " + filtered + ", for match: " + match);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			columns = repositoryManager.getColumnRepository().getMatchingColumns(match, pagingRequest);

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

	/*paginated table of UserAccess by id or user.userName*/
	@Override
	public PaginatedTableResponse<UserAccess> getUserAccessForTable(
			Integer tableId, String matching, String orderBy,String orderDirection, int startIndex, int length) {

		logger.info("getUserAccessForTable with id: " + tableId);

		//TODO see complete impl of GdmaAdminAjaxFacade.getAccessListForTable(Long tableId) add make complete logic  */ 
		/* load table and all users, iterrate over each user
		 * if 	userAccess for(tableId, userId) does not exist 
		 *      create one and set default flags to false
		 *      set if to parent and save if (maybe remove Bidirect...)
		 *      add to list   
		 *   else 
		 *      set to parent if needed
		 *      add to list
		 *  
		 * end: userAccess now exist for tableId and each user     
		 * 
		 */

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

			//Page<UserAccess> userAccessPages = repositoryManager.getUserAccessRepository().findAll(pagingRequest);
			//Page<UserAccess> userAccessPages = repositoryManager.getUserAccessRepository().findPaginatedUserAccessByTable(tableId, pagingRequest);
			//userAccessListForTable = userAccessPages.getContent();

			userAccessListForTable  = repositoryManager.getUserAccessRepository().findPaginatedUserAccessByTable(tableId, pagingRequest);
			logger.debug("userAccess entries found: " + (null != userAccessListForTable ? userAccessListForTable.size() : "0"));
		} else {
			String match = "%" + matching.trim().toUpperCase() + "%";
			total = repositoryManager.getUserAccessRepository().countUserAccessForTable(table.getId());
			logger.debug("Total count UserAccess for table, with search:" + total);
			filtered = repositoryManager.getUserAccessRepository().getCountMatching(match);
			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			userAccessListForTable =  repositoryManager.getUserAccessRepository().getMatchingUserAccesses(match, pagingRequest);

		}

		logger.debug("Search UserAccess: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Count: " + ((userAccessListForTable != null) ? userAccessListForTable.size() : "0"));

		return getPaginatedTableResponse(userAccessListForTable, total, filtered);


	}

	@Transactional
	@Override
	public void saveUserAccess(UserAccess userAccess) {
		repositoryManager.getUserAccessRepository().save(userAccess);

	}

	@Transactional
	@Override
	public void deleteUserAccess(Integer id) {
		repositoryManager.getUserAccessRepository().delete(id);
	}

	/**
	 * for URL 
	 * http://localhost:8080/gdma2/rest/server/metadata/4
	 * 
	 * gets all tables and related columns for server = 4, 
	 * using metadata creates Tables and Columns in metadata DB
	 * 
	 * CONSIDER:
	
	 1. One DB Server can contain many DBs, have this in mind when registering URL in Column: 	server_gdma2.url
	  	(dbname 'gdma20' as part of DB URL : "jdbc:pgsql://localhost:5432/gdma20" stored in Server table)
	 
	 2. "ConnectionType" is used per each server to get DB vendor specific "SHOW tables" SQL
	 
	 3. User registered in Server DB table with: 
	 	server_gdma2.username
		server_gdma2.password
 		must have 'SHOW tables' rights invoked (Column : connection_types_gdma2.select_get_tables)
 		
	 5. Using proper DriverClass from ConnectioType user connects to server (Column : connection_types_gdma2.connection_class)  
	 
	 	Using this registered data from Server and ConnectionType - DataSource is created and JdbcTemplate using it
	 	 
	 6. Once connected user needs to execute 'SHOW TABLES' SQL and obtain Set of DB Table names from remote DB server
	 
	 7. Then iteration over Set of Table Name and using ResultSet to get 'Metadata' on DB Columns 
	 
	 8. Creating Set of Columns per each Table with specifics : PK or not, Size, ...othe constraints. 
	 
	 10. Iterate over Table Name set. Save Table to Medatada DB, then use saved Table to save Columns (TODO consider TRANSATION ) 
  
	 
	 * 	 */
	@Override
	public void getTablesMetadataForServerServer(Integer serverId) {
		logger.info("getTablesMetadataForServerServer");

		Server server = repositoryManager.getServerRepository().findOne(serverId);
		DataSource dataSourceForServer = ServerUtils.getDataSourceForServer(server);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceForServer);

		String sqlGetTables = server.getConnectionType().getSqlGetTables(); /*SHOW TABLES*/
		List<String> tableNames = ServerUtils.getSqlGetTables(jdbcTemplate, sqlGetTables);


		for (String tableName : tableNames) {
			Set<Column> tableColumns = ServerUtils.getTableColumns(jdbcTemplate, tableName);

			//persist new Table Entity with Set<Column>
			Table table = new Table();
			table.setServer(server);
			table.setName(tableName);
			table.setAlias(tableName);
			table.setColumns(tableColumns);
			//TODO table.setActive(active); SET default TRUE/FALSE in Entity itself

			//transactional //TODO MAKE COMPLETE OPERATION TRANSACTIONAL - if saving columns fails, tables is still persisted!!!
			//SAVE table first so tableId can be used when saving columns - TODO CONSIDER CASCADING !!
			Table savedTable = saveTable(table); //
			//TODO TEST DOUBLE SAVE !!! should tableName or (serverName, table name) be unique?

			//persist columns
			for (Column column : tableColumns) {
				column.setTable(savedTable);
			}
			List<Column> columnList = new ArrayList<Column>(tableColumns);
			saveColumns(columnList);

		}

	}


}
