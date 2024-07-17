package com.yt.aspect.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打印方法相关日志
 * 具体包括：耗时，入参，返回值
 * 可配置总开关，默认关闭：yt-method-log.enable=true
 * 可配置条件开关：指定uid，指定返回值，指定耗时阈值
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

    /**
     * 打印日志需要满足的条件
     * 除了指定特定的条件外，还可以完全自定义，通过调用{@link LogThreadLocal#doLog()}来要求打印日志
     * 注意{@link LogThreadLocal#doLog()}的效果一旦被判定会立刻失效{@link LogAspect}
     */
    Condition[] conditions() default {};

    int keyArgsIndex() default 0;

    String keyName() default "uid";

    String parseKeyMethodName() default "";

    enum Condition {
        RESULT_IS_NULL,
        RESULT_IS_TRUE,
        RESULT_IS_FALSE,
        /**
         * 配合 keyArgsIndex, keyName, parseKeyMethodName 使用
         * 如果未指定 parseKeyMethodName，依次尝试获取指定参数本身，指定参数中的某个字段作为key
         * 如果指定了 parseKeyMethodName，使用方法获取key
         */
        ARGS_HIT_KEY,
        ALL,
        ;
    }
}
