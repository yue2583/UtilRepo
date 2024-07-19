package com.yt.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class LogUtil {

    private static final String NEXT_LINE = "\r\n";

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
