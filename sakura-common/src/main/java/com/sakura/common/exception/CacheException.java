package com.sakura.common.exception;


public class CacheException extends CloudException {

	/** 
	 * @Fields serialVersionUID : 
	 */ 
	private static final long serialVersionUID = 1L;

	public CacheException() {
		super();
	}

	public CacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(Throwable cause) {
		super(cause);
	}
	

}
  