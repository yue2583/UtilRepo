package com.yt.aspect.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class LogInfo {

    private static final ThreadLocal<AtomicInteger> ENTER_COUNT = ThreadLocal.withInitial(() -> new AtomicInteger(0));
    private final List<MethodExecuteInfo> logs = new ArrayList<>(1);

    public List<MethodExecuteInfo> getLogs() {
        return logs;
    }

    public MethodExecuteInfo get() {
        int currentMethodLogCount = getEnterCount();
        if (logs.size() < currentMethodLogCount) {
            logs.add(new MethodExecuteInfo());
        }
        return logs.get(currentMethodLogCount - 1);
    }

    public void exit() {
        enterCount().decrementAndGet();
    }

    private AtomicInteger enterCount() {
        return ENTER_COUNT.get();
    }

    public void enter() {
        enterCount().incrementAndGet();
    }

    public int getEnterCount() {
        return enterCount().get();
    }
}
