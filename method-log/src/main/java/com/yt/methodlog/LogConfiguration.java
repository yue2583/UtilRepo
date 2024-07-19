package com.yt.methodlog;

import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LogConfiguration {

    /**
     * 自定义日志信息
     */
    List<LogInfoItem> customLogInfoItems;
    /**
     * 自定义日志打印条件
     */
    List<LogCondition> customLogConditions;

    @Bean
    public LogAspect logAspect() {
        LogAspect logAspect = new LogAspect();
        logAspect.configuration = this;
        return logAspect;
    }

    private void addLogInfoItem(LogInfoItem logInfoItem) {
        if (customLogInfoItems == null) {
            customLogInfoItems = new ArrayList<>();
        }
        customLogInfoItems.add(logInfoItem);
    }

    public void addLogInfoItemWithDefaultFormatter(Supplier<Object> logInfoSupplier) {
        addLogInfoItem(new LogInfoItem() {
            @Override
            public Supplier<Object> supplier() {
                return logInfoSupplier;
            }

            @Override
            public Formatter formatter() {
                return Formatter.DEFAULT;
            }
        });
    }

    public void addLogCondition(LogCondition logCondition) {
        if (customLogConditions == null) {
            customLogConditions = new ArrayList<>();
        }
        customLogConditions.add(logCondition);
    }
}
