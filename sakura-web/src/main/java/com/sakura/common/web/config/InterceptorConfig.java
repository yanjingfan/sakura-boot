package com.sakura.common.web.config;

import com.sakura.common.web.auth.LocalUserInterceptor;
import com.sakura.common.web.interceptor.SqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    //SQL注入拦截器
    @Bean
    public SqlInterceptor sqlInjectInterceptor () {
        return new SqlInterceptor();
    }

    //本地用户拦截器
    @Bean
    public LocalUserInterceptor localUserInterceptor () {
        return new LocalUserInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决 SWAGGER 404报错
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sqlInjectInterceptor());
        registry.addInterceptor(localUserInterceptor());
    }
}
