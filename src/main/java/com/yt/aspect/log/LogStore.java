package com.yt.aspect.log;

public class LogStore {

    private static final ThreadLocal<LogStore> LOG_STORE_CONTAINER = new ThreadLocal<>();
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public static LogStore get() {
        if (LOG_STORE_CONTAINER.get() == null) {
            LOG_STORE_CONTAINER.set(new LogStore());
        }
        return LOG_STORE_CONTAINER.get();
    }

    public static void exit() {
        LOG_STORE_CONTAINER.remove();
    }


}
