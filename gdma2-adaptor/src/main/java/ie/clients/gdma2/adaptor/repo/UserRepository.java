package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.User;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/*
public interface UserDao {

    public List get(); //HERE MAPPED TO findByActiveTrue()

    public User get(Long id); // i Retrieves an entity by its id: HERE INHERITED : 	public User findOne(Integer paramID);

    public User get(String username); //  findByUserNameIgnoreCase(String userName);

    public void save(List<User> users); //	INHERITED from CRUD:  save(Iterable<S> entities) 

    public void delete(User user); //INHERITED

    public List<User> getAccess(Long tableId); ? not used

    public List<User> getCannotAccess(Long tableId); } ? not used
*/

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

	/*find all ACTIVE=true users*/
	public Iterable<User> findByActiveTrue();
	
	/*find by userName - will not be called by REST client but authentication services*/
	/*TODO post execution: also check is user is ACTIVE - or combine e.g. findByUserNameIgnoreCaseAndActiveTrue()*/
	public Iterable<User> findByUserNameIgnoreCase(String userName);

	/* SELECT count(*) FROM public.users_gdma2 
    where UPPER(first_name) like UPPER('%an%') or UPPER(last_name) like UPPER('%an%') or UPPER(user_domain) like UPPER('%an%') or UPPER(username) like UPPER('%an%')     */
	
	//@Query("select count(u) from User u where upper(u.firstName) like ?1 or upper(u.lastName) like ?1 or upper(u.userName) like ?1 or upper(u.domain) like ?1")
	@Query("select count(u) from User u where upper(u.firstName) like ?1 or upper(u.lastName) like ?1 or upper(u.userName) like ?1 or upper(u.domain) like ?1")
	public long getCountMatching(String matching);
	
	
	/*	SELECT * FROM public.users_gdma2 
    where UPPER(first_name) like UPPER('%an%') or UPPER(last_name) like UPPER('%an%') or UPPER(user_domain) like UPPER('%an%') or UPPER(username) like UPPER('%an%')    */
	@Query("select u from User u where upper(u.firstName) like ?1 or upper(u.lastName) like ?1 or upper(u.userName) like ?1 or upper(u.domain) like ?1 ")
	public List<User> getMatchingUsers(String match, Pageable pageable);
	
	

	
	

	
}
