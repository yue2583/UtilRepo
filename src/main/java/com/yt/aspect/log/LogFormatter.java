package com.yt.aspect.log;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class LogFormatter {

    public static void format(LogInfo logInfo) {
        if (0 != logInfo.getEnterCount()) {
            return;
        }
        logOverview(logInfo);
        logArgsAngResult(logInfo);
        logThrowable(logInfo);
    }

    private static void logThrowable(LogInfo logInfo) {
        // 只打印最外层异常
    }

    private static void logArgsAngResult(LogInfo logInfo) {
    }

    /**
     * 打印概览
     * 包括：方法名、耗时、执行次数、所属类
     */
    private static void logOverview(LogInfo logInfo) {
        Overview overview = parseOverview(logInfo);
        List<List<String>> data = formatToTwoDimensionalList(overview);
        log.info(render(data));
    }

    private static List<List<String>> formatToTwoDimensionalList(Overview overview) {
        return null;
    }

    private static Overview parseOverview(LogInfo logInfo) {
        return null;
    }

    private static String render(List<List<String>> data) {
        return null;
    }
}
