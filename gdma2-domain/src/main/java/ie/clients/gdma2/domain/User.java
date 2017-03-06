package ie.clients.gdma2.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author rgill
 *
 */
@Entity
@javax.persistence.Table(name = "USERS_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_USERS_GDMA2", allocationSize = 1)
public class User extends BaseEntity {

	@javax.persistence.Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

	@javax.persistence.Column(name = "LAST_NAME", nullable = false)
    private String lastName;

	@javax.persistence.Column(name = "USERNAME", nullable = false)
    private String userName;

	@javax.persistence.Column(name = "USER_DOMAIN")
    private String domain;

	@javax.persistence.Column(name = "IS_ADMIN", nullable = false)
    private boolean admin;

	@javax.persistence.Column(name = "IS_LOCKED", nullable = false)
    private boolean locked;

	@javax.persistence.Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

	//@OneToMany(mappedBy="gdmaUser")
	@javax.persistence.Transient
    private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();
    
	/*
	@ManyToMany(cascade = CascadeType.MERGE, mappedBy = "users")
	private List<Table> tables;
	*/
	
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserAccess> getUserAccess() {
        return userAccess;
    }

    public void setUserAccess(Set<UserAccess> tables) {
        this.userAccess = userAccess;
    }
    
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof User))
            return false;
        final User that = (User) other;

        // aUser without a userName is invalid - so let the equals always be
        // null
        if (this.userName == null || that.getUserName() == null)
            return false;

        return this.userName.equals(that.getUserName());
    }

    public int hashCode() {
        return userName.hashCode();
    }

}
