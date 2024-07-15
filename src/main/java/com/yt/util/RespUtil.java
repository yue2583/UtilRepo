package com.yt.util;

import com.yt.web.resp.ApiResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RespUtil {

    public static <T> T getData(ApiResponse<T> resp) {
        if (resp == null || !resp.isSuccess()) {
            return null;
        }
        return resp.getData();
    }

    public static <T> List<T> getListData(ApiResponse<List<T>> resp) {
        if (resp == null
                || !resp.isSuccess()
                || resp.getData() == null) {
            return new ArrayList<>();
        }
        return resp.getData();
    }

    public static <K, V> Map<K, V> getMapData(ApiResponse<Map<K, V>> resp) {
        if (resp == null
                || !resp.isSuccess()
                || resp.getData() == null) {
            return new HashMap<>();
        }
        return resp.getData();
    }
}
