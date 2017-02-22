package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author RGILL
 * 
 * This class is an Audit Record
 */
@Entity
@javax.persistence.Table(name = "audit_records")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "audit_records_id_seq", allocationSize = 1)
public class AuditRecord extends BaseEntity{
	@javax.persistence.Column(name = "audit_header_id")
    private Long auditHeaderID;
	@javax.persistence.Column(name = "column_id")
    private Long columnID;
	@javax.persistence.Column(name = "old_value")
    private String oldValue;
	@javax.persistence.Column(name = "new_value")
    private String newValue;

    /**
     * @return Returns the auditHeaderID.
     */
    public Long getAuditHeaderID() {
        return auditHeaderID;
    }

    /**
     * @param auditHeaderID
     *            The auditHeaderID to set.
     */
    public void setAuditHeaderID(Long auditHeaderID) {
        this.auditHeaderID = auditHeaderID;
    }

    /**
     * @return Returns the columnID.
     */
    public Long getColumnID() {
        return columnID;
    }

    /**
     * @param columnID
     *            The columnID to set.
     */
    public void setColumnID(Long columnID) {
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
