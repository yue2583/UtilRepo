package com.yt.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class MyCollUtil {

    public static <T> Collection<T> subtract(Collection<T> coll1, Collection<T> coll2) {
        final Collection<T> result = ObjectUtil.clone(coll1);
        result.removeAll(coll2);
        return result;
    }

    public static <K, V> Map<K, List<V>> groupBy(Collection<V> coll, Function<V, K> keyFunc) {
        if (CollUtil.isEmpty(coll)) {
            return new HashMap<>();
        }
        return coll.stream().collect(Collectors.groupingBy(keyFunc, Collectors.toList()));
    }

    public static <K, V, E> Map<K, V> toMap(Collection<E> coll, Func1<E, K> keyFunc, Func1<E, V> valueFunc) {
        if (CollUtil.isEmpty(coll)) {
            return new HashMap<>();
        }
        return CollUtil.toMap(coll, defaultMap(coll), keyFunc, valueFunc);
    }

    public static <K, V> Map<K, V> defaultMap(Integer size) {
        return new HashMap<>(size);
    }

    public static <K, V> Map<K, V> defaultMap(Collection<?> coll) {
        return new HashMap<>(coll.size());
    }

    public static <K, V> Map<K, V> toMap(Collection<V> coll, Func1<V, K> keyFunc) {
        if (CollUtil.isEmpty(coll)) {
            return new HashMap<>();
        }
        return CollUtil.toMap(coll, defaultMap(coll), keyFunc);
    }

    public static <T> List<T> filterNonNull(List<T> list) {
        return filter(list, Objects::nonNull);
    }

    public static <T, R> List<R> map(List<T> data, Function<T, R> mapper) {
        return CollUtil.map(data, mapper, true);
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> List<T> filterNot(List<T> list, Predicate<T> predicate) {
        return filter(list, predicate.negate());
    }

    public static boolean allEmpty(List<?>... listList) {
        for (List<?> list : listList) {
            if (CollUtil.isNotEmpty(list)) {
                return false;
            }
        }
        return true;
    }

    public static boolean anyEmpty(List<?>... listList) {
        for (List<?> list : listList) {
            if (CollUtil.isEmpty(list)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> limit(List<T> list, int size) {
        if (CollUtil.size(list) < size) {
            return list;
        }
        return list.stream().limit(size).collect(Collectors.toList());
    }
}
