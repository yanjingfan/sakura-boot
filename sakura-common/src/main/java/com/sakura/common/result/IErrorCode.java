package com.sakura.common.result;

/**
 * 封装API的错误码
 * Created by yangfan on 2019/4/19.
 */
public interface IErrorCode {
    int getCode();

    String getMessage();
}
