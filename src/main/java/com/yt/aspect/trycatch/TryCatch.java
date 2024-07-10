package com.yt.aspect.trycatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于简单的try catch一个方法
 * <br>
 * 并做出以下处理：
 * 1、打印异常信息
 * 2、打印方法所属类，方法名，参数
 * 3、返回null
 * <br>
 * 注意：
 * 使用该注解的方法不能返回基本类型，可以返回void
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TryCatch {
}
