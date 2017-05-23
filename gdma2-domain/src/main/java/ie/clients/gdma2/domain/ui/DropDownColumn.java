package ie.clients.gdma2.domain.ui;

import java.io.Serializable;
import java.util.List;

public class DropDownColumn implements Serializable {

	private String value;
	
	private int did;
	
	private int sid;
	
	private List dropdownOptions;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getDid() {
		return did;
	}
	
	public void setDid(int did) {
		this.did = did;
	}
	
	public int getSid() {
		return sid;
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public List getDropdownOptions() {
		return dropdownOptions;
	}
	
	public void setDropdownOptions(List dropdownOptions) {
		this.dropdownOptions = dropdownOptions;
	}
	
}
