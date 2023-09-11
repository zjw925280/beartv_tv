package com.android.liba.util.task;

import com.android.liba.util.UIHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 子线程使用
 */
public abstract class TaskDelayBManager {

    private Disposable mDisposable;
    private long loopMs;

    public TaskDelayBManager() {
    }

    public TaskDelayBManager loop(long delayMs, long loopMs) {
        doWork(getLoopObservable(delayMs, loopMs));
        return this;
    }

    public TaskDelayBManager loop(long loopMs) {
        doWork(getLoopObservable(loopMs));
        return this;
    }

    public TaskDelayBManager delay(long delayMs) {
        doWork(getDelayObservable(delayMs));
        return this;
    }

    public TaskDelayBManager start() {
        if (!UIHelper.isRunInUIThread()) {
            onListen(0L);
        } else {
            doWork(getDelayObservable(0));
        }
        return this;
    }

    private void doWork(Observable<Long> observable) {
        observable
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long index) {
//                        aLong 0,1,2,3,4,5,6,7,8...
                        onListen(index);
                        if (loopMs == 0) {
                            cancel();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        UIHelper.showLog("TaskDelayManager", "onError");
                        if (throwable != null) throwable.printStackTrace();
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        UIHelper.showLog("TaskDelayManager", "onComplete");
                        cancel();
                    }
                });
    }

    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    private Observable<Long> getDelayObservable(long delayMs) {
        this.loopMs = 0;
        return Observable.timer(delayMs, TimeUnit.MILLISECONDS);
    }

    private Observable<Long> getLoopObservable(long delayMs, long loopMs) {
        if (loopMs <= 0) {
            return getDelayObservable(delayMs);
        } else {
            this.loopMs = loopMs;
            return Observable.interval(delayMs, loopMs, TimeUnit.MILLISECONDS);
        }
    }

    private Observable<Long> getLoopObservable(long loopMs) {
        if (loopMs <= 0) {
            return getDelayObservable(0);
        } else {
            this.loopMs = loopMs;
            return Observable.interval(loopMs, TimeUnit.MILLISECONDS);
        }
    }

    public abstract void onListen(Long index);
}