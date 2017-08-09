package ie.clients.gdma2.adaptor.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.AuditRecord;

public interface AuditRecordRepository extends PagingAndSortingRepository<AuditRecord, Integer> {

}
