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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    
    private final LogService logService;
    private final ObjectMapper objectMapper;
    
    private static final Map<String, String> MODULE_MAP = new HashMap<>();
    private static final Map<String, String> ACTION_MAP = new HashMap<>();
    
    static {
        MODULE_MAP.put("AuthController", "系统登录");
        MODULE_MAP.put("UserController", "用户管理");
        MODULE_MAP.put("RoleController", "角色管理");
        MODULE_MAP.put("ProjectController", "项目管理");
        MODULE_MAP.put("TaskController", "任务管理");
        MODULE_MAP.put("DocumentController", "文档管理");
        MODULE_MAP.put("WikiController", "知识库");
        MODULE_MAP.put("CalendarController", "日程管理");
        MODULE_MAP.put("ReportController", "报表管理");
        MODULE_MAP.put("DashboardController", "仪表盘");
        MODULE_MAP.put("FileUploadController", "文件上传");
        MODULE_MAP.put("SettingsController", "系统设置");
        
        ACTION_MAP.put("login", "用户登录");
        ACTION_MAP.put("logout", "用户登出");
        ACTION_MAP.put("create", "新增");
        ACTION_MAP.put("save", "新增");
        ACTION_MAP.put("add", "新增");
        ACTION_MAP.put("update", "编辑");
        ACTION_MAP.put("edit", "编辑");
        ACTION_MAP.put("delete", "删除");
        ACTION_MAP.put("remove", "删除");
        ACTION_MAP.put("get", "查看");
        ACTION_MAP.put("list", "查询");
        ACTION_MAP.put("query", "查询");
        ACTION_MAP.put("find", "查询");
        ACTION_MAP.put("page", "分页查询");
        ACTION_MAP.put("all", "获取全部");
        ACTION_MAP.put("export", "导出");
        ACTION_MAP.put("import", "导入");
        ACTION_MAP.put("upload", "上传");
        ACTION_MAP.put("download", "下载");
    }
    
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
        String userAgent = getUserAgent(request);
        String params = getParams(point);
        String username = getUsername(request);
        
        String module = MODULE_MAP.getOrDefault(className, className.replace("Controller", ""));
        String action = getActionDescription(methodName, httpMethod);
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
    
    private String getActionDescription(String methodName, String httpMethod) {
        String lowerMethod = methodName.toLowerCase();
        
        if ("login".equals(lowerMethod) || "logout".equals(lowerMethod)) {
            return ACTION_MAP.get(lowerMethod);
        }
        
        for (Map.Entry<String, String> entry : ACTION_MAP.entrySet()) {
            if (lowerMethod.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        if ("GET".equalsIgnoreCase(httpMethod)) {
            return "查看";
        }
        
        return methodName;
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
    
    private String getUserAgent(HttpServletRequest request) {
        if (request == null) return "";
        return request.getHeader("User-Agent");
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
                .filter(arg -> arg != null && !(arg instanceof org.springframework.web.multipart.MultipartFile))
                .filter(arg -> arg != null && !(arg instanceof org.springframework.web.multipart.MultipartFile[]))
                .filter(arg -> !(arg instanceof org.springframework.http.HttpServletRequest))
                .filter(arg -> !(arg instanceof org.springframework.http.HttpServletResponse))
                .toArray();
            
            if (filteredArgs.length == 0) {
                return "";
            }
            
            return objectMapper.writeValueAsString(filteredArgs);
        } catch (Exception e) {
            return "参数序列化失败";
        }
    }
}
