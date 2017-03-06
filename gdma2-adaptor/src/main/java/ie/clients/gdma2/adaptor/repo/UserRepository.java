package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.User;

import org.springframework.data.repository.PagingAndSortingRepository;

/*
public interface UserDao {

    public List get(); //HERE MAPPED TO findByActiveTrue()

    public User get(Long id); // i Retrieves an entity by its id: HERE INHERITED : 	public User findOne(Integer paramID);

    public User get(String username); //  findByUserNameIgnoreCase(String userName);

    public void save(List<User> users); //	INHERITED from CRUD:  save(Iterable<S> entities) 

    public void delete(User user); //INHERITED

    public List<User> getAccess(Long tableId);

    public List<User> getCannotAccess(Long tableId); }
*/

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

	/*find all ACTIVE=true users*/
	public Iterable<User> findByActiveTrue();
	
	/*find by userName - will not be called by REST client but authentication services*/
	/*TODO post execution: also check is user is ACTIVE - or combine e.g. findByUserNameIgnoreCaseAndActiveTrue()*/
	public Iterable<User> findByUserNameIgnoreCase(String userName);

	
}
