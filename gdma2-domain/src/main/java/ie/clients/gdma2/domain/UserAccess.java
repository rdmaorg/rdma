package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

@Entity
@javax.persistence.Table(name = "USER_TABLE_ACCESS_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_USER_TABLE_ACCESS_GDMA2", allocationSize = 1)
public class UserAccess extends BaseEntity {
	
	/*openQ instead of having PK and sequnece generator from BaseEntity, to have composite primary key (user_id, table_id):
	 * 
	 * no - @SequenceGenerator
	 * public class UserAccess implements Serializable 
	 * 
	 * put  @Id
	 * on User and Table 
	 * */
	
	@javax.persistence.Column(name = "ALLOW_DISPLAY")
	private boolean allowDisplay = false;
	
	@javax.persistence.Column(name = "ALLOW_UPDATE")
	private boolean allowUpdate = false;
	
	@javax.persistence.Column(name = "ALLOW_INSERT")
	private boolean allowInsert = false;
	
	@javax.persistence.Column(name = "ALLOW_DELETE")
	private boolean allowDelete = false;
	
	//@Id
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	//@Id
	@ManyToOne
	@JoinColumn(name = "TABLE_ID", nullable = false)
	private Table table;
    
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
