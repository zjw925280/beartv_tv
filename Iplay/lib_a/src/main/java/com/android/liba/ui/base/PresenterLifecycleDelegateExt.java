package com.android.liba.ui.base;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.context.PermissionListener;
import com.android.liba.nucleus.factory.PresenterFactory;
import com.android.liba.nucleus.factory.ReflectionPresenterFactory;
import com.android.liba.nucleus.presenter.Presenter;
import com.android.liba.nucleus.view.PresenterLifecycleDelegate;
import com.android.liba.nucleus.view.ViewWithPresenter;
import com.android.liba.util.UIHelper;
import com.permissionx.guolindev.PermissionX;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class PresenterLifecycleDelegateExt<P extends Presenter> implements ViewWithPresenter<P> {
    private static final String PRESENTER_STATE_KEY = "presenter_state";

    public PresenterLifecycleDelegate<P> getPresenterDelegate() {
        return presenterDelegate;
    }

    private PresenterLifecycleDelegate<P> presenterDelegate;
    Class<?> target = null;
    private Bundle mSaveBundleInstance;

    public PresenterLifecycleDelegateExt(Class<?> target) {
        this.target = target;
        presenterDelegate = new PresenterLifecycleDelegate<>(generateFactory());
    }

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

    public PresenterFactory<P> generateFactory() {
        PresenterFactory<P> factory = ReflectionPresenterFactory.<P>fromViewClass(target);
        UIHelper.showLog("Reflection", "ReflectionPresenterFactory PresenterFactory:  " + factory);
        if (factory == null) {
            ParameterizedType genType = (ParameterizedType) this.target.getGenericSuperclass();
            Type clazz[] = genType.getActualTypeArguments();
            if (clazz[0] instanceof Class)
                factory = new ReflectionPresenterFactory((Class) clazz[0]);
            else {
                ParameterizedType ptype = (ParameterizedType) clazz[0];
                factory = new ReflectionPresenterFactory((Class) ptype.getRawType());
            }
        }
        return factory;
    }

    public Bundle handleBundle(Bundle fragmentBundle, Bundle args) {
        if (fragmentBundle != null)
            presenterDelegate.onRestoreInstanceState(fragmentBundle.getBundle(PRESENTER_STATE_KEY));
        mSaveBundleInstance = mSaveBundleInstance != null ? mSaveBundleInstance : args;
        if (mSaveBundleInstance != null) {
            fragmentBundle = fragmentBundle != null ? fragmentBundle : new Bundle();

            Bundle presentStateBundle = fragmentBundle.getBundle(PRESENTER_STATE_KEY);
            presentStateBundle = presentStateBundle != null ? presentStateBundle : new Bundle();

            Bundle presentBundle = presentStateBundle.getBundle("presenter");
            presentBundle = presentBundle != null ? presentBundle : new Bundle();

            presentBundle.putBundle(BasePresent.BUNDLE_KEY_PRESENT, mSaveBundleInstance);
            presentStateBundle.putBundle("presenter", presentBundle);
            fragmentBundle.putBundle(PRESENTER_STATE_KEY, presentStateBundle);
        }
        return mSaveBundleInstance;
    }

    public void getPermission(FragmentActivity context, PermissionListener listener, String... permissions) {
        if (listener == null) {
            return;
        }
        PermissionX.init(context)
                .permissions(permissions)
                .explainReasonBeforeRequest()
                .request((allGranted, grantedList, deniedList) -> {
                    listener.onResult(allGranted, grantedList, deniedList);
                });
    }
}
