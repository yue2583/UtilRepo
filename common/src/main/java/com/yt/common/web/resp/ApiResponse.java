package com.yt.common.web.resp;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回数据封装
 */
@Data
@ApiModel("ApiResponse")
public class ApiResponse<T> {

    @ApiModelProperty(name = "code", dataType = "int", value = "返回状态码")
    private int code;

    @ApiModelProperty(name = "msg", dataType = "String", value = "具体错误信息")
    private String msg;

    private T data;

    @ApiModelProperty(name = "traceId", dataType = "String", value = "链路id")
    private String traceId;

    public static <T> ApiResponse<T> instance() {
        return new ApiResponse<>();
    }

    public static <T> ApiResponse<T> success(T t) {
        return ApiResponse.<T>instance().codeSuccess().data(t);
    }

    public static <T> ApiResponse<T> fail(T t) {
        return ApiResponse.<T>instance().codeInternalError().data(t);
    }

    /**
     * 200成功设置code
     */
    public ApiResponse<T> codeSuccess() {
        this.setCode(BaseCode.SUCCESS.getBaseCode());
        return this;
    }

    /**
     * 400参数校验失败设置code
     */
    public ApiResponse<T> codeValidateFail() {
        this.setCode(ErrorCode.VALIDATE_FAIL.getErrCode());
        return this;
    }

    /**
     * 401授权失败设置code
     */
    public ApiResponse<T> codeUnauthorized() {
        this.setCode(ErrorCode.AUTH_FAIL.getErrCode());
        return this;
    }

    /**
     * 403无权限设置code
     */
    public ApiResponse<T> codeForbidden() {
        this.setCode(ErrorCode.NO_AUTH.getErrCode());
        return this;
    }

    /**
     * 404未找到资源设置code
     */
    public ApiResponse<T> codeNotFound() {
        this.setCode(ErrorCode.NOT_FOUND.getErrCode());
        return this;
    }

    /**
     * 500普通逻辑异常设置code
     */
    public ApiResponse<T> codeInternalError() {
        this.setCode(ErrorCode.INTERNAL_ERROR.getErrCode());
        return this;
    }

    /**
     * 设置指定code，返回ApiResponse对象
     */
    public ApiResponse<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * 设置指定信息，返回ApiResponse对象
     */
    public ApiResponse<T> msg(String msg) {
        if (StrUtil.isNotEmpty(msg)) {
            this.setMsg(msg);
        }
        return this;
    }

    /**
     * 设置指定数据，返回ApiResponse对象
     */
    public ApiResponse<T> data(T data) {
        if (data != null) {
            this.setData(data);
        }
        return this;
    }

    public boolean isSuccess() {
        return BaseCode.SUCCESS.getBaseCode() == this.getCode();
    }
}
