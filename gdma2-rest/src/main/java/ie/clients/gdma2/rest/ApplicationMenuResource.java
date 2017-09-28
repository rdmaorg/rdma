package ie.clients.gdma2.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.clients.gdma2.domain.app.PageEnum;
import ie.clients.gdma2.domain.app.UserAuthDetail;
import ie.clients.gdma2.menu.MenuBuilder;
import ie.clients.gdma2.menu.MenuItem;

@RestController
@RequestMapping(value = "/rest/menu")
public class ApplicationMenuResource extends BaseResource {

	// private static Logger logger =
	// LoggerFactory.getLogger(ApplicationMenuResource.class);

	@RequestMapping(value = "user")
	public List<MenuItem> getUserMenu() {
		MenuBuilder b = new MenuBuilder();
		UserAuthDetail u = serviceFacade.getUserContextProvider().getLoggedInUser();
		return b.add(new MenuItem().name(u.getFirstName() + " " + u.getLastName())
				.child(new MenuItem().name(PageEnum.LOGOUT.description()).view(PageEnum.LOGOUT.path()))).end();
	}

	@RequestMapping(value = "app")
	public List<MenuItem> getAppMenu() {
		UserAuthDetail u = serviceFacade.getUserContextProvider().getLoggedInUser();
		MenuBuilder b = new MenuBuilder();
		MenuItem menuItem = new MenuItem().name(applicationName);

		if (u.isPermitted(PageEnum.ADMIN_SERVER.role())) {
			menuItem.child(new MenuItem().name(PageEnum.ADMIN_SERVER.description()).view(PageEnum.ADMIN_SERVER.path())
					.authority(PageEnum.ADMIN_SERVER.role()).description(PageEnum.ADMIN_SERVER.details())
					.iconClass(PageEnum.ADMIN_SERVER.iconClass()));
		}

		if (u.isPermitted(PageEnum.ADMIN_USER.role())) {
			menuItem.child(new MenuItem().name(PageEnum.ADMIN_USER.description()).view(PageEnum.ADMIN_USER.path())
					.authority(PageEnum.ADMIN_USER.role()).description(PageEnum.ADMIN_USER.details())
					.iconClass(PageEnum.ADMIN_USER.iconClass()));
		}
		if (u.isPermitted(PageEnum.ADMIN_CONNECTIONS.role())) {
			menuItem.child(new MenuItem().name(PageEnum.ADMIN_CONNECTIONS.description())
					.view(PageEnum.ADMIN_CONNECTIONS.path()).authority(PageEnum.ADMIN_CONNECTIONS.role())
					.description(PageEnum.ADMIN_CONNECTIONS.details())
					.iconClass(PageEnum.ADMIN_CONNECTIONS.iconClass()));
		}
		if (u.isPermitted(PageEnum.ADMIN_AUDIT_LOG.role())) {
			menuItem.child(new MenuItem().name(PageEnum.ADMIN_AUDIT_LOG.description())
					.view(PageEnum.ADMIN_AUDIT_LOG.path()).authority(PageEnum.ADMIN_AUDIT_LOG.role())
					.description(PageEnum.ADMIN_AUDIT_LOG.details())
					.iconClass(PageEnum.ADMIN_AUDIT_LOG.iconClass()));
		}
		if (u.isPermitted(PageEnum.ADMIN_ACTIVITY_LOG.role())) {
			menuItem.child(new MenuItem().name(PageEnum.ADMIN_ACTIVITY_LOG.description())
					.view(PageEnum.ADMIN_ACTIVITY_LOG.path()).authority(PageEnum.ADMIN_ACTIVITY_LOG.role())
					.description(PageEnum.ADMIN_ACTIVITY_LOG.details())
					.iconClass(PageEnum.ADMIN_ACTIVITY_LOG.iconClass()));
		}

		return b.add(menuItem).end();
	}
}
