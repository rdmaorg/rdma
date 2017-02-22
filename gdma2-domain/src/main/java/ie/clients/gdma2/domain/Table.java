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
@javax.persistence.Table(name = "tables")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "tables_id_seq", allocationSize = 1)
public class Table extends BaseEntity {

//	@javax.persistence.Column(name = "server_id")
//	private Long serverId;

	@javax.persistence.Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "server_id", nullable = false)
	private Server server;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "tables_has_users", joinColumns = {
			@JoinColumn(name = "table_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) })
	private Set<User> users = new LinkedHashSet<User>();

	@OneToMany(mappedBy="table")
	private Set<Column> columns = new LinkedHashSet<Column>();

	@OneToMany(mappedBy="table")
	private Set<UserAccess> userAccess = new LinkedHashSet<UserAccess>();

	@javax.persistence.Column(name = "active")
	private boolean active;

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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
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

//	public Long getServerId() {
//		return serverId;
//	}
//
//	public void setServerId(Long serverId) {
//		this.serverId = serverId;
//	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
