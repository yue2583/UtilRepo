package com.yt.costtime;


import com.yt.common.util.ReflectionUtil;
import com.yt.costtime.info.MethodCostTimeInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class CostTimeAspect {

    @Pointcut("@annotation(com.yt.costtime.CostTime))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodCostTimeInfo methodCostTimeInfo = new MethodCostTimeInfo();
        String methodName = ReflectionUtil.getMethodName(joinPoint);
        String label = ReflectionUtil.getAnno(joinPoint, CostTime.class).label();
        methodCostTimeInfo.start(methodName, label, joinPoint.getArgs());
        Object result = joinPoint.proceed();
        methodCostTimeInfo.end(result);
        methodCostTimeInfo.log();
        return result;
    }
}
