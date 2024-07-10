package com.yt.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class CheckUtil {

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            ExUtil.exDetail(null, msg);
        }
    }

    public static void notNull(Object obj) {
        notNull(obj, "参数不可为null");
    }

    public static boolean anyNull(Object... obj) {
        return Arrays.stream(obj).anyMatch(Objects::isNull);
    }

    public static void noNull(Object... obj) {
       if (anyNull(obj)) {
           ExUtil.invalidMultiParam(obj);
       }
    }

    public static void couponBatchId(String couponBatchId) {
        strNotBlank(couponBatchId, "非法券批次id");
    }

    public static void strNotBlank(String str, String msg) {
        if (StrUtil.isBlank(str)) {
            ExUtil.exDetail(msg, str);
        }
    }

    public static void size(Collection<?> collection, int maxSize) {
        if (size(collection) > maxSize) {
            ExUtil.exDetail(null, "批量操作size不允许超过" + maxSize);
        }
    }

    public static int size(final Collection<?> coll) {
        if (coll == null) {
            return 0;
        }
        return coll.size();
    }

    public static void notEmpty(Collection<?> collection) {
        if (CollUtil.isEmpty(collection)) {
            ExUtil.exDetail(null, "集合不允许为空");
        }
    }

    /**
     * date1 < date2
     */
    public static void mustBefore(Date date1, Date date2, String msg) {
        if (!date1.before(date2)) {
            Object obj = "{date1:" + date1 + ",date2:" + date2 + "}";
            ExUtil.exDetail(obj, msg);
        }
    }

    /**
     * date1 >= date2
     */
    public static void notBefore(Date date1, Date date2, String msg) {
        if (date1.before(date2)) {
            Object obj = "{date1:" + date1 + ",date2:" + date2 + "}";
            ExUtil.exDetail(obj, msg);
        }
    }

    public static void mustAfter(Date date1, Date date2, String msg) {
        mustBefore(date2, date1, msg);
    }

    public static void notAfter(Date date1, Date date2, String msg) {
        notBefore(date2, date1, msg);
    }

    public static void notEmpty(Collection<?> coll, String msg) {
        if (CollUtil.isEmpty(coll)) {
            ExUtil.exDetail(coll, msg);
        }
    }

    public static void strEqual(String expect, String actual, String msg) {
        if (!expect.equals(actual)) {
            ExUtil.exDetail("expect:" + expect + ",actual:" + actual, msg);
        }
    }

    public static void listId(Long listId) {
        if (listId == null) {
            ExUtil.exDetail(null, "龙虎榜id为空");
        }
    }
}
