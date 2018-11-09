package com.blockeng.admin.aop.logging;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.entity.SysUserLog;
import com.blockeng.admin.service.SysUserLogService;
import com.blockeng.framework.utils.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author qiang
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysUserLogService sysUserLogService;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.blockeng.admin.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 执行方法
        result = point.proceed();
        stopWatch.stop();
        // 执行时长(毫秒)
        long time = stopWatch.getTotalTimeMillis();
        // 保存日志
        SysUserLog sysLog = new SysUserLog();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (Optional.ofNullable(logAnnotation).isPresent()) {
            // 注解上的描述
            sysLog.setGroup(logAnnotation.value());
            sysLog.setType(logAnnotation.type().getCode());
        }
        // 请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = point.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": ";
                if (!paramNames[i].equalsIgnoreCase("password")) {
                    params += args[i];
                }
            }
            sysLog.setParams(params);
        }

        // 设置IP地址
        sysLog.setIp(IpUtil.getIpAddr(request));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof SysUser) {
            SysUser sysUser = (SysUser) authentication.getPrincipal();
            sysLog.setUserId(sysUser.getId());
        }
        sysLog.setTime(time);
        // 保存系统日志
        sysUserLogService.insert(sysLog);
        return result;
    }
}