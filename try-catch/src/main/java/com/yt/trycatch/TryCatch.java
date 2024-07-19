package com.yt.trycatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 简单地try catch一个方法，捕获Exception级别的异常并打印相关信息，返回null
 * 不会捕获Throwable级别的异常
 * <br>
 * 注意：
 * 使用该注解的方法不能返回基本类型，可以返回void
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TryCatch {
}
