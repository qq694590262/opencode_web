package com.opencode.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencode.entity.Log;
import com.opencode.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    
    private final LogService logService;
    private final ObjectMapper objectMapper;
    
    @Pointcut("execution(* com.opencode.controller..*.*(..)) && !execution(* com.opencode.controller.LogController.*(..))")
    public void logPointcut() {}
    
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
        
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        String httpMethod = request != null ? request.getMethod() : "";
        String ip = getIpAddress(request);
        String params = getParams(point);
        String username = getUsername(request);
        
        String operation = className + "." + methodName;
        
        Log log = new Log();
        log.setMethod(httpMethod + ":" + operation);
        log.setIp(ip);
        log.setParams(params);
        log.setUsername(username);
        log.setCreateTime(LocalDateTime.now());
        
        Object result = null;
        try {
            result = point.proceed();
            log.setStatus(1);
        } catch (Exception e) {
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.setDuration(duration);
            
            try {
                logService.saveLog(log);
            } catch (Exception e) {
                log.error("保存操作日志失败: {}", e.getMessage());
            }
        }
        
        return result;
    }
    
    private String getIpAddress(HttpServletRequest request) {
        if (request == null) return "unknown";
        
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
    
    private String getUsername(HttpServletRequest request) {
        if (request == null) return "anonymous";
        Object username = request.getAttribute("username");
        if (username != null) {
            return username.toString();
        }
        Object sessionUsername = request.getSession(false) != null ? request.getSession(false).getAttribute("username") : null;
        if (sessionUsername != null) {
            return sessionUsername.toString();
        }
        return "anonymous";
    }
    
    private String getParams(ProceedingJoinPoint point) {
        try {
            Object[] args = point.getArgs();
            if (args == null || args.length == 0) {
                return "";
            }
            
            Object[] filteredArgs = Arrays.stream(args)
                .filter(arg -> !(arg instanceof org.springframework.web.multipart.MultipartFile))
                .filter(arg -> !(arg instanceof org.springframework.web.multipart.MultipartFile[]))
                .toArray();
            
            return objectMapper.writeValueAsString(filteredArgs);
        } catch (Exception e) {
            return "params serialization failed";
        }
    }
}
