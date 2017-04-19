package ie.clients.gdma2.menu;

import java.util.LinkedList;
import java.util.List;

public class MenuItem {

	private String name;

	private String view;

	private String directUrl;

	private String authority;
	
	private String description;
	
	private String iconClass;

	private List<MenuItem> children;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconClass() {
		return iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

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
	public MenuItem description(String description) {
		this.description = description;
		return this;
	}

	public MenuItem iconClass(String iconClass) {
		this.iconClass = iconClass;
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
