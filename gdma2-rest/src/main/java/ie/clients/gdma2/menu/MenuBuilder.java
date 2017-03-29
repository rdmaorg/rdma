package ie.clients.gdma2.menu;

import java.util.LinkedList;
import java.util.List;

public class MenuBuilder {

	private List<MenuItem> menus;

	public MenuBuilder add(MenuItem menuItem) {
		if (menus == null) {
			menus = new LinkedList<MenuItem>();
		}

		menus.add(menuItem);
		return this;
	}

	public List<MenuItem> end() {
		return menus;
	}
}
