package com.yt.aspect.log;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "xxx")
public class LogMethodArgsAndResultProperties {
    private List<String> keys = Lists.newArrayList("34664228088");

    public boolean inKeys(Object key) {
        return keys.contains(String.valueOf(key));
    }
}
