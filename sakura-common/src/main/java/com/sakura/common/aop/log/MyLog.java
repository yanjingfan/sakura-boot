package com.sakura.common.aop.log;

import java.lang.annotation.*;

/**
 * @description: 自定义日志类
 *
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
    String value() default "";
}
