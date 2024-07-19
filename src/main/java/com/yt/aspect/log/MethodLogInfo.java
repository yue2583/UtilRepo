package com.yt.aspect.log;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.yt.util.LogUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.yt.constant.StrConstant.NEXT_LINE;

@Slf4j
@Data
public class MethodLogInfo {

    private static final String FILL_SYMBOL = "=";
    private static final int HALF_TITLE_LENGTH = 20;
    private static final String TITLE_TEMPLATE =
            StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + " {} " + StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + NEXT_LINE +
                    "{}    {}    {}" + NEXT_LINE +
                    StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH) + "{}" + StrUtil.repeat(FILL_SYMBOL, HALF_TITLE_LENGTH);

    private static final Gson gson = new Gson();

    private Long uid;
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
        log.info(NEXT_LINE + format(title(), time(), args(), result(), throwable()));
    }

    private String format(String... logs) {
        if (logs == null || logs.length == 0) {
            return "";
        }
        return Arrays.stream(logs).filter(StrUtil::isNotBlank).collect(Collectors.joining(NEXT_LINE));
    }

    private String result() {
        return StrUtil.format("结果：{}", gson.toJson(result));
    }

    private String throwable() {
        if (throwable == null) {
            return "";
        }
        return StrUtil.format("异常：{}", LogUtil.logThrowable(throwable));
    }

    private String time() {
        return StrUtil.format("耗时：{}ms     开始时间：{} 结束时间：{}", costMills(), startTime, endTime);
    }

    private String args() {
        return StrUtil.format("入参：{}", gson.toJson(args));
    }

    /**
     * 类似：
     * <br>==================MethodLogInfo==================
     * <br>MethodLogInfo    log    10086
     * <br>=================================================
     */
    private String title() {
        return StrUtil.format(TITLE_TEMPLATE,
                className(),
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                uid,
                StrUtil.repeat(FILL_SYMBOL, className().length() + 2));
    }

    private String className() {
        return getClass().getSimpleName();
    }
}
