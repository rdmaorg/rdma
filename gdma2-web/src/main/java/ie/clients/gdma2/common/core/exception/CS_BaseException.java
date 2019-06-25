package ie.clients.gdma2.common.core.exception;

public abstract class CS_BaseException extends RuntimeException {

	private static final long serialVersionUID = 8831297289608495058L;
	
	private String errHeader;
	private String errCode;
	private String errMsg;
 

	 
	public CS_BaseException(){}
	public CS_BaseException(String errMsg) {
		super(errMsg);
		this.errMsg = errMsg;
	}

	public CS_BaseException(StringBuilder errMsg) {
		super(errMsg.toString());
		this.errMsg = errMsg.toString();
	}
	
	public CS_BaseException(String errCode, String errHeader, String errMsg) {
		super(errMsg);
		this.errCode = errCode;
		this.errHeader = errHeader;
		this.errMsg = errMsg;
	}
	
	
	public String getErrHeader() {
		return errHeader;
	}

	public void setErrHeader(String errHeader) {
		this.errHeader = errHeader;
	}

	public String getErrCode() {
		return errCode;
	}
 
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
 
	public String getErrMsg() {
		return errMsg;
	}
	@Override
	public String getMessage() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}