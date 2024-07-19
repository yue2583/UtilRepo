package com.yt.aspect.log;

import com.google.gson.Gson;
import com.yt.util.LogUtil;

public interface Formatter {

    Gson gson = new Gson();
    Formatter DEFAULT = obj -> {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Throwable) {
            return LogUtil.logThrowable((Throwable) obj);
        }
        return gson.toJson(obj);
    };

    String format(Object obj);
}
