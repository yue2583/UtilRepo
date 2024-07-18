package com.yt.aspect.log;


import com.yt.util.ReflectionUtils;
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

        LogInfo logInfo = LogInfoHolder.get();
        logInfo.enter();

        recordBefore(logInfo);
        ResultAndThrowable resultAndThrowable = tryCatchExecute(joinPoint);
        recordAfter(joinPoint, resultAndThrowable, logInfo);

        logInfo.exit();
        LogFormatter.format(logInfo);

        if (resultAndThrowable.throwable != null) {
            throw resultAndThrowable.throwable;
        }
        return resultAndThrowable.result;
    }

    private ResultAndThrowable tryCatchExecute(ProceedingJoinPoint joinPoint) {
        try {
            return ResultAndThrowable.result(joinPoint.proceed());
        } catch (Throwable t) {
            return ResultAndThrowable.throwable(t);
        }
    }

    private void recordAfter(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, LogInfo logInfo) {
        MethodExecuteInfo methodExecuteInfo = logInfo.get();
        methodExecuteInfo.end();
        methodExecuteInfo.enable = enableAfter(getAnno(joinPoint), resultAndThrowable, methodExecuteInfo.costMills());
        if (!methodExecuteInfo.enable) {
            return;
        }
        recordMethodExecuteInfo(joinPoint, resultAndThrowable, methodExecuteInfo);
    }

    private void recordMethodExecuteInfo(ProceedingJoinPoint joinPoint, ResultAndThrowable resultAndThrowable, MethodExecuteInfo methodExecuteInfo) {
        Method method = getMethod(joinPoint);
        methodExecuteInfo.clazz = method.getDeclaringClass();
        methodExecuteInfo.method = method;
        methodExecuteInfo.args = joinPoint.getArgs();
        methodExecuteInfo.result = resultAndThrowable.result;
        methodExecuteInfo.throwable = resultAndThrowable.throwable;
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

    private void recordBefore(LogInfo logInfo) {
        logInfo.get().start();
    }

    private MethodLog getAnno(ProceedingJoinPoint joinPoint) {
        return ReflectionUtils.getAnno(joinPoint, MethodLog.class);
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        return ReflectionUtils.getMethod(joinPoint);
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
