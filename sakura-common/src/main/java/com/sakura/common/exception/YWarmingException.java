package com.sakura.common.exception;

public class YWarmingException extends CloudException{
	private static final long serialVersionUID = 3526781918697320427L;
	/**
	 * 构建一个在ui弹出友好警示框的异常
	 * @param warming
	 */
	public YWarmingException(String warming) {
		super("businessLogicWarm["+warming+"]");
	}

}
