package com.yt.common.interfaces;

import com.yt.common.util.ExUtil;

public interface InternalOpt {

    default void checkSecretKey() {
        String secretKey = getSecretKey();
        if ("yyyyyyt".equals(secretKey)) {
            return;
        }
        ExUtil.exDetail(secretKey, "密钥错误");
    }

    String getSecretKey();
}
