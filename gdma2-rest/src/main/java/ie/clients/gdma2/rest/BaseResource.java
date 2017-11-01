package ie.clients.gdma2.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import ie.clients.gdma2.spi.ServiceFacade;

public abstract class BaseResource {
	@Value("${application.name}")
	protected String applicationName;

	@Autowired
	protected ServiceFacade serviceFacade;
	
	@Autowired(required=false)
	private HttpServletRequest req;
	
	protected void logActivity(String activity){
		serviceFacade.getLoggingService().log((req!=null?req.getRemoteAddr():""), activity);
	}
	
	
}
