package com.sakura.common.web.interceptor;

import com.sakura.common.web.wrapper.SQLInjectionHttpServletRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther YangFan
 * @Date 2021/3/8 16:08
 */
@ConfigurationProperties(prefix = "security.sql")
public class SqlInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(SqlInterceptor.class);


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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 不启用或者已忽略的URL不拦截
        if (!enable || isExcludeUrl(request.getServletPath())) {
            return true;
        }

        log.debug("SqlInterceptor start pre ...");

        String method = request.getMethod();

        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()){
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            for(String value: values){
                //sql注入直接拦截
                if (sqlValidate(response, value)) return false;
            }
        }

        if("POST".equals(method)){
            SQLInjectionHttpServletRequestWrapper wrapper = new SQLInjectionHttpServletRequestWrapper(request);
            String requestBody = wrapper.getRequestBodyParame();
            if(!StringUtils.isEmpty(requestBody)){
                //sql注入直接拦截
                if (sqlValidate(response, requestBody)) return false;
            }
        }

        //TODO
        return true;
    }

    private boolean sqlValidate(HttpServletResponse response, String requestBody) throws IOException {
        if(sqlInject(requestBody)){
            response.setContentType("text/html; charset=utf-8");
            String jsonStr = "{\"code\":591,\"message\":\"请求参数含有非法字符!\",\"data\":\"null\"}";
            response.getWriter().write(jsonStr);
            response.setStatus(591);
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     *
     * @Title: sqlInject
     * @Description: TODO SQL 注入正在表达式(sql 函数关键字过滤)
     * @param: @param value
     * @param: @return
     * @return: boolean
     * @throws
     */
    public boolean sqlInject(String value){
        if(value == null || "".equals(value)){
            return false;
        }
        /**
         * 预编译SQL过滤正则表达式
         */
        Pattern sqlPattern = Pattern.compile(
                " and |\\+\\|\\|\\+|\\+or\\+|\\+and\\+| exec | execute |insert |select |delete |update |count |drop |declare |sitename |net user |xp_cmdshell |like' |table | from | grant | group_concat |column_name |information_schema.columns |table_schema |union | where |order by| truncate |'|\\*|;|--|//|‘",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = sqlPattern.matcher(value);
        return matcher.find();
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
