package com.sakura.common.exception;

import com.sakura.common.result.CommonResult;
import com.sakura.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import static com.sakura.common.result.CommonResult.failed;

/**
 * @auther YangFan
 * @Date 2020/12/22 10:29
 */

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> constraintViolationException(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult IllegalArgumentException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult noHandlerFoundException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.VALIDATE_FAILED, ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult unknownException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.FAILED, "系统发生未知异常：" + ex.getMessage());
    }

    @ExceptionHandler({CloudException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult cloudException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.FAILED, ex.getMessage());
    }

    @ExceptionHandler({YErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult yErrorException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.FAILED, "系统错误：" + ex.getMessage());
    }

    @ExceptionHandler({YInfoException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult yInfoException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.FAILED, "系统提示：" + ex.getMessage());
    }

    @ExceptionHandler({YWarmingException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult yWarmingException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage(), ex);
        return CommonResult.failed(ResultCode.FAILED, "系统警告：" + ex.getMessage());
    }
}
