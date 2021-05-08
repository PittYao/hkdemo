package com.cebon.jiwei.shengxunzhuji.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author: bugProvider
 * @date: 2019/10/3 10:15
 * @description: 请求日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.cebon.jiwei.shengxunzhuji.controller..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result;
        Boolean isError = Boolean.FALSE;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            isError = Boolean.TRUE;
            throw e;
        } finally {
            // 执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            // 保存日志
            saveLog(point, time, isError);
        }
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time, Boolean isError) {
        // 获取ip
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();


            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            // 请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            StringBuilder params = new StringBuilder();
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params.append(" ").append(paramNames[i]).append(": ").append(args[i]);
                }
            }

            log.info("{\"请求日志\":\"\", \"requestIp\": \"{}\", \"url\": \"{}\", \"method\": \"{}\" , \"contentType\": \"{}\" , \"characterEncoding\": \"{}\",\"controllerMethod\": \"{}\",\"params\": \"{}\",\"executeTime\": \"{}ms\",\"isError\": \"{}\" }",
                    request.getRemoteAddr(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    request.getContentType(),
                    request.getCharacterEncoding(),
                    className + "." + methodName + "()",
                    params.toString(),
                    time,
                    isError
            );
        } else {
            log.warn("日志拦截获取request失败");
        }

    }
}
