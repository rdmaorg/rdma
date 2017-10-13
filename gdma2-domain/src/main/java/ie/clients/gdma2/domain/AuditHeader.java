package ie.clients.gdma2.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author RGILL
 * 
 *         This class is an Audit Header record
 */
@Entity
@javax.persistence.Table(name = "AUDIT_HEADER_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_AUDIT_HEADER_GDMA2", allocationSize = 1)
public class AuditHeader extends BaseEntity{

	/*TODO openQ use table object instead od ID?*/
	/*DB table being modified*/
//	@javax.persistence.Column(name = "TABLE_ID")
	@ManyToOne
	@JoinColumn(name = "TABLE_ID")
    private Table tableID;

	/*auditType - UPDATE, DELETE, ADD*/
	@javax.persistence.Column(name = "AUDIT_TYPE")
    private char type;

	/*user who modified table - is our domain user not spring one*/
	@javax.persistence.Column(name = "MODIFIED_BY")
    private String modifiedBy;

	/*date of modification, usually current java.util.Date */
	@javax.persistence.Column(name = "MODIFIED_ON")
    private Date modifiedOn;

	public Table getTableID() {
		return tableID;
	}
	
	public void setTableID(Table tableID) {
		this.tableID = tableID;
	}
	
    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

}
