package com.yt.web.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseCode {
    SUCCESS(200, "成功"),
    DATA_NO_CHANGE(304, "使用缓存、数据没有发生变化"),
    DATA_REDIRECT(302, "数据重定向");

    private final Integer baseCode;
    private final String message;
}
