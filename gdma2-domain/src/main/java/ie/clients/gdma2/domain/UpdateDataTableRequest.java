package ie.clients.gdma2.domain;

import java.util.List;

public class UpdateDataTableRequest {

	private String action;

	private List<List<String>> data;
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public List<List<String>> getData() {
		return data;
	}
	
	public void setData(List<List<String>> data) {
		this.data = data;
	}
	
}
