package com.sakura.common.web.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LocalUserInterceptor extends HandlerInterceptorAdapter {

    private static String USER_HEAD_ID = "id";
    private static String USER_HEAD_USERID = "loginUserId";
    private static String USER_HEAD_USERNAME = "loginUserName";
    private static String USER_HEAD_ORGID = "loginUserOrgId";
    private static String USER_HEAD_DEPID = "loginUserDepId";
    private static String USER_HEAD_DEPNAME = "loginUserDepName";
    private static String USER_HEAD_USERTYPE = "loginUserType";
    private static String USER_HEAD_SESSIONID = "loginUserSessionId";

    @Override
    public void afterCompletion(HttpServletRequest requestuest, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        UserContext.remove();
        super.afterCompletion(requestuest, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest requestuest, HttpServletResponse response,
            Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(requestuest, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String id = request.getHeader(USER_HEAD_ID);
        String userId = request.getHeader(USER_HEAD_USERID);
        String userName = request.getHeader(USER_HEAD_USERNAME);
        String orgId = request.getHeader(USER_HEAD_ORGID);
        String depId = request.getHeader(USER_HEAD_DEPID);
        String depName = request.getHeader(USER_HEAD_DEPNAME);
        String userType = request.getHeader(USER_HEAD_USERTYPE);
        String sessionId = request.getHeader(USER_HEAD_SESSIONID);
        if (!StringUtils.isEmpty(userId) && !StringUtils.isEmpty(sessionId)) {
            LocalUser localUser = new LocalUser(id, userId, userName, orgId, depId, depName, userType, sessionId);
            UserContext.set(localUser);
        }

        return super.preHandle(request, response, handler);
    }

}
