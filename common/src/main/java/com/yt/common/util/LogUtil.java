package com.yt.common.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.yt.common.constant.StrConstant.NEXT_LINE;

public abstract class LogUtil {

    public static String logThrowable(Throwable t) {
        if (t == null) {
            return "";
        }
        return t.getMessage() + NEXT_LINE + formatStackTrace(t.getStackTrace());
    }

    private static CharSequence formatStackTrace(StackTraceElement[] stackTrace) {
        if (stackTrace == null || stackTrace.length == 0) {
            return "";
        }
        return Arrays.stream(stackTrace).map(StackTraceElement::toString).collect(Collectors.joining(NEXT_LINE));
    }
}
