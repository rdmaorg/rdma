package ie.clients.gdma2.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@javax.persistence.Table(name = "user_access")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "user_access_id_seq", allocationSize = 1)
public class UserAccess implements Serializable {

	private static final long serialVersionUID = -2834870515554339937L;

	@javax.persistence.Column(name = "user_id")
	private Long userId;
	
	@javax.persistence.Column(name = "table_id")
	private Long tableId;
	
	@javax.persistence.Column(name = "allow_display")
	private boolean allowDisplay = false;
	
	@javax.persistence.Column(name = "allow_update")
	private boolean allowUpdate = false;
	
	@javax.persistence.Column(name = "allow_insert")
	private boolean allowInsert = false;
	
	@javax.persistence.Column(name = "allow_delete")
	private boolean allowDelete = false;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "table_id", nullable = false)
	private Table table;
	
	public Long  getUserId() {
        return userId;
    }

    public void setUserId(Long  userId) {
        this.userId = userId;
    }
    
    public Long  getTableId() {
        return tableId;
    }

    public void setTableId(Long  tableId) {
        this.tableId = tableId;
    }
    
    public boolean getAllowDisplay() {
        return allowDisplay;
    }

    public void setAllowDisplay(boolean allowDisplay) {
        this.allowDisplay = allowDisplay;
    }
    
    public boolean getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }
    
    public boolean getAllowInsert() {
        return allowInsert;
    }

    public void setAllowInsert(boolean allowInsert) {
        this.allowInsert = allowInsert;
    }
    
    public boolean getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }   
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    } 
    
}
