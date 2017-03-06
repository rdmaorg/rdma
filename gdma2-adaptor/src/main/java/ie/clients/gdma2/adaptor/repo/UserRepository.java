package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.User;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer>{

}
