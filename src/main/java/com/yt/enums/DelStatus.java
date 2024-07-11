package com.yt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelStatus {
    NOT_DELETE(10, "未删除"),
    DELETE(5, "已删除");
    private final Integer code;
    private final String desc;

    public static Integer getNotDelete() {
        return NOT_DELETE.code;
    }

    public static Integer getDelete() {
        return DELETE.code;
    }

    public static boolean isDelete(Integer code) {
        return DELETE.code.equals(code);
    }

    public static boolean isNotDelete(Integer code) {
        return NOT_DELETE.code.equals(code);
    }
}
