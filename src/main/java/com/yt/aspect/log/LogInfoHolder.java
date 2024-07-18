package com.yt.aspect.log;

class LogInfoHolder {

    private static final ThreadLocal<LogInfo> LOG_STORE = new ThreadLocal<>();

    public static LogInfo get() {
        if (LOG_STORE.get() == null) {
            LOG_STORE.set(new LogInfo());
        }
        return LOG_STORE.get();
    }
}
