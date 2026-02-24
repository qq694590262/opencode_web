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
        String httpMethod = request != null ? request.getMethod() : "UNKNOWN";
        String ip = getIpAddress(request);
        String userAgent = request != null ? request.getHeader("User-Agent") : "";
        String params = getParams(point);
        String username = getUsername(request);
        
        String module = getModule(className);
        String action = getAction(methodName, httpMethod);
        String operation = module + " - " + action;
        
        Log logEntity = new Log();
        logEntity.setModule(module);
        logEntity.setMethod(httpMethod + " " + className + "." + methodName);
        logEntity.setIp(ip);
        logEntity.setUserAgent(userAgent);
        logEntity.setParams(params);
        logEntity.setUsername(username);
        logEntity.setOperation(operation);
        logEntity.setCreateTime(LocalDateTime.now());
        
        Object result = null;
        try {
            result = point.proceed();
            logEntity.setStatus(1);
        } catch (Exception e) {
            logEntity.setStatus(0);
            logEntity.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logEntity.setDuration(duration);
            
            try {
                logService.saveLog(logEntity);
            } catch (Exception e) {
                log.error("保存操作日志失败: {}", e.getMessage());
            }
        }
        
        return result;
    }
    
    private String getModule(String className) {
        if ("AuthController".equals(className)) return "系统登录";
        if ("UserController".equals(className)) return "用户管理";
        if ("RoleController".equals(className)) return "角色管理";
        if ("ProjectController".equals(className)) return "项目管理";
        if ("TaskController".equals(className)) return "任务管理";
        if ("DocumentController".equals(className)) return "文档管理";
        if ("WikiController".equals(className)) return "知识库";
        if ("CalendarController".equals(className)) return "日程管理";
        if ("ReportController".equals(className)) return "报表管理";
        if ("DashboardController".equals(className)) return "仪表盘";
        if ("FileUploadController".equals(className)) return "文件上传";
        if ("SettingsController".equals(className)) return "系统设置";
        if ("LogController".equals(className)) return "日志管理";
        return className.replace("Controller", "");
    }
    
    private String getAction(String methodName, String httpMethod) {
        String name = methodName.toLowerCase();
        
        if ("login".equals(name)) return "用户登录";
        if ("logout".equals(name)) return "用户登出";
        if (name.contains("create") || name.contains("save") || name.contains("add")) return "新增";
        if (name.contains("update") || name.contains("edit")) return "编辑";
        if (name.contains("delete") || name.contains("remove")) return "删除";
        if (name.contains("get") || name.contains("list") || name.contains("query") || name.contains("find") || name.contains("page") || name.contains("all")) return "查询";
        if (name.contains("export")) return "导出";
        if (name.contains("import")) return "导入";
        if (name.contains("upload")) return "上传";
        if (name.contains("download")) return "下载";
        
        if ("GET".equalsIgnoreCase(httpMethod)) return "查看";
        
        return methodName;
    }
    
    private String getIpAddress(HttpServletRequest request) {
        if (request == null) return "unknown";
        
        String ip = request.getHeader("X-Forwarded-For");
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
        
        if (request.getSession(false) != null) {
            Object sessionUsername = request.getSession(false).getAttribute("username");
            if (sessionUsername != null) {
                return sessionUsername.toString();
            }
        }
        
        return "anonymous";
    }
    
    private String getParams(ProceedingJoinPoint point) {
        try {
            Object[] args = point.getArgs();
            if (args == null || args.length == 0) {
                return "";
            }
            
            Object[] filtered = Arrays.stream(args)
                .filter(arg -> arg != null)
                .filter(arg -> !arg.getClass().getName().contains("MultipartFile"))
                .filter(arg -> !arg.getClass().getName().contains("HttpServletRequest"))
                .filter(arg -> !arg.getClass().getName().contains("HttpServletResponse"))
                .toArray();
            
            if (filtered.length == 0) {
                return "";
            }
            
            return objectMapper.writeValueAsString(filtered);
        } catch (Exception e) {
            return "参数获取失败";
        }
    }
}
