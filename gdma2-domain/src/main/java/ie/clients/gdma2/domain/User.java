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
@javax.persistence.Table(name = "user")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "user_id_seq", allocationSize = 1)
public class User extends BaseEntity {

	@javax.persistence.Column(name = "first_name")
    private String firstName;

	@javax.persistence.Column(name = "last_name")
    private String lastName;

	@javax.persistence.Column(name = "user_name")
    private String userName;

	@javax.persistence.Column(name = "domain")
    private String domain;

	@javax.persistence.Column(name = "admin")
    private boolean admin;

	@javax.persistence.Column(name = "locked")
    private boolean locked;

	@javax.persistence.Column(name = "active")
    private boolean active;

	@OneToMany(mappedBy="user")
    private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();
    
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
