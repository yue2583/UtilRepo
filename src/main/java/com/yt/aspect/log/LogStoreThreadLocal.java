package com.yt.aspect.log;

import com.yt.util.ExUtil;

public class LogStoreThreadLocal {

    private static final ThreadLocal<LogStore> LOG_STORE_CONTAINER = new ThreadLocal<>();

    public static LogStore enter() {
        if (LOG_STORE_CONTAINER.get() != null) {
            ExUtil.ex("LogStoreThreadLocal.enter() called twice");
        }
        LogStore logStore = new LogStore();
        LOG_STORE_CONTAINER.set(logStore);
        return logStore;
    }

    public static void exit() {
        LOG_STORE_CONTAINER.remove();
    }
}
