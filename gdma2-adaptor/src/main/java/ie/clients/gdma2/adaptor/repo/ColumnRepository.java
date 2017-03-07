package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Column;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ColumnRepository extends PagingAndSortingRepository<Column, Integer>{

	/*
	ColumnDao
    public Column get(Long id); //used is DynamicDAO for addRecord, deleteRecords,get(paginatedRequest), updateRecords() and loading dropdowns
								//Inherited by CRUD : 				T findOne(ID id)		Retrieves an entity by its id. 
	
	public Column save(Column column);  //
										//Inherited by CRUD:		public abstract <S extends T> S save(S paramS);
	
	public void save(List<Column> columns); //	used in GdmaAdmin.saveColumns
	 										//TODO : BEFORE SAVING : applyRules(column) needs tobe IMPLEMENTED
											//Inherited by CRUD:		<S extends T> Iterable<S> save(Iterable<S> entities)
	*/
	
}
