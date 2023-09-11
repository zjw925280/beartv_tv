package com.android.liba.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.android.liba.ui.base.listgroup.RecycleViewConfig;
import com.android.liba.ui.base.listgroup.holder.listProvider;
import com.android.liba.ui.base.loading.LoadingAndRetryManager;

import java.util.List;

public abstract class BaseListFragment<P extends BaseListPresent, D> extends BaseFragment<P> implements listProvider<D> {

    BaseListManager<D> listManager = new BaseListManager<D>(this);

    public BaseListManager getRecycleViewManager() {
        return listManager;
    }

    private View ensureParent(View view) {
        if (view.getParent() == null) {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        rootView = ensureParent(rootView);
        RecycleViewConfig config = new RecycleViewConfig();
        ViewStub viewStub = findView(getContainerId(config));
        return listManager.onCreateView(rootView, viewStub, config);
    }

    public void requestData(Boolean status) {
        getPresenter().requestData(status);
    }

    public List<D> getData() {
        return getPresenter().getListData();
    }

    public void onLoadMoreRequested() {
        getPresenter().requestData(false);
    }

    public void requestRefreshData(boolean showLoadingPage) {
        getPresenter().requestData(true);
    }

    @Override
    public void onReady() {

    }

    public LoadingAndRetryManager getLoadingAndRetryManager() {
        return null;
    }
}
