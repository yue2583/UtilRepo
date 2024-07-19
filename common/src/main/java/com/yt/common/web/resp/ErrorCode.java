package com.yt.common.web.resp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    VALIDATE_FAIL(400, "非法参数校验异常"),
    AUTH_FAIL(401, "授权失败异常"),
    NO_AUTH(403, "权限校验异常"),
    NOT_FOUND(404, "未找到资源异常"),
    INTERNAL_ERROR(500, "系统内部异常,请稍后重试"),
    BAD_GATEWAY(502, "服务器异常"),
    UN_SUPPORTED_HEAD(415, "参数缺少首部字段"),
    ANTI_REPTILE(430, "谨慎！非法请求！");

    private final Integer errCode;
    private final String message;
}
