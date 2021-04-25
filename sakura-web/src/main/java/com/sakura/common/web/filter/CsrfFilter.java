package com.sakura.common.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther YangFan
 * @Date 2020/12/26 15:48
 */

@Component
@ConfigurationProperties(prefix = "security.csrf")
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CsrfFilter.class);

    /**
     * 过滤器配置对象
     */
    FilterConfig filterConfig = null;

    /**
     * 是否启用
     */
    private boolean enable = true;

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 忽略的URL
     */
    private List<String> excludes;

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

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
        if (!enable || isExcludeUrl(request.getServletPath())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String referer = request.getHeader("Referer");
        String serverName = request.getServerName();
        String gatewayHeader = request.getHeader("gatewayTest");

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 判断是否存在外链请求本站
        if (StringUtils.isEmpty(gatewayHeader) && null != referer && !referer.contains(serverName)) {
            log.error("CSRF过滤器 => 服务器：{} => 当前域名：{}", serverName, referer);
            response.setContentType("text/html; charset=utf-8");
            String jsonStr = "{\"code\":\"500\",\"message\":\"系统不支持当前域名的访问！\",\"data\":\"null\"}";
            response.getWriter().write(jsonStr);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
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
    private boolean isExcludeUrl(String url) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
                .anyMatch(Matcher::find);
    }
}
