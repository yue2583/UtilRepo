package com.yt.aspect.log;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.yt.util.LogUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@Data
public class MethodLogInfo {

    private static final String FILL_SYMBOL = "=";
    private static final int HALF_TITLE_LENGTH = 20;
    private static final String NEXT_LINE = "\r\n";
    private static final String TITLE_TEMPLATE =
            StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + " {} " + StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + NEXT_LINE +
                    "{}    {}" + NEXT_LINE +
                    StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + "{}" + StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH);

    private static final Gson gson = new Gson();

    private Boolean enable;
    private Long startTime;
    private Long endTime;
    private Method method;
    private Object[] args;
    private Object result;
    private Throwable throwable;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long costMills() {
        return endTime - startTime;
    }

    public void log() {
        if (!enable) {
            return;
        }
        log.info(title() + NEXT_LINE +
                cost() + NEXT_LINE +
                args() + NEXT_LINE +
                result() + NEXT_LINE +
                throwable()
        );
    }

    private String result() {
        return StrUtil.format("结果：{}", gson.toJson(result));
    }

    private String throwable() {
        return StrUtil.format("异常：{}", LogUtil.logThrowable(throwable));
    }

    private String cost() {
        return StrUtil.format("耗时：{}ms", costMills());
    }

    private String args() {
        return StrUtil.format("入参：{}", gson.toJson(args));
    }

    /**
     * 类似：
     * <br>==================MethodLogInfo==================
     * <br>MethodLogInfo    log
     * <br>=================================================
     */
    private String title() {
        return StrUtil.format(TITLE_TEMPLATE,
                className(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                StrUtil.repeat(FILL_SYMBOL, className().length() + 2));
    }

    private String className() {
        return getClass().getSimpleName();
    }
}
