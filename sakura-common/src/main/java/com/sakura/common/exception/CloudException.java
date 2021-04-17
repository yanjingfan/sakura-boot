package com.sakura.common.exception;

public class CloudException extends RuntimeException {

	/** 
	 * @Fields serialVersionUID : 
	 */ 
	private static final long serialVersionUID = 1L;

	public CloudException() {
		super();
	}

	public CloudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CloudException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloudException(String message) {
		super(message);
	}

	public CloudException(Throwable cause) {
		super(cause);
	}

}
  