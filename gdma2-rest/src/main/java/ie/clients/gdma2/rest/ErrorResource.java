package ie.clients.gdma2.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 29/09/2015
 */
@RestController
public class ErrorResource implements ErrorController {
	private static Logger logger = LoggerFactory.getLogger(ErrorResource.class);
	private static final String ERROR_PATH = "/error";

	@Value("${application.debug}")
	private boolean debug;

	@Autowired
	private ErrorAttributes errorAttributes;

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

	@RequestMapping(ERROR_PATH)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Error handleError(HttpServletRequest request, HttpServletResponse response) {
		Throwable t = errorAttributes.getError(new ServletRequestAttributes(request));

		if (t != null) {
			logger.error(t.getMessage(), t);

			if (t.getCause() != null) {
				logger.error(t.getCause().getMessage(), t.getCause());
			}
		}

		return new Error(response.getStatus(),
				errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), debug));
	}

	private class Error {
		public int status;
		public String error;
		public String message;
		public Date timestamp;
		public String trace;

		public Error(int status, Map<String, Object> errorAttributes) {
			this.status = status;
			this.error = (String) errorAttributes.get("error");
			this.message = (String) errorAttributes.get("message");
			this.timestamp = (Date) errorAttributes.get("timestamp");
			this.trace = (String) errorAttributes.get("trace");
		}
	}
}