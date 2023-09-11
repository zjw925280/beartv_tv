package com.android.liba.ui.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.android.liba.context.AppContext;
import com.android.liba.exception.Error;
import com.android.liba.exception.Fail;
import com.android.liba.exception.TimeoutError;
import com.android.liba.network.ApiService;
import com.android.liba.nucleus.factory.Factory;
import com.android.liba.nucleus.presenter.RxPresenter;
import com.android.liba.nucleus.presenter.delivery.DeliverCombine;
import com.android.liba.nucleus.presenter.delivery.Delivery;
import com.android.liba.util.UIHelper;

import icepick.Icepick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;


public class BasePresent<View> extends RxPresenter<View> {

    public void handlerErro(View view, Throwable errorThrowable) {

        if (view instanceof ViewHandler) {
            ViewHandler viewHandler = (ViewHandler) view;
            viewHandler.handlerErro(errorThrowable);
        }

        if (errorThrowable.getMessage().contains("<!DOCTYPE html>")) {

        } else {
            AppContext.showToast(errorThrowable.getMessage());
        }
        UIHelper.showLog("zzzz", "errorThrowable == " + errorThrowable.toString());
        errorThrowable.printStackTrace();
    }

    public <T> void loadData(Observable<T> observable, final BiConsumer<View, T> onNext) {
        loadData(observable, onNext, (view, errorThrowable) -> {
            handlerErro(view, errorThrowable);
        });
    }

    public <T> void loadDataWithNoLifecyle(Observable<T> observable, final Consumer<T> onNext) {
        observable.subscribe(onNext, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public static final String BUNDLE_KEY_PRESENT = "BasePresent";
    private boolean isLoading;

    public boolean isLoading() {
        return isLoading;
    }

    public <T> DeliverCombine<View, T> deliverCombine(int size) {
        return new DeliverCombine<>(view(), size);
    }

    public void setLoading(boolean isLoadingDialog) {
        this.isLoading = isLoadingDialog;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        if (savedState != null && savedState.getIntegerArrayList(RxPresenter.class.getName() + "#requested") != null) {
            super.onCreate(savedState);
        }
        Icepick.restoreInstanceState(this, savedState);
        if (savedState != null && savedState.getBundle(BUNDLE_KEY_PRESENT) != null)
            handleIntent(savedState.getBundle(BUNDLE_KEY_PRESENT));
    }

    public <T extends ApiService> T getApiServer() {
        return (T) AppContext.getInstance().getApi();
    }

    public <T extends ApiService> T getApiServer(Class<T> clazz) {
        return (T) AppContext.getInstance().getApi();
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }

    public <T> void loadData(Observable<T> observable, final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, Error> onError) {
        add(observable.compose(deliverFirst()).subscribe(getSubscribe(onNext, onError)));
    }

    public <T> void loadDataCombine(Observable<T> observable, int size, final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, Error> onError) {
        add(observable.compose(deliverCombine(size)).subscribe(getSubscribe(onNext, onError)));
    }

    public <T> void loadDataCombine(Observable<T> observable, int size, final BiConsumer<View, T> onNext) {
        loadDataCombine(observable, size, onNext, (view, errorThrowable) -> {
            handlerErro(view, errorThrowable);
        });
    }

    public void loadDataCombine(final BiConsumer<View, Object> onNext, Observable<?>... observable) {
        loadDataCombine(Observable.fromArray(observable).concatMap(t -> t), observable.length, onNext, (view, errorThrowable) -> {
            handlerErro(view, errorThrowable);
        });
    }

    public void loadDataCombine(final BiConsumer<View, Object> onNext, @Nullable final BiConsumer<View, Error> onError, Observable<?>... observable) {
        loadDataCombine(Observable.fromArray(observable).concatMap(t -> t), observable.length, onNext, onError);
    }

    public <T> Consumer<Delivery<View, T>> getSubscribe(final BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, Error> onError) {
        return viewTDelivery -> {
            viewTDelivery.split(onNext, (view, throwable) -> handleError(onError, view, throwable));
        };
    }

    public <T> void restartableMyFirst(int restartableId, Factory<Observable<T>> observableFactory, BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, Error> onError) {
        super.restartableFirst(restartableId, observableFactory, onNext, (view, throwable) -> handleError(onError, view, throwable));
    }

    public <T> Factory<Disposable> getDisposableFactory(int size, Factory<Observable<T>> observableFactory, BiConsumer<View, T> onNext, @Nullable final BiConsumer<View, Error> onError) {
        return new Factory<Disposable>() {
            @Override
            public Disposable create() {
                return observableFactory.create()
                        .compose(deliverCombine(size))
                        .subscribe(split(onNext, (view, throwable) -> handleError(onError, view, throwable)));
            }
        };
    }

    private void handleError(@Nullable BiConsumer<View, Error> onError, View view, Throwable throwable) throws Exception {
        if (throwable instanceof Error) {
            Error error = (Error) throwable;
            if (throwable instanceof TimeoutError) {
                AppContext.showToast(error.getMessage());
            }
            onError.accept(view, error);
        } else {
            throwable.printStackTrace();
            onError.accept(view, new Fail(throwable.toString()));
        }
    }

    public <T> Observable<T> combineObservable(Observable<?>... observables) {
        return Observable.fromArray(observables).concatMap(t -> (Observable<T>) t);
    }

    public void handleIntent(Bundle bundle) {
    }
}
