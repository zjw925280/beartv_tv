package com.android.liba.ui.base.listgroup;


import com.android.liba.ui.base.loading.LoadState;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

public class RecycleViewConfig {

    private boolean isRefreshEnable = true;
    private boolean isLoadMoreEnable = true;

    public interface OnInitSmartRefreshLayout {
        void initSmartRefreshLayout(SmartRefreshLayout smartRefreshLayout);
    }

    OnInitSmartRefreshLayout initSmartRefreshLayoutNotify;
    private LoadState loadState = LoadState.DEFAULT;
    private SmartRefreshLayout refreshLayout;

    public RecycleViewConfig setRefreshEnable(boolean isRefreshEnable, boolean isLoadMoreEnable) {
        this.isRefreshEnable = isRefreshEnable;
        this.isLoadMoreEnable = isLoadMoreEnable;
        return this;

    }

    public RecycleViewConfig setOnInitSmartRefreshLayout(OnInitSmartRefreshLayout initSmartRefreshLayoutNotify) {
        this.initSmartRefreshLayoutNotify = initSmartRefreshLayoutNotify;
        return this;
    }

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public void setSmartRefreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    public boolean isRefreshEnable() {
        return isRefreshEnable;
    }

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public LoadState getLoadState() {
        return loadState;
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        return refreshLayout;
    }

    public void notifyInitSmartRefreshLayout(SmartRefreshLayout refreshLayout) {
        if (initSmartRefreshLayoutNotify != null)
            initSmartRefreshLayoutNotify.initSmartRefreshLayout(refreshLayout);
        else {
            refreshLayout.setRefreshHeader(new ClassicsHeader(refreshLayout.getContext()).setSpinnerStyle(SpinnerStyle.Translate));
            refreshLayout.setRefreshFooter(new ClassicsFooter(refreshLayout.getContext()).setSpinnerStyle(SpinnerStyle.Translate));
        }
    }
}