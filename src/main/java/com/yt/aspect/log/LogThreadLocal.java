package com.yt.aspect.log;

import cn.hutool.core.util.BooleanUtil;

public class LogThreadLocal {

    private static final ThreadLocal<Boolean> DO_LOG = new ThreadLocal<>();

    public static void doLog() {
        DO_LOG.set(true);
    }

    public static void clear() {
        DO_LOG.remove();
    }

    public static boolean isDoLog() {
        return BooleanUtil.isTrue(DO_LOG.get());
    }
}
