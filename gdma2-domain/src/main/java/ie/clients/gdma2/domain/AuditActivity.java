package ie.clients.gdma2.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author RGILL
 * 
 *         This class is an Audit Header record
 */
@Entity
@javax.persistence.Table(name = "AUDIT_ACTIVITY_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_AUDIT_ACTIVITY_GDMA2", allocationSize = 1)
public class AuditActivity extends BaseEntity{

	/*activity performed */
	@javax.persistence.Column(name = "ACTIVITY", length=2048)
    private String activity;

	/*activity performed from client IP address*/
	@javax.persistence.Column(name = "ACTIVITY_FROM")
    private String activityFrom;

	/*user who performed activity - is our domain user not spring one*/
	@javax.persistence.Column(name = "ACTIVITY_BY")
    private String activityBy;

	/*date of activity, usually current java.util.Date */
	@javax.persistence.Column(name = "ACTIVITY_ON")
    private Date activityOn;

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getActivityFrom() {
		return activityFrom;
	}

	public void setActivityFrom(String activityFrom) {
		this.activityFrom = activityFrom;
	}

	public String getActivityBy() {
		return activityBy;
	}

	public void setActivityBy(String activityBy) {
		this.activityBy = activityBy;
	}

	public Date getActivityOn() {
		return activityOn;
	}

	public void setActivityOn(Date activityOn) {
		this.activityOn = activityOn;
	}


}
