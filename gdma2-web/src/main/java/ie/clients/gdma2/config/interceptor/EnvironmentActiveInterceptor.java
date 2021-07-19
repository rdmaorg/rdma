package ie.clients.gdma2.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;
import ie.clients.gdma2.common.core.exception.CS_ServiceUnavailableException;


public class EnvironmentActiveInterceptor extends HandlerInterceptorAdapter {
	 
	 private static final Logger logger = LoggerFactory.getLogger(EnvironmentActiveInterceptor.class);

	 
	    @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
	    								Object handler) throws Exception {

	    	logger.trace("EnvironmentActiveInterceptor.preHandle -ENTER"); 

	    	Date today = Calendar.getInstance().getTime();
	    	Date expirationDate = null;
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	    	try {
	    		String dateString = "21-07-2023";
		    	expirationDate = sdf.parse(dateString);
		    	
	    	} catch (ParseException e) {
	    	    e.printStackTrace();
	    	}
	    	
	    	
	    	if (today.after(expirationDate)) {
    			
				throw new CS_ServiceUnavailableException("The license for this product has expired. Please contact your administrator.");					
    		}

    		logger.trace("EnvironmentActiveInterceptor.preHandle -EXIT"); 
		    return true;
	    }
	 
		@Override
	    public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,
	            						ModelAndView modelAndView) throws Exception {

	    }
	 
	    @Override
	    public void afterCompletion(HttpServletRequest request,
	            HttpServletResponse response, Object handler, Exception ex)throws Exception {

	    }

}
