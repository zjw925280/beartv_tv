package com.android.liba.ui.base;

import android.os.Bundle;

import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.context.PermissionListener;
import com.android.liba.nucleus.factory.PresenterFactory;
import com.android.liba.nucleus.presenter.Presenter;
import com.android.liba.nucleus.view.ViewWithPresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

import icepick.Icepick;


public class AbstractFragment<P extends Presenter> extends RxFragment implements ViewWithPresenter<P> {

    private  final String PRESENTER_STATE_KEY = this.getClass().getSimpleName();
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
        return presenterDelegate.getPresenter(storeOwner);
    }

    public P getPresenter() {
        return getPresenter(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Icepick.restoreInstanceState(this, bundle);

        Bundle b = presenterDelegate.handleBundle(bundle, getArguments());
        if (b != null) {
            handleIntent(b);
        }
    }

    public void getPermission(PermissionListener listener, String... permissions) {
        presenterDelegate.getPermission(getActivity(), listener, permissions);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Icepick.saveInstanceState(this, bundle);
        bundle.putBundle(PRESENTER_STATE_KEY, presenterDelegate.getPresenterDelegate().onSaveInstanceState(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.getPresenterDelegate().onResume(this, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterDelegate.getPresenterDelegate().onDropView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.getPresenterDelegate().onDestroy(!getActivity().isChangingConfigurations() && !retentionData());
    }

    public boolean retentionData() {
        return false;
    }

    public void handleIntent(Bundle bundle) {
    }
}
