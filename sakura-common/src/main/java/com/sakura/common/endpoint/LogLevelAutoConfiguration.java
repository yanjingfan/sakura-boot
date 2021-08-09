package com.sakura.common.endpoint;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @auther YangFan
 * @Date 2021/8/7 18:43
 */
@Configuration
@EnableConfigurationProperties(LogLevelProperties.class)
public class LogLevelAutoConfiguration {
}
