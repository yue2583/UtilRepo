package com.yt.costtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录方法耗时，调用链路的第一个方法被视为根方法
 * 使用 EnableCostTime 开启
 * 不同 label 的方法耗时将被分组统计
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CostTime {
    String label() default "inner";
}
