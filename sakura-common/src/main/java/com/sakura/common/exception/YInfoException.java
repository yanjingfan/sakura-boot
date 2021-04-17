package com.sakura.common.exception;

public class YInfoException extends CloudException{
	private static final long serialVersionUID = 3526781918697320427L;
	/**
	 * 构建一个在ui弹出友好信息提示的异常
	 * @param warming
	 */
	public YInfoException(String warming) {
		super("businessLogicInfo["+warming+"]");
	}

}
