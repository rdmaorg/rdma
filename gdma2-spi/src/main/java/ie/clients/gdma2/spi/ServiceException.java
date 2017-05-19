package ie.clients.gdma2.spi;

/**
 * @author Kamran Zafar (N000989)
 *         Created on 30/08/2016
 */
public class ServiceException extends RuntimeException {
	private String trace;
	private String errorCode;
	private String errorDescription;

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String errorCode, String errorDescription) {
		super(message);
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}
	
	
}
