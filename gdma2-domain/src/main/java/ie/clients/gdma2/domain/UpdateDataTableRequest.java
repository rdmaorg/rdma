package ie.clients.gdma2.domain;

import java.util.List;
import java.util.Map;

public class UpdateDataTableRequest {

	private String action;

	private List<Map<String,String>> data;
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public List<Map<String, String>> getData() {
		return data;
	}
	
	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}
}
