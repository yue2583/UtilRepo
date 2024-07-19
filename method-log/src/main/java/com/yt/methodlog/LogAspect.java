package com.yt.methodlog;


import com.yt.common.common.ResultAndThrowable;
import com.yt.common.util.ReflectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;
import java.util.List;


@Aspect
public class LogAspect {

    LogConfiguration configuration;

    @Pointcut("@annotation(com.yt.methodlog.MethodLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LogInfo logInfo = new LogInfo();
        boolean customLogCondition = customLogCondition();
        boolean mightAnnoCondition = mightAnnoCondition(joinPoint);
        boolean enable = mightAnnoCondition || customLogCondition;
        if (!enable) {
            return joinPoint.proceed();
        }
        return doAround(joinPoint, logInfo, customLogCondition);
    }

    private boolean customLogCondition() {
        List<LogCondition> customLogConditions = configuration.customLogConditions;
        if (customLogConditions == null) {
            return false;
        }
        for (LogCondition logCondition : customLogConditions) {
            if (logCondition.isTrue()) {
                return true;
            }
        }
        return false;
    }

    private Object doAround(ProceedingJoinPoint joinPoint, LogInfo logInfo, boolean customLogCondition) throws Throwable {
        recordBefore(logInfo);
        ResultAndThrowable resultAndThrowable = execute(joinPoint);
        recordAfter(joinPoint, resultAndThrowable, logInfo, customLogCondition);
        logInfo.log();

        if (resultAndThrowable.getThrowable() != null) {
            throw resultAndThrowable.getThrowable();
        }
        return resultAndThrowable.getResult();
    }

    private ResultAndThrowable execute(ProceedingJoinPoint joinPoint) {
        try {
            return ResultAndThrowable.result(joinPoint.proceed());
        } catch (Throwable t) {
            return ResultAndThrowable.throwable(t);
        }
    }

    private void recordAfter(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, LogInfo logInfo, boolean customLogCondition) {
        logInfo.end();
        logInfo.setEnable(isEnableLog(joinPoint, resultAndThrowable, logInfo, customLogCondition));
        if (!logInfo.getEnable()) {
            return;
        }
        recordMethodExecuteInfo(joinPoint, resultAndThrowable, logInfo);
    }

    private void recordMethodExecuteInfo(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, LogInfo logInfo) {
        logInfo.setMethod(getMethod(joinPoint));
        logInfo.setArgs(joinPoint.getArgs());
        logInfo.setResult(resultAndThrowable.getResult());
        logInfo.setThrowable(resultAndThrowable.getThrowable());
        recordCustomLogInfo(logInfo);
    }

    private void recordCustomLogInfo(LogInfo logInfo) {
        logInfo.setCustomLogInfos(configuration.customLogInfoItems);
    }

    private boolean isEnableLog(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, LogInfo logInfo, boolean customLogCondition) {
        if (customLogCondition) {
            return true;
        }
        MethodLog methodLogAnno = getAnno(joinPoint);
        if (logInfo.costMills() > methodLogAnno.costMillsThreshold()) {
            return true;
        }
        if (resultAndThrowable.getThrowable() != null && methodLogAnno.logWhenException()) {
            return true;
        }
        return methodLogAnno.resultCondition().match(resultAndThrowable.getResult());
    }

    private boolean mightAnnoCondition(ProceedingJoinPoint joinPoint) {
        MethodLog anno = getAnno(joinPoint);
        return anno.logWhenException()
                || anno.costMillsThreshold() < Long.MAX_VALUE
                || anno.resultCondition() != MethodLog.ResultCondition.NO;
    }

    private void recordBefore(LogInfo logInfo) {
        logInfo.start();
    }

    private MethodLog getAnno(ProceedingJoinPoint joinPoint) {
        return ReflectionUtil.getAnno(joinPoint, MethodLog.class);
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        return ReflectionUtil.getMethod(joinPoint);
    }
}
