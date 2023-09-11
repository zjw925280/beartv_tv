package com.android.liba.nucleus.view;


import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.nucleus.factory.PresenterFactory;
import com.android.liba.nucleus.presenter.Presenter;

public interface ViewWithPresenter<P extends Presenter> {

    PresenterFactory<P> getPresenterFactory();

    void setPresenterFactory(PresenterFactory<P> presenterFactory);

    P getPresenter(ViewModelStoreOwner storeOwner);

}
