package com.yt.costtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 仅简单的记录某个方法耗时，并 log 入参和结果，可使用 “MCTI” 字符串查询日志
 * 使用 EnableCostTime 开启
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CostTime {

    /**
     * label信息将在log时输出
     */
    String label() default "inner";
}