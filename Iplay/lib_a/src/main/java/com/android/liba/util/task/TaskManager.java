package com.android.liba.util.task;

import com.android.liba.util.UIHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * runOnBackgroundThread 返回不能是 null
 * 线程切换使用
 */
public abstract class TaskManager<T> {

    private Disposable mDisposable;

    public TaskManager() {
    }

    public TaskManager<T> start() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull T t) {
                        runOnUIThread(t);
                        cancel();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        UIHelper.showLog("TaskManager", "onError " + e.getMessage());
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
        return this;
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<T> getObservable() {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<T> observableEmitter) {
                T t = runOnBackgroundThread();
                if (t == null) {
                    // t == null 时，observableEmitter.onNext会调用失败，Error
                    new TaskDelayManager() {
                        @Override
                        public void onListen(Long index) {
                            runOnUIThread(null);
                            observableEmitter.onComplete();
                            cancel();
                        }
                    }.start();
                } else {
                    observableEmitter.onNext(t);
                    observableEmitter.onComplete();
                }
            }
        });
    }

    public abstract T runOnBackgroundThread();

    public abstract void runOnUIThread(T t);
}