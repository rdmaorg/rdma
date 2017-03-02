/*
 * Created on 14-Mar-2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author Ronan Gill
 * 
 *         14-Mar-2004
 */
@Entity
@javax.persistence.Table(name = "CONNECTION_TYPES_GDMA2")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_CONNECTION_TYPES_GDMA2", allocationSize = 1)
public class ConnectionType extends BaseEntity {

	@javax.persistence.Column(name = "NAME")
	private String name;

	@javax.persistence.Column(name = "SELECT_GET_TABLES")
	private String SQLGetTables;

	@javax.persistence.Column(name = "CONNECTION_CLASS")
	private String connectionClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSQLGetTables() {
		return SQLGetTables;
	}

	public void setSQLGetTables(String getTables) {
		SQLGetTables = getTables;
	}

	public String getConnectionClass() {
		return connectionClass;
	}

	public void setConnectionClass(String connectionClass) {
		this.connectionClass = connectionClass;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof ConnectionType))
			return false;
		final ConnectionType that = (ConnectionType) other;
		return this.name.equals(that.getName());
	}

	public int hashCode() {
		return name.hashCode();
	}

}
