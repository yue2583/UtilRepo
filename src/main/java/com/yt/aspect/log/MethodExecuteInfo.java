package com.yt.aspect.log;

import java.lang.reflect.Method;

class MethodExecuteInfo {

    boolean enable;
    long startTime;
    long endTime;
    Class<?> clazz;
    Method method;
    Object[] args;
    Object result;
    Throwable throwable;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long costMills() {
        return endTime - startTime;
    }
}
