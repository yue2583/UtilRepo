package com.yt.util;

import cn.hutool.core.util.StrUtil;
import com.yt.exception.ApiLogicException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExUtil {

    public static void exDetail(Object obj, String msg) {
        throw new ApiLogicException(StrUtil.format("{}:{}", msg, obj));
    }

    public static void invalidParam(Object... params) {
        exDetail(StrUtil.join("\n", params), "非法参数");
    }

    public static void invalidNullParam() {
        invalidParam(new Object[]{null});
    }
}
