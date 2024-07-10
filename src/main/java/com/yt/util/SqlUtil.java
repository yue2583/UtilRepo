package com.yt.util;


import cn.hutool.core.util.StrUtil;

public abstract class SqlUtil {
    public static String genLimitSql(int page, int size) {
        int start = (page - 1) * size;
        return StrUtil.format("limit {}, {}", start, size);
    }

    public static String genLimitSql(int size) {
        return StrUtil.format("limit {}", size);
    }

    public static String limitOne() {
        return "limit 1";
    }
}
