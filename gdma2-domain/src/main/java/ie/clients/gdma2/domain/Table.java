package ie.clients.gdma2.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author RGILL
 * 
 */
@Entity
@javax.persistence.Table(name = "TABLE_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_TABLE_GDMA2", allocationSize = 1)
public class Table extends BaseEntity {

	@javax.persistence.Column(name = "NAME")
	private String name;

	@javax.persistence.Column(name = "ACTIVE")
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "SERVER_ID", nullable = false)
	private Server server;

	/*TODO REMOVE this we need LINK table with extra columns
	/*
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "TABLE_HAS_USERS_GDMA2", joinColumns = {
			@JoinColumn(name = "TABLE_ID", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
	private Set<User> users = new LinkedHashSet<User>();
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
	*/

	//@OneToMany(mappedBy="table")
	@javax.persistence.Transient
	private Set<Column> columns = new LinkedHashSet<Column>();

	//@OneToMany(mappedBy="table")
	@javax.persistence.Transient
	private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();



	public Set<UserAccess> getUserAccess() {
		return userAccess;
	}

	public void setUserAccess(Set<UserAccess> userAccess) {
		this.userAccess = userAccess;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Set<Column> getColumns() {
		return columns;
	}

	public void setColumns(Set<Column> columns) {
		this.columns = columns;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Table))
			return false;
		final Table that = (Table) other;
		return this.name.equals(that.getName());
	}

	public int hashCode() {
		return name.hashCode();
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
