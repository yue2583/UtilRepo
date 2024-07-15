package com.yt.interfaces;


import com.yt.util.ExUtil;

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
