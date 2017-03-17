package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.UniqueConstraint;

import com.avnet.cs.commons.dao.BaseEntity;

@Entity
@javax.persistence.Table(name = "USER_TABLE_ACCESS_GDMA2", uniqueConstraints = {
			@UniqueConstraint(columnNames={"USER_ID","TABLE_ID"})
		})
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_USER_TABLE_ACCESS_GDMA2", allocationSize = 1)
public class UserAccess extends BaseEntity {
	
	
	@javax.persistence.Column(name = "ALLOW_DISPLAY", nullable = false)
	private boolean allowDisplay = false;
	
	@javax.persistence.Column(name = "ALLOW_UPDATE", nullable = false)
	private boolean allowUpdate = false;
	
	@javax.persistence.Column(name = "ALLOW_INSERT", nullable = false)
	private boolean allowInsert = false;
	
	@javax.persistence.Column(name = "ALLOW_DELETE", nullable = false)
	private boolean allowDelete = false;
	
	//@Id
	//TODO ? @ManyToOne(cascade = CascadeType.ALL)
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	//TODO ? @ManyToOne(cascade = CascadeType.ALL)
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

    
	//TODO use biz key for HashCode and equals not generic
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (allowDelete ? 1231 : 1237);
		result = prime * result + (allowDisplay ? 1231 : 1237);
		result = prime * result + (allowInsert ? 1231 : 1237);
		result = prime * result + (allowUpdate ? 1231 : 1237);
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccess other = (UserAccess) obj;
		if (allowDelete != other.allowDelete)
			return false;
		if (allowDisplay != other.allowDisplay)
			return false;
		if (allowInsert != other.allowInsert)
			return false;
		if (allowUpdate != other.allowUpdate)
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAccess [allowDisplay=" + allowDisplay + ", allowUpdate="
				+ allowUpdate + ", allowInsert=" + allowInsert
				+ ", allowDelete=" + allowDelete + ", user=" + user
				+ ", table=" + table + ", getId()=" + getId() + "]";
	} 
    
	
    
    
}
