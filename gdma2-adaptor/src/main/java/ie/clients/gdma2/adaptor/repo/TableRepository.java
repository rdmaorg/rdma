package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface TableRepository extends PagingAndSortingRepository<Table, Integer>{

	/*help:
	 *
	 * https://github.com/spring-projects/spring-data-examples/blob/master/jpa/example/src/main/java/example/springdata/jpa/simple/SimpleUserRepository.java
	 * http://stackoverflow.com/questions/10696490/does-spring-data-jpa-have-any-way-to-count-entites-using-method-name-resolving
	 * https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-seven-pagination/
	 * */

	/*TESTING PURPOSE*/
	/*find by ServerId and Active - not pageable*/
	public List<Table> findByServerIdAndActiveTrue(Integer serverId);
	public List<Table> findByServerId(int serverId);

	
	
	@Query("select count(t) from Table t where t.server.id = ?1")
	public long countTablesForServer(Integer serverId);
	
	/*find by ServerId - pageable*/
	public List<Table> findByServerId(int serverId, Pageable pageable);

	
	
	
	/*TODO not active - DELETE if not needed!*/
	/*matching = search term, search in all columns of data table on UI*/
	@Query("select count(t) from Table t "
			+ " where "
			+ " (upper(t.name) like ?1 or upper(t.alias) like ?1 or upper(t.server.name) like ?1 )"
			+ " and t.server.id = ?2")
	public long getCountMatching(String matching, Integer serverId); //TODO pass serverId

	/*TODO not active - DELETE if not needed!*/
	@Query("select t from "
			+ " Table t where "
			+ " (upper(t.name) like ?1  or upper(t.alias) like ?1 or upper(t.server.name) like ?1 )"
			+ "  and t.server.id = ?2")
	public List<Table> getMatchingTables(String matching, Integer serverId, Pageable pageable);

	
	
	
	/*--ACTIVE TABLES for Admin module - after synch, on local */
	@Query("select count(t) from Table t where t.active = true and t.server.id = ?1")
	public long countActiveTablesForServer(Integer serverId);
	
	/*active Tables for server - Pageable - after synch, on local */
	@Query("select t from Table t where t.active= true and t.server.id = ?1")
	public List<Table> getActivePagableTables(Integer serverId, Pageable pageable);
	//public List<Table> findByServerIdAndActiveTrue(int serverId, Pageable pageable); works too
	
	/*active Tables for server with search - Pageable - after synch, on local */
	@Query("select t "
			+ " from Table t "
			+ " where t.active = true "
			+ " and (upper(t.name) like ?1  or upper(t.alias) like ?1 or upper(t.server.name) like ?1 )"
			+ " and t.server.id = ?2")
	public List<Table> getActiveMatchingTables(String matching, Integer serverId, Pageable pageable);

	
	

	
	
	/*DATA MODULE section*/

	@Query("select ua.table from UserAccess ua "
			+ " where UPPER(ua.user.userName) = UPPER(:userName) "
			+ " and ua.allowDisplay = TRUE "
			+ " and ua.table.active = TRUE "
			+ " and ua.table.server.active = TRUE"
			+ " and ua.table.server.id  = :serverId ")
	public List<Table> activeTablesOnActiveServerForUser(@Param("userName") String userName, @Param("serverId") Integer serverId);

	/*part of getDropDown logic - get table for colulmn*/
	@Query("select distinct c.table from Column c "
			+"  where c.table.active = TRUE "
			+ " and c.id = ?1")
	public Table activeTableForColumn(Integer columnId);
	
		
}

