package com.yt.aspect.log;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(MethodLogProperties.class)
public class MethodLogConfiguration {

    @Bean
    public LogAspect logAspect(MethodLogProperties methodLogProperties) {
        methodLogProperties.setEnable(true);
        return new LogAspect(methodLogProperties);
    }
}
