package ie.clients.gdma2.adaptor.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.AuditHeader;

public interface AuditHeaderRepository extends PagingAndSortingRepository<AuditHeader, Integer> {

}
