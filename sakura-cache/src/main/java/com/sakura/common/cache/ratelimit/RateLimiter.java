package com.sakura.common.cache.ratelimit;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int DEFAULT_REQUEST = 10;

    /**
     * max 最大请求数
     */
    @AliasFor("max") int value() default DEFAULT_REQUEST;

    /**
     * max 最大请求数
     */
    @AliasFor("value") int max() default DEFAULT_REQUEST;

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 超时时长，默认1分钟
     */
    long timeout() default 1;

    /**
     * 超时时间单位，默认 分钟
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
