package com.yt.common;

import lombok.Data;

@Data
public class ResultAndThrowable {

    private Object result;
    private Throwable throwable;

    public ResultAndThrowable(Object result, Throwable throwable) {
        this.result = result;
        this.throwable = throwable;
    }

    public static ResultAndThrowable result(Object result) {
        return new ResultAndThrowable(result, null);
    }

    public static ResultAndThrowable throwable(Throwable throwable) {
        return new ResultAndThrowable(null, throwable);
    }
}
