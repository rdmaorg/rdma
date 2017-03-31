package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Column;
import ie.clients.gdma2.domain.Table;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
	
	
	
	
	
	/*find all columns for table*/
	public Set<Column> findByTableId(int tableId);
	
	/*find ACTIVE columns for table*/
	public List<Column> findByTableIdAndActiveTrue(Integer tableId);

	/*count all columns for table*/
	@Query("select count(c) from Column c where c.table.id = ?1")
	public long countColumnsForTable(Integer id);

	/*PAGINATED columns*/
	@Query("select count(c) from Column c where upper(c.name) like ?1 or upper(c.dropDownColumnDisplay.name) like ?1 "
			+ " or upper(c.dropDownColumnStore.name) like ?1 or upper(c.special) like ?1 or upper(c.alias) like ?1 ")
	public long getCountMatching(String matching);
	
	@Query("select c from Column c where upper(c.name) like ?1 or upper(c.dropDownColumnDisplay.name) like ?1"
			+ " or upper(c.dropDownColumnStore.name) like ?1 or upper(c.special) like ?1 or upper(c.alias) like ?1")
	public List<Column> getMatchingColumns(String matching, Pageable pageable);

	
	/*Admin Module Section*/
	
	/*-- Count active Columns for Table : Admin module - after synch*/
	@Query("select count(c) from Column c where c.active = true and c.table.id = ?1")
	public long countActiveForTable(Integer tableId);	

	/* All Active columns for table : Admin module - after synch*/
	@Query("select c from Column c where c.active = true and c.table.id = ?1")
	public List<Column> findActiveforTable(Integer tableId, Pageable pageable);
	
	/* count Active and Matching columns for table : Admin module - after synch*/
	@Query("select count(c) from Column c where "
			+ " c.active = true "
			+ " and ( upper(c.name) like ?1 or upper(c.special) like ?1 or upper(c.alias) like ?1 )"
			+ " and c.table.id = ?2")
	public long countActiveAndMatchingForTable(String matching, Integer tableId);
	
	/* All Active and Matching columns for table : Admin module - after synch*/
	@Query("select c from Column c where "
			+ " c.active = true "
			+ " and (upper(c.name) like ?1 or upper(c.special) like ?1 or upper(c.alias) like ?1 ) "
			+ " and c.table.id = ?2")
	public List<Column> findActiveAndMatchingforTable(String matching, Integer tableId, Pageable pageable);
	
}
