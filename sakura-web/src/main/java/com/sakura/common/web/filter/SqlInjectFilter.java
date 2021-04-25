package com.sakura.common.web.filter;

import com.sakura.common.web.wrapper.SQLInjectionHttpServletRequestWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * SQL注入过滤器
 *
 * @auther YangFan
 * @Date 2020/11/20 9:37
 */

@Component
@WebFilter(filterName = "SqlInjectFilter", urlPatterns = "/*")
public class SqlInjectFilter implements Filter {

    private final static Log logger = LogFactory.getLog(SqlInjectFilter.class);

    /**
     * 过滤器配置对象
     */
    FilterConfig filterConfig = null;

    /**
     * 初始化
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    /**
     * 拦截
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;



        String ContentType = request.getHeader("Content-Type");
        if (request.getParameterMap().entrySet().size() > 0 || ContentType != null
                && ContentType.contains("application/json")) {
            SQLInjectionHttpServletRequestWrapper sqlInjectHttpServletRequestWrapper = new SQLInjectionHttpServletRequestWrapper(
                    request);
            filterChain.doFilter(sqlInjectHttpServletRequestWrapper, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}