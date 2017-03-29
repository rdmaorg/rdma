package ie.clients.gdma2.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.clients.gdma2.domain.app.PageEnum;
import ie.clients.gdma2.menu.MenuBuilder;
import ie.clients.gdma2.menu.MenuItem;

@RestController
@RequestMapping(value = "/rest/menu")
public class ApplicationMenuResource extends BaseResource {

//	private static Logger logger = LoggerFactory.getLogger(ApplicationMenuResource.class);
	
	public static final String MENU_USER_NAME = "Avnet";
	public static final String MENU_APP_NAME = "GDMA";
	public static final String LOGGOUT_MENU = "Log Out";
	

	@RequestMapping(value = "user")
	public List<MenuItem> getUserMenu() {
		MenuBuilder b = new MenuBuilder();
		return b.add(new MenuItem().name(MENU_USER_NAME)
				.child(new MenuItem().name(PageEnum.CAS_LOGOUT.description()).view(PageEnum.CAS_LOGOUT.path()))).end();
	}
	
	
	@RequestMapping(value = "app")
	public List<MenuItem> getAppMenu() {
		MenuBuilder b = new MenuBuilder();
		MenuItem menuItem = new MenuItem().name(MENU_APP_NAME);
		for(PageEnum page : PageEnum.values()){
			menuItem.child(new MenuItem().name(page.description()).view(page.path()));
		}
		return b.add(menuItem).end();
	}
}
