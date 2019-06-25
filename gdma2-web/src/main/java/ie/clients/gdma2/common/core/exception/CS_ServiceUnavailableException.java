package ie.clients.gdma2.common.core.exception;

public class CS_ServiceUnavailableException extends CS_BaseException {

	private static final long serialVersionUID = 5167349627685241916L;

	public CS_ServiceUnavailableException() {
		super();
	}

	public CS_ServiceUnavailableException(String errCode, String errHeader, String errMsg) {
		super(errCode, errHeader, errMsg);
	}

	public CS_ServiceUnavailableException(String errMsg) {
		super(errMsg);
	}

	public CS_ServiceUnavailableException(StringBuilder errMsg) {
		super(errMsg);
	}

	
}