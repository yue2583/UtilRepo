package com.yt.util;

import cn.hutool.core.util.StrUtil;
import com.yt.exception.ApiLogicException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExUtil {

    public static void exDetail(Object obj, String msg) {
        ex(StrUtil.format("{}:{}", msg, obj));
    }

    public static void exNull() {
        exNull("参数不能为null");
    }

    public static void exNull(String msg) {
        exDetail(null, msg);
    }

    public static void invalidParam(Object... params) {
        exDetail(StrUtil.join("\n", params), "非法参数");
    }


    public static void ex(String msg) {
        throw new ApiLogicException(msg);
    }

    public static void invalidNullParam() {
        invalidParam(new Object[]{null});
    }
}
