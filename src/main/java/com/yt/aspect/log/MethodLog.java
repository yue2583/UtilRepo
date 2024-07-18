package com.yt.aspect.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>打印方法相关日志</p>
 * <p>具体包括：耗时，入参，返回值，异常信息</p>
 * <p>另有配置见 {@code MethodLogProperties}</p>
 * <p>不支持多线程</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

    /**
     * 指定返回值满足什么条件时打印日志
     */
    ResultCondition resultCondition() default ResultCondition.NO;

    /**
     * 指定耗时毫秒阈值，超过阈值时打印日志
     */
    long costMillsThreshold() default Long.MAX_VALUE;

    /**
     * 指定当方法执行抛出异常时打印日志
     */
    boolean logWhenException() default false;

    enum ResultCondition {
        /**
         * 任何返回值都不打印
         */
        NO,
        /**
         * 任何返回值都打印
         */
        ANY,
        /**
         * 返回值为null时打印
         */
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
