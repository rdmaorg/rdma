package ie.clients.gdma2.web;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ie.clients.gdma2.rest.JsonWrapper;
import ie.clients.gdma2.spi.BusinessException;
import ie.clients.gdma2.spi.ServiceException;
import ie.clients.gdma2.common.core.exception.CS_ServiceUnavailableException;

@ControllerAdvice
public class GeneralControllerAdvice {
	private static final int MAX_EXCEPTION_MESSAGE_LENGTH = 256;
	private static Logger logger = Logger.getLogger(GeneralControllerAdvice.class.getName());

	@Autowired
	protected MessageSource messageSource;

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public JsonWrapper handleServiceException(ServiceException se, HttpServletResponse response) {
		logger.severe(se.getMessage());

		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

		JsonWrapper jw = new JsonWrapper("{\"error\":\"Request has not been accepted. "
				+ truncateExceptionMessage(se.getMessage()) + "\"}");

		logger.severe(jw.value());

		return jw;
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public JsonWrapper handleBusinessException(BusinessException e) {
		e.printStackTrace();
		logger.severe(e.getMessage());

		JsonWrapper jw = new JsonWrapper("{\"error\":\"" + truncateExceptionMessage(e.getMessage()) + "\"}");
		logger.severe(jw.value());
		return jw;
	}

	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public JsonWrapper handleDataIntegrityException(Exception e) {
		e.printStackTrace();
		logger.severe(e.getMessage());
		JsonWrapper jw = new JsonWrapper("{\"error\":\"This registry is currently in use and cannot be deleted\"}");
		if(e.getMessage().toUpperCase().contains("INSERT") || e.getMessage().toUpperCase().contains("UPDATE")){
			jw = new JsonWrapper("{\"error\":\"This operation has a data integrity error and cannot be executed\"}");
		}
		
		logger.severe(jw.value());
		return jw;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public JsonWrapper handleException(Exception e) {
		e.printStackTrace();
		logger.severe(e.getMessage());

		JsonWrapper jw = new JsonWrapper("{\"error\":\"" + truncateExceptionMessage(e.getMessage()) + "\"}");
		logger.severe(jw.value());
		return jw;
	}
	
	private String truncateExceptionMessage(String message) {
		if (message != null) {
			message = message.replaceAll("\\p{Cntrl}", "");
			if (message.length() > MAX_EXCEPTION_MESSAGE_LENGTH) {
				message = message.substring(0, MAX_EXCEPTION_MESSAGE_LENGTH);
				int lastSpace = message.lastIndexOf(" ");
				if (lastSpace > 10)
					message = message.substring(0, lastSpace);
				message = message + " ... ";
			}

			return message;
		}

		return "Unknown Error Received.";
	}

	@ExceptionHandler(CS_ServiceUnavailableException.class)
	@ResponseBody
	public JsonWrapper handleCS_ServiceUnavailableException(CS_ServiceUnavailableException se, HttpServletResponse response) {
		logger.severe(se.getMessage());

		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

		JsonWrapper jw = new JsonWrapper("{\"error\": "
				+ truncateExceptionMessage(se.getMessage()) + "\"}");

		logger.severe(jw.value());

		return jw;
	}
	
	


}
