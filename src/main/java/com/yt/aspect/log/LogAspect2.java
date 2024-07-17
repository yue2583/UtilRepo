package com.yt.aspect.log;


import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.NotNull;
import com.yt.util.ReflectUtils;
import lombok.AllArgsConstructor;
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

import static com.yt.aspect.log.MethodLog.Condition;
import static com.yt.aspect.log.MethodLog.Condition.*;

@Aspect
@Component
@AllArgsConstructor
public class LogAspect2 implements ApplicationContextAware {

    private MethodLogProperties properties;
    private ApplicationContext applicationContext;

    @Pointcut("@annotation(com.yt.aspect.log.MethodLog2)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enableBefore(joinPoint)) {
            return joinPoint.proceed();
        }

        Object result = null;
        Throwable ex = null;
        LogStore logStore = LogStoreThreadLocal.enter();

        try {
            recordBefore(logStore);
            result = joinPoint.proceed();
        } catch (Throwable e) {
            ex = e;
        } finally {
            recordAfter(joinPoint, result, ex, logStore);
            LogStoreThreadLocal.exit();
        }
        if (ex != null) {
            throw ex;
        }
        return result;
    }

    private void recordAfter(ProceedingJoinPoint joinPoint, Object result, Throwable ex, LogStore logStore) {
    }

    private boolean enableBefore(ProceedingJoinPoint joinPoint) {
        MethodLog2 anno = ReflectUtils.getAnnotation(joinPoint, MethodLog2.class);
        return anno != null && mainSwitchOn() && mightAnyCondition(anno);
    }

    private boolean mightAnyCondition(MethodLog2 anno) {
        return properties.inUids(getUid())
                || anno.logWhenException()
                || anno.costMillsThreshold() < Long.MAX_VALUE
                || anno.resultCondition() != MethodLog2.ResultCondition.NO;
    }

    private boolean mainSwitchOn() {
        return properties.enable();
    }

    private Long getUid() {
        // todo 待实现
        return null;
    }

    private void recordBefore(LogStore logStore) {
        logStore.start();
    }

    private void recordArgsAndResult(ProceedingJoinPoint joinPoint, Object result) {
        MethodLog anno = ReflectUtils.getAnnotation(joinPoint, MethodLog.class);
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

    private boolean isInKeys(MethodLog anno, ProceedingJoinPoint joinPoint) {
        try {
            return properties.inKeys(tryGetFromArgs(anno, joinPoint));
        } catch (Exception e) {
            log.error("LogAspect tryGetFromArgs error", e);
        }
        return false;
    }

    private Object tryGetFromArgs(MethodLog anno, ProceedingJoinPoint joinPoint) throws Exception {
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

    private Object getFromSelfOrField(MethodLog anno, Object keyArg) throws Exception {
        if (keyArg instanceof Number) {
            return keyArg;
        }
        if (keyArg instanceof String) {
            return keyArg;
        }
        return ReflectUtils.getFieldValue(keyArg, anno.keyName());

    }

    private Method getParseKeyMethod(MethodLog anno, ProceedingJoinPoint joinPoint, Class<?> keyArgClass) throws NoSuchMethodException {
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
