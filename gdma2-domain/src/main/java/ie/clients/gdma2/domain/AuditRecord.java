package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author RGILL
 * 
 * This class is an Audit Record
 */
@Entity
@javax.persistence.Table(name = "AUDIT_RECORD_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_AUDIT_RECORD_GDMA2", allocationSize = 1)
public class AuditRecord extends BaseEntity{
	
	/*TODO openQ use AuditHeader object instead od ID?*/
	@ManyToOne
	@JoinColumn(name = "AUDIT_HEADER_ID")
    private AuditHeader auditHeader;
	
	/*TODO openQ use Column object instead od ID?*/
	@javax.persistence.Column(name = "COLUMN_ID")
    private Integer columnID;
	
	@javax.persistence.Column(name = "OLD_VALUE")
    private String oldValue;
	
	@javax.persistence.Column(name = "NEW_VALUE")
    private String newValue;
	
    /**
     * @return Returns the auditHeaderID.
     */
    public AuditHeader getAuditHeader() {
        return auditHeader;
    }

    /**
     * @param auditHeaderID
     *            The auditHeaderID to set.
     */
    public void setAuditHeader(AuditHeader auditHeader) {
        this.auditHeader = auditHeader;
    }

    /**
     * @return Returns the columnID.
     */
    public Integer getColumnID() {
        return columnID;
    }

    /**
     * @param columnID
     *            The columnID to set.
     */
    public void setColumnID(Integer columnID) {
        this.columnID = columnID;
    }

    /**
     * @return Returns the newValue.
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * @param newValue
     *            The newValue to set.
     */
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    /**
     * @return Returns the oldValue.
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * @param oldValue
     *            The oldValue to set.
     */
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
}
