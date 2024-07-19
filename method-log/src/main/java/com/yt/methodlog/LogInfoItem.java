package com.yt.methodlog;

import java.util.function.Supplier;

/**
 * 自定义一个需要打印的日志信息项
 */
public interface LogInfoItem {
    Supplier<Object> supplier();

    Formatter formatter();
}
