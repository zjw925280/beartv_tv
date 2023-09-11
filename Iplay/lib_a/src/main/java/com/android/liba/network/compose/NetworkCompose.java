package com.android.liba.network.compose;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.liba.R;
import com.android.liba.context.AppContext;
import com.android.liba.exception.Error;
import com.android.liba.exception.Fail;
import com.android.liba.exception.NetworkError;
import com.android.liba.network.ApiManager;
import com.google.gson.JsonParseException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class NetworkCompose<T> implements ObservableTransformer<T, T> {
    private NetworkCompose() {
    }

    public static NetworkCompose newCompose() {
        return new NetworkCompose();
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return isNetworkAvailableObservable()
                .flatMap(aBoolean -> upstream)
                //    .flatMap(new ReadResponseFunc())
                .onErrorResumeNext(errorHandler())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Function<Throwable, ? extends ObservableSource<? extends T>> errorHandler() {
        return throwable -> {
            ApiManager.getApiManager().onHttpError(throwable);

            if (throwable instanceof JsonParseException) {
                return Observable.error(new Fail(throwable.getMessage()));
            } else if (throwable instanceof retrofit2.HttpException || throwable instanceof UnknownHostException || throwable instanceof ConnectException || throwable instanceof SocketTimeoutException) {
                return Observable.error(new Fail(AppContext.getInstance().getString(R.string.error_server_unknown)));
            } else if (throwable instanceof NetworkError) {
                return Observable.error(new NetworkError());
            } else if (throwable instanceof Error) {
                return Observable.error(throwable);
            } else {
                String error = throwable == null ? AppContext.getInstance().getString(R.string.error_unknown) : throwable.toString();
                return Observable.error(new UnknownError(error));
            }
        };
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) AppContext.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    public static Observable<Boolean> isNetworkAvailableObservable() {
        return Observable.just(isNetworkAvailable()).flatMap(t -> {
            if (t)
                return Observable.just(true);
            else
                return Observable.error(new NetworkError());
        });
    }
}
