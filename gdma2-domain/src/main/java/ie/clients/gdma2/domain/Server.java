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
@javax.persistence.Table(name = "SERVER_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_SERVER_GDMA2", allocationSize = 1)
public class Server extends BaseEntity {

	@javax.persistence.Column(name = "NAME", nullable = false)
	private String name;
	
	@javax.persistence.Column(name = "USERNAME", nullable = false)
	private String username;

	@javax.persistence.Column(name = "PASSWORD")
	private String password;

	@javax.persistence.Column(name = "URL")
	private String connectionUrl;

	@javax.persistence.Column(name = "PREFIX")
	private String prefix;

	@javax.persistence.Column(name = "ACTIVE")
	private boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "CONNECTION_TYPE_ID", nullable = false)
	private ConnectionType connectionType;
	
	//@OneToMany(mappedBy="server") //BIDIRECT :causing infinite loop on REST response!
	@javax.persistence.Transient
	private Set<Table> tables = new LinkedHashSet<Table>();

	/*runtime value - not persisted*/
	@Transient
	private boolean connected;

	/*runtime value - not persisted*/
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


	@Override
	public String toString() {
		return "Server [name=" + name + ", username=" + username
				+ ", connectionUrl=" + connectionUrl
				+ ", prefix=" + prefix + ", active=" + active + ","
				+ " connectionType=" + connectionType + ", tables="
				+ tables + ", connected=" + connected + ", lastError="
				+ lastError + ", getId()=" + getId() + "]";
	}

}
