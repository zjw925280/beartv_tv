package com.gys.play.http.jj;

import androidx.fragment.app.FragmentActivity;

import com.android.liba.util.UIHelper;
import com.gys.play.MyApplication;
import com.gys.play.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 */
public abstract class SimpleSubscriber<T> implements Observer<T> {

    private final boolean isShouldShowDialog;
    private FragmentActivity mActivity;
    protected String TAG = "SimpleSubscriber";

    public SimpleSubscriber() {
        this.isShouldShowDialog = false;
    }

    public SimpleSubscriber(FragmentActivity mActivity, boolean isShouldShowDialog) {
        this.mActivity = mActivity;
        this.isShouldShowDialog = isShouldShowDialog;
        if (isShouldShowDialog) showDialog();
    }

    @Override
    public void onSubscribe(@NonNull Disposable disposable) {
        UIHelper.showLog(TAG, "onSubscribe");
    }

    private void showDialog() {
        if (mActivity != null && !mActivity.isFinishing()) {
            UIHelper.showLog(TAG, "showDialog");
            //这里就不弹窗了
//            DialogManager.showDialog(mActivity);
        }
    }

    private void hideDialog() {
        if (mActivity != null && !mActivity.isFinishing()) {
//            DialogManager.hideDialog(mActivity);
            UIHelper.showLog(TAG, "hideDialog");
            mActivity = null;
        }
    }

    @Override
    public void onComplete() {
        UIHelper.showLog(TAG, "onComplete");
        if (isShouldShowDialog) hideDialog();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        UIHelper.showLog(TAG, "onError:" + e);
        if (isShouldShowDialog) hideDialog();
        if (mActivity != null && mActivity.isFinishing()) {
            mActivity = null;
            return;
        }
        String error = MyApplication.getInstance().getString(R.string.http_error);
        if (e != null) {
            String message = e.getMessage();
            if (message != null && message.contains("Canceled")) {
                error = MyApplication.getInstance().getString(R.string.http_cancel);
            } else if (e instanceof SocketTimeoutException) {
                error = MyApplication.getInstance().getString(R.string.socket_timeout);
            } else if (e instanceof ConnectException) {
                error = MyApplication.getInstance().getString(R.string.connect_error);
            }
        }
        onFail(error);
        UIHelper.showLog(TAG, "httpResult error:" + error);
    }

    @Override
    public void onNext(@NonNull T t) {
        UIHelper.showLog(TAG, "onNext:" + t);
        if (isShouldShowDialog) hideDialog();
        if (mActivity != null && mActivity.isFinishing()) {
            mActivity = null;
            return;
        }
        onSucceed(t);
    }

    public abstract void onSucceed(@NonNull T t);

    public abstract void onFail(@NonNull String error);
}