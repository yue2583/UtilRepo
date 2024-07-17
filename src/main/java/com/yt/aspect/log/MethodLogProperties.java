package com.yt.aspect.log;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "yt-method-log")
public class MethodLogProperties {
    /**
     * 方法日志总开关
     */
    private Boolean enable = false;
    /**
     * 命中指定用户时打印方法日志
     */
    private Set<Long> uids = Sets.newHashSet(34664228088L);

    public boolean enable() {
        return enable;
    }

    public boolean inUids(Long uid) {
        return uids.contains(uid);
    }
}
