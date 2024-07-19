package com.yt.common.util;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.yt.common.constant.StrConstant.NEXT_LINE;

public abstract class LogUtil {

    // 匹配邮箱的正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "(?<=[\\w.%+-]{2})[\\w.%+-]+(?=[\\w.%+-]{2}@\\w+\\.\\w+)");
    // 匹配电话号码的正则表达式
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(?<=\\d{3})\\d(?=\\d{4})");

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

    public static String desensitize(String str) {
        if (str == null) {
            return null;
        }
        String result = EMAIL_PATTERN.matcher(str).replaceAll("*");
        return PHONE_PATTERN.matcher(result).replaceAll("*");
    }
}
