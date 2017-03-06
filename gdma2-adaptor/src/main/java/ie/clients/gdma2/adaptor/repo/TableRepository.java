package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import ie.clients.gdma2.domain.Server;
import ie.clients.gdma2.domain.Table;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TableRepository extends PagingAndSortingRepository<Table, Integer>{

	/*
	@Override
	public Iterable<Table> findAll();
	 */
	/*help:
	 *
	 * https://github.com/spring-projects/spring-data-examples/blob/master/jpa/example/src/main/java/example/springdata/jpa/simple/SimpleUserRepository.java
	 * http://stackoverflow.com/questions/10696490/does-spring-data-jpa-have-any-way-to-count-entites-using-method-name-resolving
	 * 
	 * https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-seven-pagination/
	 * 
	 * Example Queries
	http://docs.oracle.com/javaee/6/tutorial/doc/bnbtl.html
	http://docs.oracle.com/javaee/6/tutorial/doc/bnbuf.html

	 * */

	public List<Table> findByServerId(int serverId);

	@Query("select count(t) from Table t where t.server.id = ?1")
	public long countTablesForServer(Integer serverId);

	/*matching = search term, search in all columns of data table on UI*/
	@Query("select count(t) from Table t where upper(t.name) like ?1 or upper(t.server.name) like ?1")
	public long getCountMatching(String matching);

	@Query("select t from Table t where upper(t.name) like ?1 or upper(t.server.name) like ?1")
	public List<Table> getMatchingTables(String matching, Pageable pageable);

	
	
}
