package ie.clients.gdma2.adaptor.repo;

import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.AuditActivity;

public interface AuditActivityRepository extends PagingAndSortingRepository<AuditActivity, Integer> {

}
