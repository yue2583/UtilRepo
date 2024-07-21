package com.yt.methodlog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 满足条件时打印方法日志
 * 用于工单排查和慢请求分析
 * <br>
 * 通过com.yt.methodlog.LogConfiguration#addLogInfoItemWithDefaultFormatter(java.util.function.Supplier)
 * 添加获取uid的方法来在日志中打印uid
 * <br>
 * 通过com.yt.methodlog.LogConfiguration#addLogCondition(com.yt.methodlog.LogCondition)
 * 添加判断uid是否在灰度的方法，当该用户访问时打印日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

    ResultCondition resultCondition() default ResultCondition.NO;

    long costMillsThreshold() default Long.MAX_VALUE;

    boolean logWhenException() default false;

    enum ResultCondition {
        NO,
        ANY,
        NULL,
        TRUE,
        FALSE,
        NOT_NULL,
        NOT_TRUE,
        NOT_FALSE,
        ;

        public boolean match(Object result) {
            if (this == NO) {
                return false;
            }
            if (this == ANY) {
                return true;
            }
            if (this == NULL) {
                return result == null;
            }
            if (this == TRUE) {
                return Boolean.TRUE.equals(result);
            }
            if (this == FALSE) {
                return Boolean.FALSE.equals(result);
            }
            if (this == NOT_NULL) {
                return result != null;
            }
            if (this == NOT_TRUE) {
                return !Boolean.TRUE.equals(result);
            }
            if (this == NOT_FALSE) {
                return !Boolean.FALSE.equals(result);
            }
            return false;
        }
    }
}
