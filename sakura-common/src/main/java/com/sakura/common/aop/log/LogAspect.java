package com.sakura.common.aop.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @description: 自定义日志切面类
 *
 **/
@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.sakura.common.aop.log.MyLog)")
    public void myPointCut(){
    }
    @Before("myPointCut()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //获取url,请求方法，ip地址，类名以及方法名，参数
        logger.info("url={},method={},ip={},class_method={},args={}", request.getRequestURI(),request.getMethod(),request.getRemoteAddr(),joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),joinPoint.getArgs());
    }
    @AfterReturning(pointcut = "myPointCut()")
    public void printLog(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        int status = response.getStatus();
        String statusStr = "";
        if (status == 200){
            statusStr = "成功";
        }else {
            statusStr = "失败";
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        MyLog myLog = method.getAnnotation(MyLog.class);
        String value = null;
        if (myLog!=null){
            value = myLog.value();
        }
        logger.info(new Date()+"-----"+value+"   调用结果："+statusStr);
    }
}
