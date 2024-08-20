package com.yt.costtime.info;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MethodCostTimeInfo {

    private String methodName;
    private String label;
    private long startTime;
    private long endTime;
    private Object args;
    private Object result;

    public void start(String methodName, String label, Object args) {
        this.methodName = methodName;
        this.label = label;
        this.startTime = System.currentTimeMillis();
        this.args = args;
    }

    public void end(Object result) {
        this.endTime = System.currentTimeMillis();
        this.result = result;
    }

    public void log() {
        String info = StrUtil.format(
                "{} method: {}\n" +
                        "cost {}ms\n" +
                        "args: {}\n" +
                        "result: {}",
                label, methodName, endTime - startTime, args, result);
        log.info(info);
    }
}
