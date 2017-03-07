package ie.clients.gdma2.adaptor;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;
import ie.clients.gdma2.domain.User;
import ie.clients.gdma2.domain.ui.PaginatedTableResponse;
import ie.clients.gdma2.spi.interfaces.MetaDataService;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Server> getAllServers() {
		return IteratorUtils.toList(repositoryManager.getServerRepository().findAll().iterator());
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
	public PaginatedTableResponse<Table> getTablesForServer(Integer serverId,
			String matching, String orderBy, String orderDirection,
			int startIndex, int length) {

		logger.debug("Param ServerId: " + serverId);
		logger.debug("getTablesForServer");

		//Article article = serviceFacade.getCmsService().getArticleById(id);
		Server server = null;
		List<Table> tables = null;
		long total = 0;
		long filtered = 0;

		server = repositoryManager.getServerRepository().findOne(serverId);
		//TODO if(null == server)

		//empty search, select all tables for server
		if (StringUtils.isBlank(matching)) {
			total = repositoryManager.getTableRepository().countTablesForServer(server.getId());
			logger.debug("Total, no search:" + total);
			filtered = total;

			logger.debug("findALL...getPagingRequest():");
			/*
			To get paging in your query methods, you must change the signature of your query methods to accept a Pageable 
			as a parameter and return a Page<T> rather than a List<T>.

			PageRequest pagingRequest = getPagingRequest(orderBy, orderDirection, startIndex, length, total);
			Page<Table> tablesPage = repositoryManager.getTableRepository().findAll(pagingRequest);
			tables = tablesPage.getContent();
			 */ 

			tables = repositoryManager.getTableRepository().findAll(getPagingRequest(orderBy, orderDirection, startIndex, length, total)).getContent();
			logger.debug("tables found: " + tables.size());
		} else {
			total = repositoryManager.getTableRepository().countTablesForServer(server.getId());
			logger.debug("Total, with search:" + total);
			filtered = repositoryManager.getTableRepository().getCountMatching("%" + matching.trim().toUpperCase() + "%");

			tables = repositoryManager.getTableRepository().getMatchingTables(
					"%" + matching.trim().toUpperCase() + "%",
					getPagingRequest(orderBy, orderDirection, startIndex, length, total));

		}

		logger.debug("Search Tables: Search: " + matching + ", Total: " + total + ", Filtered: " + filtered
				+ ", Result Table Count: " + tables.size());

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
	public void saveTable(Table table) {
		repositoryManager.getTableRepository().save(table);
		
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

	/*Column*/
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
	
}
