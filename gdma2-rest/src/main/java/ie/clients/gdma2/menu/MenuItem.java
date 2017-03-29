package ie.clients.gdma2.menu;

import java.util.LinkedList;
import java.util.List;

public class MenuItem {

	private String name;

	private String view;

	private String directUrl;

	private String authority;

	private List<MenuItem> children;

	public MenuItem name(String name) {
		this.name = name;
		return this;
	}

	public MenuItem view(String view) {
		this.view = view;
		return this;
	}

	public MenuItem authority(String authority) {
		this.authority = authority;
		return this;
	}

	public MenuItem directUrl(String directUrl) {
		this.directUrl = directUrl;
		return this;
	}

	public MenuItem child(MenuItem child) {
		if (children == null) {
			children = new LinkedList<MenuItem>();
		}
		children.add(child);
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}

	public String getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

}
