package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.AuditRecord;

public interface AuditRecordRepository extends PagingAndSortingRepository<AuditRecord, Integer> {

	
	@Query("select count(ar) from AuditRecord ar where upper(ar.columnID.name) like ?1 "
			+ " or upper(ar.oldValue) like ?1 "
			+ " or upper(ar.newValue) like ?1 "
			+ " or upper(''||ar.auditHeader.tableID.name) like ?1 "
			+ " or upper(ar.auditHeader.type) like ?1 "
			+ " or upper(''||ar.auditHeader.modifiedBy) like ?1 "
			+ " or upper(''||ar.auditHeader.modifiedOn) like ?1 ")
	public long getCountMatching(String match);
	
	@Query("select ar from AuditRecord ar where upper(ar.columnID.name) like ?1 "
			+ " or upper(ar.oldValue) like ?1 "
			+ " or upper(ar.newValue) like ?1 "
			+ " or upper(''||ar.auditHeader.tableID.name) like ?1 "
			+ " or upper(ar.auditHeader.type) like ?1 "
			+ " or upper(''||ar.auditHeader.modifiedBy) like ?1 "
			+ " or upper(''||ar.auditHeader.modifiedOn) like ?1 ")
	public List<AuditRecord> getMatchingAuditRecords(String match, Pageable pageable);
	
}
