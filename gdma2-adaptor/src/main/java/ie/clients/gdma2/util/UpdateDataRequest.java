package ie.clients.gdma2.util;

import java.util.ArrayList;
import java.util.List;

public class UpdateDataRequest {

	private Integer serverId;

	private Integer tableId;

	private List<List<ColumnDataUpdate>> updates = new ArrayList(new ArrayList<ColumnDataUpdate>());

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public List<List<ColumnDataUpdate>> getUpdates() {
		return updates;
	}

	public void setUpdates(List<List<ColumnDataUpdate>> updates) {
		this.updates = updates;
	}

}
