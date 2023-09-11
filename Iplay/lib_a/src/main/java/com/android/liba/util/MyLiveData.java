package com.android.liba.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class MyLiveData<T> extends LiveData<T> {

    public MyLiveData() {
    }

    public MyLiveData(T value) {
        super(value);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        UIHelper.runOnUIThread(() -> super.observe(owner, observer));
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        UIHelper.runOnUIThread(() -> super.observeForever(observer));
    }

    @Override
    public void setValue(T value) {
        //要执行于主线程
        UIHelper.runOnUIThread(() -> super.setValue(value));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        UIHelper.runOnUIThread(() -> super.removeObserver(observer));
    }

    @Override
    public void removeObservers(@NonNull LifecycleOwner owner) {
        UIHelper.runOnUIThread(() -> super.removeObservers(owner));
    }
}
