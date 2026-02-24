package com.opencode.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 不需要拦截的路径
    private static final String[] EXCLUDE_PATHS = {
        "/api/auth/login",        // 登录接口
        "/api/auth/info",         // 获取用户信息
        "/error",                 // 错误页面
        "/api/logs/all",         // 日志查询（可选，根据需求）
        "/api/logs/recent",      // 最近日志
        "/api/logs/statistics", // 日志统计
        "/api/logs/search"       // 日志搜索
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        
        // 检查是否是排除路径
        for (String path : EXCLUDE_PATHS) {
            if (requestURI.contains(path)) {
                return true;
            }
        }
        
        // 检查session
        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("请求 [{}] 无session，拒绝访问", requestURI);
            sendUnauthorizedResponse(response);
            return false;
        }
        
        Object userId = session.getAttribute("userId");
        Object username = session.getAttribute("username");
        
        if (userId == null || username == null) {
            log.warn("请求 [{}] 用户未登录或session过期，username: {}", requestURI, username);
            sendUnauthorizedResponse(response);
            return false;
        }
        
        // 将用户信息存入request属性，供后续使用
        request.setAttribute("userId", userId);
        request.setAttribute("username", username.toString());
        
        log.debug("请求 [{}] 用户 {} 验证通过", requestURI, username);
        
        return true;
    }
    
    private void sendUnauthorizedResponse(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"请重新登录\",\"data\":null}");
    }
}
