package com.yt.aspect.log;


import com.yt.util.ReflectionUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
@AllArgsConstructor
public class LogAspect {

    private MethodLogProperties properties;

    @Pointcut("@annotation(com.yt.aspect.log.MethodLog)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!enableBefore(joinPoint)) {
            return joinPoint.proceed();
        }

        MethodLogInfo methodLogInfo = MethodLogInfoHolder.get();
        try {
            return doAround(joinPoint, methodLogInfo);
        } finally {
            MethodLogInfoHolder.remove();
        }
    }

    private Object doAround(ProceedingJoinPoint joinPoint, MethodLogInfo methodLogInfo) throws Throwable {
        recordBefore(methodLogInfo);
        ResultAndThrowable resultAndThrowable = execute(joinPoint);
        recordAfter(joinPoint, resultAndThrowable, methodLogInfo);
        methodLogInfo.log();

        if (resultAndThrowable.throwable != null) {
            throw resultAndThrowable.throwable;
        }
        return resultAndThrowable.result;
    }

    private ResultAndThrowable execute(ProceedingJoinPoint joinPoint) {
        try {
            return ResultAndThrowable.result(joinPoint.proceed());
        } catch (Throwable t) {
            return ResultAndThrowable.throwable(t);
        }
    }

    private void recordAfter(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, MethodLogInfo methodLogInfo) {
        methodLogInfo.end();
        methodLogInfo.setEnable(enableAfter(getAnno(joinPoint), resultAndThrowable, methodLogInfo.costMills()));
        if (!methodLogInfo.getEnable()) {
            return;
        }
        recordMethodExecuteInfo(joinPoint, resultAndThrowable, methodLogInfo);
    }

    private void recordMethodExecuteInfo(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, MethodLogInfo methodLogInfo) {
        methodLogInfo.setMethod(getMethod(joinPoint));
        methodLogInfo.setArgs(joinPoint.getArgs());
        methodLogInfo.setResult(resultAndThrowable.result);
        methodLogInfo.setThrowable(resultAndThrowable.throwable);
    }

    private boolean enableAfter(MethodLog methodLogAnno, ResultAndThrowable resultAndThrowable, long costMills) {
        if (costMills > methodLogAnno.costMillsThreshold()) {
            return true;
        }
        if (resultAndThrowable.throwable != null && methodLogAnno.logWhenException()) {
            return true;
        }
        return methodLogAnno.resultCondition().match(resultAndThrowable.result);
    }

    private boolean enableBefore(ProceedingJoinPoint joinPoint) {
        return mainSwitchOn() && mightHitCondition(getAnno(joinPoint));
    }

    private boolean mightHitCondition(MethodLog anno) {
        return properties.inUids(getUid())
                || anno.logWhenException()
                || anno.costMillsThreshold() < Long.MAX_VALUE
                || anno.resultCondition() != MethodLog.ResultCondition.NO;
    }

    private boolean mainSwitchOn() {
        return properties.enable();
    }

    private Long getUid() {
        // todo 待实现
        return null;
    }

    private void recordBefore(MethodLogInfo methodLogInfo) {
        methodLogInfo.start();
    }

    private MethodLog getAnno(ProceedingJoinPoint joinPoint) {
        return ReflectionUtil.getAnno(joinPoint, MethodLog.class);
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        return ReflectionUtil.getMethod(joinPoint);
    }

    private static class ResultAndThrowable {
        Object result;
        Throwable throwable;

        public ResultAndThrowable(Object result, Throwable throwable) {
            this.result = result;
            this.throwable = throwable;
        }

        public static ResultAndThrowable result(Object result) {
            return new ResultAndThrowable(result, null);
        }

        public static ResultAndThrowable throwable(Throwable throwable) {
            return new ResultAndThrowable(null, throwable);
        }
    }
}
