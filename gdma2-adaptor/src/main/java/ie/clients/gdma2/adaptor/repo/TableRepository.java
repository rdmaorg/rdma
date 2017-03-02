package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Table;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TableRepository extends PagingAndSortingRepository<Table, Integer>{

	/*
	@Override
	public Iterable<Table> findAll();
	*/
	
}
