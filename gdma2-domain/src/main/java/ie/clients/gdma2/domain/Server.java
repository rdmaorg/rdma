/*
 * Created on 13-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ie.clients.gdma2.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author Ronan Gill
 */
@Entity
@javax.persistence.Table(name = "server")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "server_id_seq", allocationSize = 1)
public class Server extends BaseEntity {

	@javax.persistence.Column(name = "name")
	private String name;

	@javax.persistence.Column(name = "username")
	private String username;

	@javax.persistence.Column(name = "password")
	private String password;

	@javax.persistence.Column(name = "connection_url")
	private String connectionUrl;

	@ManyToOne
	@JoinColumn(name = "connection_type_id", nullable = false)
	private ConnectionType connectionType;

	@javax.persistence.Column(name = "prefix")
	private String prefix;

	@javax.persistence.Column(name = "active")
	private boolean active;

	@OneToMany(mappedBy="server")
	private Set<Table> tables = new LinkedHashSet<Table>();

	// the following 2 values are runtime values and are not persisted
	@Transient
	private boolean connected;

	@Transient
	private String lastError;


	public Server() {
		connected = false;
		lastError = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	public String getPrefix() {
		return prefix;
	}

	public ConnectionType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Table> getTables() {
		return tables;
	}

	public void setTables(Set<Table> tables) {
		this.tables = tables;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getLastError() {
		return lastError;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Server))
			return false;
		final Server that = (Server) other;
		return this.name.equals(that.getName());
	}

	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return "Server [connected=" + connected + ", connectionType=" + connectionType + ", connectionUrl="
				+ connectionUrl + ", id=" + getId() + ", lastError=" + lastError + ", name=" + name + ", password="
				+ password + ", prefix=" + prefix + ", tables=" + tables + ", username=" + username + "]";
	}

}
