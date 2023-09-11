package com.android.liba.ui.base;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.liba.context.AppContext;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.android.liba.R;
import com.android.liba.ui.base.titlebar.TitleBarConfig;
import com.android.liba.ui.dialog.ProgressDialog;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


public abstract class BaseFragment<P extends BasePresent> extends AbstractFragment<P> implements ViewHandler {

    protected View contentView;

    private TitleBarConfig titleBarConfig;

    private ProgressDialog mProgressDialog;

    public View getStatusBarView() {
        return titleBarConfig.getTitleBar();
    }

    protected ViewDataBinding dataBinding;

    public <T> T getViewDataBinding() {
        return (T) dataBinding;
    }

    ViewModelProvider viewModelProvider = null;

    public RequestManager getGlide() {
        return Glide.with(this);
    }

    public <T> void safeAction(T data, Consumer<? super T> onNext) {
        Observable.just(data).compose(bindUntilEvent(FragmentEvent.DESTROY)).subscribe(onNext);
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass) {
        synchronized (this) {
            if (viewModelProvider == null)
                viewModelProvider = new ViewModelProvider(this.getActivity());
        }
        return viewModelProvider.get(modelClass);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = getRootView();
            titleBarConfig = new TitleBarConfig(getActivity(), container, TitleBarConfig.LayoutMode.NULL, R.color.white);
            initTitleBar(titleBarConfig);
            titleBarConfig.initTitleBar();
            if (titleBarConfig.getLayoutMode() == TitleBarConfig.LayoutMode.TITLE_BAR) {
                contentView = StatusBarCompat.addTitleBar(titleBarConfig, contentView);
            }
            initView(savedInstanceState, contentView);
            setListener();
            if (getPresenter() != null && getPresenter().isLoading()) {
                showLoadingDialog();
            }
            loadData(savedInstanceState);
        }
        createView();
        return contentView;
    }

    /**
     * 每次都刷新，fragment切换的时候 避免复用没有更新ui
     * 使用场景 使用replace加载fragment，增加addToBackStack(),原来Fragment不会销毁，但是会销毁视图和重新创建视图（回调onDestroyView和onCreateView)
     */
    protected void createView() {

    }

    public void setViewDataBinding(ViewDataBinding binding) {
        dataBinding = binding;
    }

    protected View getRootView() {
        int resId = getLayoutId();
        View itemView = null;
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), resId, null, false);
        if (dataBinding == null) {
            itemView = LayoutInflater.from(getActivity()).inflate(resId, null, false);
        } else {
            dataBinding.setLifecycleOwner(this);
            itemView = dataBinding.getRoot();
        }
        return itemView;
    }

    public static void showToast(String msg) {
        AppContext.showToast(msg);
    }

    public static void showToast(int id) {
        AppContext.showToast(id);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
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

    public void initTitleBar(TitleBarConfig titleBarConfig) {
    }

    public void showLoadingDialog() {
        showLoadingDialog(null, false);
    }

    public void showLoadingDialog(String text) {
        showLoadingDialog(text, false);
    }

    public ProgressDialog showLoadingDialog(String text, boolean cancelAble) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = !TextUtils.isEmpty(text) ? ProgressDialog.show(getActivity(), cancelAble) : ProgressDialog.show(getActivity());
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

    public void closeLoadingDialog() {
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

    public <T extends View> T findView(int id) {
        return findView(contentView, id);
    }

    public <T extends View> T findView(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    public void click(int viewId, View.OnClickListener listener) {
        click(findView(viewId), listener);
    }

    public void click(View view, View.OnClickListener listener) {
        view.setOnClickListener(listener);
    }

    public void longClick(View view, View.OnLongClickListener listener) {
        view.setOnLongClickListener(listener);
    }

    public void doubleClick(View view, View.OnClickListener onClickListener) {
        final long[] mHits = new long[2];
        view.setOnClickListener(v -> {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                // 双击事件
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    protected void loadLazyData() {
    }

    public void navigate(String Url) {
        ARouter.getInstance().build(Url).navigation();
    }

    public void navigate(String Url, Bundle data) {
        ARouter.getInstance().build(Url).with(data).navigation();
    }

    @Override
    public void initView(Bundle savedInstanceState, View fragmentView) {
    }

    @Override
    public void loadData(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }
}
