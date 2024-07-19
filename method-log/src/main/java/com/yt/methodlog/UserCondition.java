package com.yt.methodlog;

import com.yt.common.common.Condition;
import com.yt.common.util.FunctionInterfaceUtil;
import lombok.Data;

import java.util.function.Predicate;
import java.util.function.Supplier;

@Data
public class UserCondition<T> implements Condition {

    private Supplier<T> userSupplier = FunctionInterfaceUtil.getEmptySupplier();
    private Predicate<T> userHitCondition = FunctionInterfaceUtil.getFalsePredicate();

    public T getUser() {
        return userSupplier.get();
    }

    public boolean testUser(T user) {
        return userHitCondition.test(user);
    }

    @Override
    public boolean isTrue() {
        return testUser(getUser());
    }
}
