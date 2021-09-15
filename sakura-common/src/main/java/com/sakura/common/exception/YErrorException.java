package com.sakura.common.exception;

public class YErrorException extends CloudException{
	private static final long serialVersionUID = 3526781918697320427L;
	/**
	 * 构建一个在ui弹出友好错误框的异常
	 * @param warming
	 */
	public YErrorException(String warming) {
		super(warming);
	}

}
