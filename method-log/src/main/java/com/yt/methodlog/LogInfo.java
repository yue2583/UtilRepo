package com.yt.methodlog;

import cn.hutool.core.util.StrUtil;
import com.yt.common.util.LogUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.yt.common.constant.StrConstant.NEXT_LINE;

@Slf4j
@Data
public class LogInfo {

    private static final Formatter FORMATTER = Formatter.DEFAULT;
    private Boolean enable;
    private Long startTime;
    private Long endTime;
    private Method method;
    private Object[] args;
    private Object result;
    private Throwable throwable;
    private List<LogInfoItem> customLogInfos;

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
        String logStr = joinMultiLog(title(), customInfo(), fixedInfo());
        log.info(LogUtil.desensitize(NEXT_LINE + logStr));
    }

    private String customInfo() {
        if (customLogInfos == null || customLogInfos.isEmpty()) {
            return "";
        }
        String[] customLogInfoStrArr = customLogInfos.stream().map(i ->
                i.formatter().format(i.supplier().get())).toArray(String[]::new);
        return showLogItem("自定义信息", joinMultiLog(customLogInfoStrArr));
    }

    private String showLogItem(String label, String logInfo) {
        return StrUtil.format("[{}] {}", label, logInfo);
    }

    private String fixedInfo() {
        return joinMultiLog(time(), args(), result(), throwable());
    }

    private String joinMultiLog(String... logs) {
        if (logs == null || logs.length == 0) {
            return "";
        }
        return Arrays.stream(logs).filter(StrUtil::isNotBlank).collect(Collectors.joining(NEXT_LINE));
    }

    private String result() {
        return showLogItem("结果", FORMATTER.format(result));
    }

    private String throwable() {
        if (throwable == null) {
            return "";
        }
        return StrUtil.format("异常", LogUtil.logThrowable(throwable));
    }

    private String time() {
        return showLogItem("耗时", costMills() + "ms");
    }

    private String args() {
        return showLogItem("入参", FORMATTER.format(args));
    }

    private String title() {
        return TitleTemplateHelper.buildTitle(className(), method);
    }

    private String className() {
        return getClass().getSimpleName();
    }

    private static class TitleTemplateHelper {
        private static final char FILL_SYMBOL = '=';
        private static final String TITLE_TEMPLATE;

        static {
            int halfLineLength = 20;
            String halfLine = StrUtil.repeat(FILL_SYMBOL, halfLineLength);
            String titleTop = halfLine + " {} " + halfLine;
            String titleMiddle = "{}    {}";
            String titleBottom = halfLine + "{}" + halfLine;
            TITLE_TEMPLATE = String.join(NEXT_LINE, titleTop, titleMiddle, titleBottom);
        }


        public static String buildTitle(String title, Method method) {
            return StrUtil.format(TITLE_TEMPLATE,
                    title,
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    StrUtil.repeat(FILL_SYMBOL, title.length() + 2));
        }
    }
}
