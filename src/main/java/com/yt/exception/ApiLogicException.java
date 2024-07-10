package com.yt.exception;


import com.yt.common.resp.ErrorCode;

import java.io.Serializable;

public class ApiLogicException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    private int errorCode;
    private String errorMessage;

    public ApiLogicException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ApiLogicException(String resMsg) {
        super(resMsg);
    }

    public ApiLogicException() {
        this.errorMessage = ErrorCode.INTERNAL_ERROR.getMessage();
        this.errorCode = ErrorCode.INTERNAL_ERROR.getErrCode();
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}