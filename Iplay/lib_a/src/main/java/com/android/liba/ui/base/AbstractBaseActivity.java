package com.android.liba.ui.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.context.PermissionListener;
import com.android.liba.nucleus.factory.PresenterFactory;
import com.android.liba.nucleus.presenter.Presenter;
import com.android.liba.nucleus.view.ViewWithPresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import icepick.Icepick;


class AbstractBaseActivity<P extends Presenter> extends RxAppCompatActivity implements ViewWithPresenter<P> {

    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private PresenterLifecycleDelegateExt<P> presenterDelegate =
            new PresenterLifecycleDelegateExt<>(getClass());

    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    public P getPresenter(ViewModelStoreOwner storeOwner) {
        return presenterDelegate.getPresenter(this);
    }

    public P getPresenter() {
        return presenterDelegate.getPresenter(this);
    }

    public void getPermission(PermissionListener listener, String... permissions) {
        presenterDelegate.getPermission(this, listener, permissions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        Bundle bundle = presenterDelegate.handleBundle(savedInstanceState, (getIntent() != null ? getIntent().getExtras() : null));
        if (bundle != null)
            handleIntent(bundle);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.getPresenterDelegate().onSaveInstanceState(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenterDelegate.getPresenterDelegate().onResume(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.getPresenterDelegate().onDropView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterDelegate.getPresenterDelegate().onDestroy(!isChangingConfigurations());
    }

    public void handleIntent(Bundle bundle) {
    }
}
