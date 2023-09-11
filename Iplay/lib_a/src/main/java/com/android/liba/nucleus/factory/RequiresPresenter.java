package com.android.liba.nucleus.factory;

import com.android.liba.nucleus.presenter.Presenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPresenter {
    Class<? extends Presenter> value();
}
