package com.android.liba.nucleus.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.nucleus.factory.PresenterFactory;
import com.android.liba.nucleus.factory.PresenterStorage;
import com.android.liba.nucleus.presenter.Presenter;


/**
 * This class adopts a View lifecycle to the Presenter`s lifecycle.
 *
 * @param <P> a type of the presenter.
 */
public final class PresenterLifecycleDelegate<P extends Presenter> {

    private static final String PRESENTER_KEY = "presenter";
    private static final String PRESENTER_ID_KEY = "presenter_id";

    @Nullable
    private PresenterFactory<P> presenterFactory;
    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    private boolean presenterHasView;

    public PresenterLifecycleDelegate(@Nullable PresenterFactory<P> presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    @Nullable
    public PresenterFactory<P> getPresenterFactory() {
        return presenterFactory;
    }

    public void setPresenterFactory(@Nullable PresenterFactory<P> presenterFactory) {
        if (presenter != null)
            throw new IllegalArgumentException("setPresenterFactory() should be called before onResume()");
        this.presenterFactory = presenterFactory;
    }

    public P getPresenter(ViewModelStoreOwner storeOwner) {
        if (presenterFactory != null) {
            if (presenter == null && bundle != null)
                presenter = PresenterStorage.INSTANCE.getPresenter(bundle.getString(PRESENTER_ID_KEY));

            if (presenter == null) {
                presenter = presenterFactory.createPresenter(storeOwner);
                PresenterStorage.INSTANCE.add(storeOwner + "-" + System.currentTimeMillis(), presenter);
                presenter.create(bundle == null ? null : bundle.getBundle(PRESENTER_KEY));
            }
            bundle = null;
        }
        return presenter;
    }

    public Bundle onSaveInstanceState(ViewModelStoreOwner storeOwner) {
        Bundle bundle = new Bundle();
        getPresenter(storeOwner);
        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenter.save(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
            bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(presenter));
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle presenterState) {
        if (presenter != null)
            throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
        this.bundle = ParcelFn.unmarshall(ParcelFn.marshall(presenterState));
    }

    public void onResume(Object view, ViewModelStoreOwner storeOwner) {
        getPresenter(storeOwner);
        if (presenter != null && !presenterHasView) {
            //noinspection unchecked
            presenter.takeView(view);
            presenterHasView = true;
        }
    }

    public void onDropView() {
        if (presenter != null && presenterHasView) {
            presenter.dropView();
            presenterHasView = false;
        }
    }

    public void onDestroy(boolean isFinal) {
        if (presenter != null && isFinal) {
            presenter.destroy();
            presenter = null;
        }
    }
}
