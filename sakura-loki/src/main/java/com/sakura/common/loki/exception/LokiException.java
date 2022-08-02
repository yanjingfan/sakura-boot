
package com.sakura.common.loki.exception;

public class LokiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LokiException() {
		super();
	}

	public LokiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LokiException(String message, Throwable cause) {
		super(message, cause);
	}

	public LokiException(String message) {
		super(message);
	}

	public LokiException(Throwable cause) {
		super(cause);
	}

}