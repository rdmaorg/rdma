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
@javax.persistence.Table(name = "audit_header")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "audit_header_id_seq", allocationSize = 1)
public class AuditHeader extends BaseEntity{

	@javax.persistence.Column(name = "table_id")
    private Long tableID;

	@javax.persistence.Column(name = "type")
    private char type;

	@javax.persistence.Column(name = "modified_by")
    private String modifiedBy;

	@javax.persistence.Column(name = "modified_on")
    private Date modifiedOn;

    public Long getTableID() {
        return tableID;
    }

    public void setTableID(Long tableID) {
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
