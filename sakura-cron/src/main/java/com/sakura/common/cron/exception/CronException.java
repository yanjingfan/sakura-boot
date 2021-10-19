package com.sakura.common.cron.exception;

public class CronException extends RuntimeException{
	private static final long serialVersionUID = 3526781918697320427L;

	public CronException(String warming) {
		super(warming);
	}

}
