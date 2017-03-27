package ie.clients.gdma2.adaptor.repo;

import ie.clients.gdma2.domain.Column;

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
	
	
	
	/**
     * Only called from admin and it's a special case.
     * We need to re-sync the columns before calling it, just to ensure that the list is current.
     * 
     * mid step: serverUtil.resyncColumnList(server, table):  Use serverId to connect to server and get tables, resynch columns and call tableDao.save(table);  
     * 
     * once columns of a table are in a synch - get all ACTIVE columns from synched Table
     * 
     * @return
     */
     	/*
	public Set<Column> getColumnsForTable(Long serverId, Long tableId) {
        // TODO use an AOP trigger for this
        Server server = gdmaFacade.getServerDao().get(serverId);
        Table table = gdmaFacade.getTableDao().get(tableId);
        
        serverUtil.resyncColumnList(server, table); //SYNCH
        
        //GET ALL ACTIVE columns from synchronized table
         * 
        Set<Column> activeColumns = new HashSet<Column>();
        Set<Column> allColumns = table.getColumns();
        Column[] allColumnsArray = new Column[allColumns.size()];
        allColumnsArray = allColumns.toArray(allColumnsArray);
        for(int i = 0; i < allColumnsArray.length; i++){
        	if (allColumnsArray[i].isActive()) {
        		activeColumns.add(allColumnsArray[i]);
            }                 	
        }       
        return activeColumns;
    }
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

	
	
	

	
	
	
}
