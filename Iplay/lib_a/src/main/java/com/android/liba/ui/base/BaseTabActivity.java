package com.android.liba.ui.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.liba.R;
import com.android.liba.ui.base.titlebar.TitleBarConfig;
import com.android.liba.ui.dialog.ProgressDialog;
import com.android.liba.ui.widget.tabLayout.CommonTabLayout;
import com.android.liba.ui.widget.tabLayout.SegmentTabLayout;
import com.android.liba.ui.widget.tabLayout.SlidingTabLayout;
import com.android.liba.ui.widget.tabLayout.listener.OnTabSelectListener;
import com.trello.rxlifecycle2.android.ActivityEvent;


import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public abstract class BaseTabActivity<P extends BasePresent> extends AbstractBaseActivity<P> implements LoadingListener, ViewHandler {
    private TitleBarConfig titleBarConfig;

    private ProgressDialog mProgressDialog;

    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        if (!isTranslucent()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ViewGroup rootView = findViewById(android.R.id.content);
        View contentView = inflaterView(getLayoutId());
        titleBarConfig = new TitleBarConfig(this, rootView, TitleBarConfig.LayoutMode.TITLE_BAR, R.color.purple_200);
        initTitleBar(titleBarConfig);
        titleBarConfig.initTitleBar();
        setContentView(StatusBarCompat.handleView(titleBarConfig, contentView));
        initView(savedInstanceState, contentView);
        loadData(savedInstanceState);
        setListener();

        if (getPresenter() != null && getPresenter().isLoading()) {
            showLoading();
        }
    }

    public boolean isTranslucent() {
        return false;
    }

    public <T> void safeAction(T data, Consumer<? super T> onNext) {
        Observable.just(data).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(onNext);

    }

    protected void initTitleBar(TitleBarConfig titleBarConfig) {
    }

    public void click(int viewId, View.OnClickListener listener) {
        click(findViewById(viewId), listener);
    }

    public void click(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    public void longClick(View view, View.OnLongClickListener listener) {
        view.setOnLongClickListener(listener);
    }

    public void onTabSelectListener(View v, OnTabSelectListener listener) {
        if (v instanceof CommonTabLayout) {
            ((CommonTabLayout) v).setOnTabSelectListener(listener);
        }
        if (v instanceof SlidingTabLayout) {
            ((SlidingTabLayout) v).setOnTabSelectListener(listener);
        }
        if (v instanceof SegmentTabLayout) {
            ((SegmentTabLayout) v).setOnTabSelectListener(listener);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog(null, false);
    }

    public void showLoadingDialog(String text) {
        showLoadingDialog(text, false);
    }

    public ProgressDialog showLoadingDialog(String text, boolean cancelAble) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = !TextUtils.isEmpty(text) ? ProgressDialog.show(this, cancelAble) : ProgressDialog.show(this);
            }
            mProgressDialog.setLoadingText(text);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            setLoadingState(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mProgressDialog;
    }

    public void setLoadingDialogText(String loadingDialogText) {
        if (mProgressDialog != null) {
            mProgressDialog.setLoadingText(loadingDialogText);
        }
    }

    @Override
    public void closeLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        setLoadingState(false);
    }

    private void setLoadingState(boolean isLoading) {
        if (getPresenter() != null) {
            getPresenter().setLoading(isLoading);
        }
    }

    public void setTitleBarVisible(int visibility) {
        if (titleBarConfig.getTitleBar() != null) {
            ViewParent parentView = titleBarConfig.getTitleBar().getParent();
            if (parentView != null) {
                ((View) parentView).setVisibility(visibility);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (titleBarConfig != null) {
            titleBarConfig.clear();
            titleBarConfig = null;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;


    }

    protected View inflaterView(int resId) {
        return LayoutInflater.from(this).inflate(resId, null);
    }

    public void navigate(String Url) {
        ARouter.getInstance().build(Url).navigation();
    }

    public void navigate(String Url, Bundle data) {
        ARouter.getInstance().build(Url).with(data).navigation();
    }
}
