package com.android.liba.nucleus.factory;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.liba.nucleus.presenter.Presenter;

/**
 * This class represents a {@link PresenterFactory} that creates a presenter using {@link Class#newInstance()} method.
 *
 * @param <P> the type of the presenter.
 */
public class ReflectionPresenterFactory<P extends Presenter> implements PresenterFactory<P> {

    private Class<P> presenterClass;
    ViewModelProvider viewModelProvider;
    /**
     * This method returns a {@link ReflectionPresenterFactory} instance if a given view class has
     * a {@link RequiresPresenter} annotation, or null otherwise.
     *
     * @param viewClass a class of the view
     * @param <P>       a type of the presenter
     * @return a {@link ReflectionPresenterFactory} instance that is supposed to create a presenter from {@link RequiresPresenter} annotation.
     */
    @Nullable
    public static <P extends Presenter> ReflectionPresenterFactory<P> fromViewClass(Class<?> viewClass) {
        RequiresPresenter annotation = viewClass.getAnnotation(RequiresPresenter.class);
        //noinspection unchecked
        Class<P> presenterClass = annotation == null ? null : (Class<P>)annotation.value();
        return presenterClass == null ? null : new ReflectionPresenterFactory<>(presenterClass);
    }

    public ReflectionPresenterFactory(Class<P> presenterClass) {
        this.presenterClass = presenterClass;
    }
    public <T extends ViewModel> T getViewModel(ViewModelStoreOwner activity, @NonNull Class<? extends ViewModel> modelClass) {
        synchronized (this) {
            if(viewModelProvider==null)
                viewModelProvider = new ViewModelProvider(activity);
        }

        String key=activity.getClass().getSimpleName()+"_"+modelClass.getSimpleName();
        return (T)viewModelProvider.get(key,modelClass);
    }
    @Override
    public P createPresenter(ViewModelStoreOwner activity) {

        try {
            return getViewModel( activity,presenterClass);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
