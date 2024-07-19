package com.yt.trycatch;


import com.yt.common.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TryCatchAspect {

    @Pointcut("@annotation(com.yt.trycatch.TryCatch)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            logDetails(joinPoint, e);
            return null;
        }
    }

    private void logDetails(ProceedingJoinPoint joinPoint, Exception e) {
        Method method = ReflectionUtil.getMethod(joinPoint);
        log.error("TryCatchAspect\n" +
                        "class:{}\n" +
                        "method:{}\n" +
                        "args:{}",
                method.getDeclaringClass().getSimpleName(), method.getName(), joinPoint.getArgs());
        log.error("TryCatchAspect", e);
    }
}
