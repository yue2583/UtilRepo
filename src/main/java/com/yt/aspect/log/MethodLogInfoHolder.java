package com.yt.aspect.log;

class MethodLogInfoHolder {

    private static final ThreadLocal<MethodLogInfo> LOG_INFO = new ThreadLocal<>();

    public static MethodLogInfo get() {
        if (LOG_INFO.get() == null) {
            LOG_INFO.set(new MethodLogInfo());
        }
        return LOG_INFO.get();
    }

    public static void remove() {
        LOG_INFO.remove();
    }
}
