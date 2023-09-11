package com.android.liba.ui.base;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.liba.R;
import com.android.liba.context.AppContext;
import com.android.liba.jk.OnListener;
import com.android.liba.ui.base.titlebar.TitleBarConfig;
import com.android.liba.ui.dialog.ProgressDialog;
import com.android.liba.util.UIHelper;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public abstract class BaseActivity<P extends BasePresent> extends AbstractBaseActivity<P> implements LoadingListener, ViewHandler {

    private TitleBarConfig titleBarConfig;

    private ProgressDialog mProgressDialog;
    private View contentView;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private OnListener<Intent> onResultListener;

    public Activity getActivity() {
        return this;
    }

    public ViewDataBinding dataBinding;
    public FragmentActivity mActivity;

    public <T> T getViewDataBinding() {
        return (T) dataBinding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        super.onCreate(savedInstanceState);
        mActivity = this;
        if (!isTranslucent()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ViewGroup rootView = findViewById(android.R.id.content);
        contentView = getRootView();
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

        //registerForActivityResult必须执行于onCreate中
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            //此处是跳转的result回调方法
            if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                if (onResultListener != null) onResultListener.onListen(result.getData());
                onResultListener = null;
            }
        });
    }

    public View getContentView() {
        return contentView;
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
        //   new ViewClickObservableExt(view).throttleFirst(500, TimeUnit.MILLISECONDS).compose(bindToLifecycle()).subscribe(v->{listener.onClick(v);});
    }

    public void longClick(View view, View.OnLongClickListener listener) {
        view.setOnLongClickListener(listener);
        // new ViewLongClickObservableExt(view).compose(bindToLifecycle()).subscribe(v->{listener.onLongClick(v);});
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

    public static void showToast(String msg) {
        AppContext.showToast(msg);
    }

    public static void showToast(int id) {
        AppContext.showToast(id);
    }

    public void setViewDataBinding(ViewDataBinding binding) {
        dataBinding = binding;
    }

    protected View getRootView() {
        int resId = getLayoutId();
        View itemView;
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), resId, null, false);
        if (dataBinding == null) {
            itemView = LayoutInflater.from(getActivity()).inflate(resId, null, false);
        } else {
            dataBinding.setLifecycleOwner(this);
            itemView = dataBinding.getRoot();
        }
        UIHelper.showLog(this, "dataBinding = " + dataBinding);
        return itemView;
    }

    public void navigate(String Url) {
        ARouter.getInstance().build(Url).navigation();
    }

    public void navigate(String Url, Bundle data) {
        ARouter.getInstance().build(Url).with(data).navigation();
    }

    public void navigate(String Url, int requestCode) {
        ARouter.getInstance().build(Url).navigation(this, requestCode);
    }

    /**
     * startActivityForResult的替代方法
     */
    public void startActivityForResult(Class<?> toClass, OnListener<Intent> onListener) {
        if (toClass == null) return;
        startActivityForResult(new Intent(this, toClass), onListener);
    }

    /**
     * startActivityForResult的替代方法
     */
    public void startActivityForResult(Intent intent, OnListener<Intent> onListener) {
        if (intent == null) return;
        this.onResultListener = onListener;
        intentActivityResultLauncher.launch(intent);
    }
}
