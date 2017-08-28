package ie.clients.gdma2.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import ie.clients.gdma2.spi.ServiceFacade;

@Component
public class AppLogoutHandler implements ApplicationListener<HttpSessionDestroyedEvent> {
	private static Logger logger = LoggerFactory.getLogger(AppLogoutHandler.class);

	@Autowired
	private ServiceFacade serviceFacade;
	@Autowired(required = false)
	private HttpServletRequest req;

//	@EventListener({HttpSessionDestroyedEvent.class})
//	public void sessionDestroyed(HttpSessionDestroyedEvent event){
//		try {
//			if (serviceFacade != null) {
//				logger.info("+++++++++++++++++== USER LOGOUT: "
//						+ serviceFacade.getUserContextProvider().getLoggedInUserName());
//				serviceFacade.getMetadataService().log((req != null ? req.getRemoteAddr() : ""), "Logging Out.");
//			} else {
//				logger.info("ServiceFacade not autowired!");
//			}
//		} catch (Throwable t) {
//			logger.error("An error occurred in AppLogoutHandler.", t);
//		}
//		
//	}
	
	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent event) {
		HttpSession session = event.getSession();
		try {
			if (serviceFacade != null) {
				logger.info("+++++++++++++++++== USER LOGOUT: "
						+ serviceFacade.getUserContextProvider().getLoggedInUserName());
				serviceFacade.getMetadataService().log((req != null ? req.getRemoteAddr() : ""), "Logging Out.");
			} else {
				logger.info("ServiceFacade not autowired!");
			}
		} catch (Throwable t) {
			logger.error("An error occurred in AppLogoutHandler.", t);
		}

	}

}
