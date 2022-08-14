package com.sakura.common.web.filter;

import com.sakura.common.web.properties.WebSecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
//@ConfigurationProperties(prefix = "security.csrf")
@WebFilter(filterName = "CsrfFilter", urlPatterns = "/*")
public class CsrfFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CsrfFilter.class);

    @Autowired
    private WebSecurityProperties properties;

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

        boolean enabled = properties.getCsrf().isEnabled();
        // 不启用或者已忽略的URL不拦截
        if (enabled || isExcludeUrl(request.getServletPath())) {
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
        List<String> excludes = properties.getCsrf().getExcludes();
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
                .anyMatch(Matcher::find);
    }
}
