package com.yt.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static <T extends Annotation> T getAnnotation(ProceedingJoinPoint joinPoint, Class<T> annoClass) {
        return getMethod(joinPoint).getAnnotation(annoClass);
    }

    public static Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    public static void setAccessible(AccessibleObject accessibleObject) {
        if (!accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
    }

    public static Object invoke(Method method, Object target, Object... args) throws Exception {
        setAccessible(method);
        return method.invoke(target, args);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        ReflectUtils.setAccessible(field);
        return field.get(obj);
    }
}
