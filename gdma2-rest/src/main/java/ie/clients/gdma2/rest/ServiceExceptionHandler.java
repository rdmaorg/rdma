package ie.clients.gdma2.rest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ie.clients.gdma2.spi.ServiceFacade;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 16/10/2015
 */
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);
	
	@Autowired
	protected ServiceFacade serviceFacade;

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public JsonWrapper handleBusinessException(Throwable e) {
		logger.error(e.getMessage(), e);
		return new JsonWrapper("{\"error\":\"" + StringEscapeUtils.escapeJson(e.getMessage()) + "\"}");
	}
}
