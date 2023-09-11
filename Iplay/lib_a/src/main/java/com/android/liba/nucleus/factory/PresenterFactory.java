package com.android.liba.nucleus.factory;


import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.nucleus.presenter.Presenter;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter(ViewModelStoreOwner storeOwner);
}
