package com.sakura.common.web.filter;

import com.sakura.common.web.wrapper.XssHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Xss过滤器
 *
 * @auther YangFan
 * @Date 2020/12/26 15:28
 */

@Component
//@ConfigurationProperties(prefix = "security.xss")
@WebFilter(filterName = "XssFilter", urlPatterns = "/*")
public class XssFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(XssFilter.class);

    /**
     * 过滤器配置对象
     */
    FilterConfig filterConfig = null;

    /**
     * 是否启用
     */
//    private boolean enable;
//
//    public void setEnable(boolean enable) {
//        this.enable = enable;
//    }

    /**
     * 忽略的URL
     */
//    private List<String> excludes;
//
//    public void setExcludes(List<String> excludes) {
//        this.excludes = excludes;
//    }

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

        // 不启用或者已忽略的URL不拦截
//        if (!enable || isExcludeUrl(request.getServletPath())) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(xssHttpServletRequestWrapper, servletResponse);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * 判断是否为忽略的URL
     *
     * @param url URL路径
     * @return true-忽略，false-过滤
     */
//    private boolean isExcludeUrl(String url) {
//        if (excludes == null || excludes.isEmpty()) {
//            return false;
//        }
//        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
//                .anyMatch(Matcher::find);
//    }

}
