package com.android.liba.network;

import com.android.liba.network.compose.NetworkCompose;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.reactivex.Observable;

public class ApiProxyHandler implements InvocationHandler {
    private Object object;

    public ApiProxyHandler(Object object) {
        this.object = object;
    }

    @Override
    public Observable<?> invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SkipHandler handler = method.getAnnotation(SkipHandler.class);

        Observable<?> ob = (Observable<?>) method.invoke(object, args);
        if (handler == null) {
            Observable<?> next = ob;
            return next.compose(NetworkCompose.newCompose());
        } else {
            return ob;
        }
    }
}
