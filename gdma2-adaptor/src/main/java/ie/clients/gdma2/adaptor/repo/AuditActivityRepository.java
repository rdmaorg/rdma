package ie.clients.gdma2.adaptor.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import ie.clients.gdma2.domain.AuditActivity;

public interface AuditActivityRepository extends PagingAndSortingRepository<AuditActivity, Integer> {

	@Query("select count(aa) from AuditActivity aa where upper(''||aa.activity) like ?1 "
			+ " or upper(aa.activityFrom) like ?1 "
			+ " or upper(aa.activityBy) like ?1 "
			+ " or upper(''||aa.activityOn) like ?1 ")
	public long getCountMatching(String match);
	
	@Query("select aa from AuditActivity aa where upper(''||aa.activity) like ?1 "
			+ " or upper(aa.activityFrom) like ?1 "
			+ " or upper(aa.activityBy) like ?1 "
			+ " or upper(''||aa.activityOn) like ?1 ")
	public List<AuditActivity> getMatchingAuditActivity(String match, Pageable pageable);
	
}
