package com.android.liba.nucleus.presenter.delivery;

import com.android.liba.nucleus.view.OptionalView;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
/*import nucleus5.presenter.delivery.Delivery;
import nucleus5.view.OptionalView;

import static nucleus5.presenter.delivery.Delivery.validObservable;*/



public class DeliverCombine<View, T> implements ObservableTransformer<T, Delivery<View, T>> {
    private final Observable<OptionalView<View>> view;
    int size=1;
    public DeliverCombine(Observable<OptionalView<View>> view,int size) {
        this.view = view;
        this.size=size;
    }

    @Override
    public ObservableSource<Delivery<View, T>> apply(Observable<T> observable) {
        return observable.materialize()
                .take(size)
                .switchMap(new Function<Notification<T>, ObservableSource<Delivery<View, T>>>() {
                    @Override
                    public ObservableSource<Delivery<View, T>> apply(final Notification<T> notification) throws Exception {
                        return view.concatMap(new Function<OptionalView<View>, ObservableSource<Delivery<View, T>>>() {
                            @Override
                            public ObservableSource<Delivery<View, T>> apply(OptionalView<View> view) throws Exception {
                                return Delivery.validObservable(view, notification);
                            }
                        });
                    }
                })
                .take(size);

    }
}