package com.sakura.common.web.filter;

import com.sakura.common.web.properties.WebSecurityProperties;
import com.sakura.common.web.wrapper.SQLInjectionHttpServletRequestWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean enabled = properties.getSql().isEnabled();
        if (!enabled || isExcludeUrl(request.getServletPath())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()){
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            for(String value: values){
                if (sqlValidate(response, value)) {
                    return;
                }
            }
        }

        String ContentType = request.getHeader("Content-Type");
        if(request.getParameterMap().entrySet().size() > 0 || ContentType != null
                && ContentType.contains("application/json")) {
            SQLInjectionHttpServletRequestWrapper wrapper = new SQLInjectionHttpServletRequestWrapper(request);
            String requestBody = wrapper.getRequestBodyParame();
            if(!StringUtils.isEmpty(requestBody)){
                if (sqlValidate(response, requestBody)) {
                    return;
                }
            }
            filterChain.doFilter(wrapper, servletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean sqlValidate(HttpServletResponse response, String requestBody) throws IOException {
        if(sqlInject(requestBody)){
            response.setContentType("application/json; charset=utf-8");
            String req = requestBody.replaceAll("\"", "'").replaceAll("\\s+", "");
            String jsonStr = "{\"code\":591,\"message\":\"请求参数含有非法字符：" + req + "\",\"data\":\"null\"}";
            response.getWriter().write(jsonStr);
            response.setStatus(591);
            return true;
        }
        return false;
    }

    public boolean sqlInject(String value){
        if(value == null || "".equals(value)){
            return false;
        }
        Pattern sqlPattern = Pattern.compile(
                " and |\\+\\|\\|\\+|\\+or\\+|\\+and\\+| exec | execute |insert |select |delete |update |count |drop |declare |sitename |net user |xp_cmdshell |like' |table | from | grant | group_concat |column_name |information_schema.columns |table_schema |union | where |order by| truncate |'|\\*|;|-- |//|‘",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = sqlPattern.matcher(value);
        return matcher.find();
    }

    private boolean isExcludeUrl(String url) {
        List<String> excludes = properties.getSql().getExcludes();
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url))
                .anyMatch(Matcher::find);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}