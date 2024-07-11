package com.yt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelStatus {
    NORMAL(10, "正常"),
    DELETE(5, "已删除");
    private final Integer code;
    private final String desc;

    public static Integer getNormal() {
        return NORMAL.code;
    }

    public static Integer getDelete() {
        return DELETE.code;
    }

    public static boolean isDelete(Integer code) {
        return DELETE.code.equals(code);
    }

    public static boolean isNormal(Integer code) {
        return NORMAL.code.equals(code);
    }
}
