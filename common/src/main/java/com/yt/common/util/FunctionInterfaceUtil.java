package com.yt.common.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class FunctionInterfaceUtil {

    public static <T> Supplier<T> getEmptySupplier() {
        return () -> null;
    }

    public static <T> Predicate<T> getFalsePredicate() {
        return (T obj) -> false;
    }
}
