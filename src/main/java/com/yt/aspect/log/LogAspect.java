package com.yt.aspect.log;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.NotNull;
import com.yt.util.ReflectUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.yt.aspect.log.LogMethodArgsAndResult.Condition;
import static com.yt.aspect.log.LogMethodArgsAndResult.Condition.*;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LogAspect implements ApplicationContextAware {

    private LogMethodArgsAndResultProperties properties;
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.yt.aspect.log.LogMethodArgsAndResult)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        try {
            recordArgsAndResult(joinPoint, result);
        } catch (Exception e) {
            log.error("LogAspect around error", e);
        }
        return result;
    }

    private void recordArgsAndResult(ProceedingJoinPoint joinPoint, Object result) {
        LogMethodArgsAndResult anno = ReflectUtils.getAnnotation(joinPoint, LogMethodArgsAndResult.class);
        boolean needLog = false;
        Set<Condition> conditions = new HashSet<>(Arrays.asList(anno.conditions()));
        if (conditions.contains(RESULT_IS_NULL)) {
            if (result == null) {
                needLog = true;
            }
        }
        if (conditions.contains(RESULT_IS_TRUE)) {
            if (BooleanUtil.isTrue((Boolean) result)) {
                needLog = true;
            }
        }
        if (conditions.contains(RESULT_IS_FALSE)) {
            if (BooleanUtil.isFalse((Boolean) result)) {
                needLog = true;
            }
        }
        if (conditions.contains(ALL)) {
            needLog = true;
        }
        if (conditions.contains(ARGS_HIT_KEY)) {
            needLog = isInKeys(anno, joinPoint);
        }
        if (!needLog) {
            needLog = LogThreadLocal.isDoLog();
            LogThreadLocal.clear();
        }
        if (needLog) {
            Method method = ReflectUtils.getMethod(joinPoint);
            log.info("LogAspect\n{}\n{}\nargs:{}\nresult:{}",
                    method.getDeclaringClass().getSimpleName(), method.getName(), joinPoint.getArgs(), result);
        }
    }

    private boolean isInKeys(LogMethodArgsAndResult anno, ProceedingJoinPoint joinPoint) {
        try {
            return properties.inKeys(tryGetFromArgs(anno, joinPoint));
        } catch (Exception e) {
            log.error("LogAspect tryGetFromArgs error", e);
        }
        return false;
    }

    private Object tryGetFromArgs(LogMethodArgsAndResult anno, ProceedingJoinPoint joinPoint) throws Exception {
        Object keyArg = joinPoint.getArgs()[anno.keyArgsIndex()];
        if (keyArg == null) {
            return null;
        }
        Method parseKeyMethod = getParseKeyMethod(anno, joinPoint, keyArg.getClass());
        if (parseKeyMethod != null) {
            return ReflectUtils.invoke(parseKeyMethod, applicationContext.getBean(parseKeyMethod.getDeclaringClass()), keyArg);
        }
        return getFromSelfOrField(anno, keyArg);
    }

    private Object getFromSelfOrField(LogMethodArgsAndResult anno, Object keyArg) throws Exception {
        if (keyArg instanceof Number) {
            return keyArg;
        }
        if (keyArg instanceof String) {
            return keyArg;
        }
        return ReflectUtils.getFieldValue(keyArg, anno.keyName());

    }

    private Method getParseKeyMethod(LogMethodArgsAndResult anno, ProceedingJoinPoint joinPoint, Class<?> keyArgClass) throws NoSuchMethodException {
        if (StrUtil.isBlank(anno.parseKeyMethodName())) {
            return null;
        }
        Method method = ReflectUtils.getMethod(joinPoint);
        Class<?> clazz = method.getDeclaringClass();
        String parseKeyMethodName = anno.parseKeyMethodName();
        return clazz.getDeclaredMethod(parseKeyMethodName, keyArgClass);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
