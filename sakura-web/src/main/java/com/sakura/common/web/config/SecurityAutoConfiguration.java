package com.sakura.common.web.config;

import com.sakura.common.web.properties.WebSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @auther YangFan
 * @Date 2021/8/7 18:43
 */
@Configuration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class SecurityAutoConfiguration {
}
