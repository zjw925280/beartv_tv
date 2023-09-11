package com.android.liba.ui.base;


import android.view.View;
import android.view.ViewStub;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.R;
import com.android.liba.exception.EmptyError;
import com.android.liba.exception.Error;
import com.android.liba.ui.base.listgroup.RecycleViewConfig;
import com.android.liba.ui.base.listgroup.holder.listProvider;
import com.android.liba.ui.base.loading.LoadingAndRetryManager;
import com.android.liba.ui.base.loading.OnLoadingAndRetryListener;
import com.android.liba.ui.base.loading.SimpleLoadingAndRetryListener;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class BaseListManager<D> {
    private LoadingAndRetryManager mLoadingAndRetryManager = null;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout refreshLayout;

    private RecyclerView.Adapter baseAdapter;
    listProvider<D> provider;

    public BaseListManager(listProvider provider) {
        this.provider = provider;
    }

    public LoadingAndRetryManager getLoadingAndRetryManager() {
        return mLoadingAndRetryManager;
    }

    public void setLoadingAndRetryManager(LoadingAndRetryManager mLoadingAndRetryManager) {
        this.mLoadingAndRetryManager = mLoadingAndRetryManager;
    }

    public View onCreateView(View rootView, ViewStub viewStub, RecycleViewConfig config) {
        viewStub.setLayoutResource(config.isRefreshEnable() ? R.layout.layout_list_refresh_loadmore : R.layout.layout_list);
        View refreshLoadMoreListView = viewStub.inflate();
        mLoadingAndRetryManager = provider.getLoadingAndRetryManager();
        if (mLoadingAndRetryManager == null) {
            OnLoadingAndRetryListener onLoadingAndRetryListener = new SimpleLoadingAndRetryListener(config.getLoadState()) {
                @Override
                public void onRetryClickListener() {
                    requestRefreshData(true);
                }
            };
            mLoadingAndRetryManager = new LoadingAndRetryManager(refreshLoadMoreListView, onLoadingAndRetryListener);
        }

        if (config.isRefreshEnable() || config.isLoadMoreEnable()) {
            refreshLayout = refreshLoadMoreListView.findViewById(R.id.refreshLayout);
            config.setSmartRefreshLayout(refreshLayout);
            refreshLayout.setEnabled(true);
            //设置 Header 为 Material样式
            refreshLayout.setEnableAutoLoadMore(true);
            config.notifyInitSmartRefreshLayout(refreshLayout);
            if (config.isRefreshEnable()) {
                refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        requestRefreshData(false);
                    }
                });
            }
            if (config.isLoadMoreEnable()) {
                refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        onLoadMoreRequested();
                    }
                });
            }
            mRecyclerView = refreshLoadMoreListView.findViewById(R.id.rvList);
        } else {
            mRecyclerView = (RecyclerView) refreshLoadMoreListView;
        }
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(viewStub.getContext()));
        }

        baseAdapter = provider.getListAdapter(mRecyclerView, provider.getData());
        mRecyclerView.setAdapter(baseAdapter);

        requestRefreshData(true);


        return rootView;
    }

    public void onLoadMoreRequested() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.finishRefresh();
        }
        provider.requestData(false);
    }

    @CallSuper
    public void showListItems(boolean refresh, List<D> dataList) {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
        }
    }

    public void finishLoadMoreWithNoMoreData() {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    public void showLoadingEmpty() {
        finishRefreshAndLoading();
        if (mLoadingAndRetryManager != null) {
            mLoadingAndRetryManager.showLoadingEmpty();
        }
    }

    private void finishRefreshAndLoading() {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
        }
    }

    public void showLoadingRetry(Error error) {
        finishRefreshAndLoading();
        if (mLoadingAndRetryManager != null) {
            mLoadingAndRetryManager.showLoadingRetry(error);
        }
    }

    public void setRefreshEnable(boolean enable) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(enable);
        }
    }

    public void scrollTop() {
        scrollToPosition(0);
    }

    public void scrollToPosition(int position) {
        if (position < 0) {
            return;
        }
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public void autoRefresh() {
        if (refreshLayout != null && !refreshLayout.isRefreshing()) {
            refreshLayout.autoRefresh();
        }
    }

    public void requestRefreshData(boolean showLoadingPage) {
        if (showLoadingPage) {
            showLoadingPage();
            if (mRecyclerView != null && mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
                mRecyclerView.scrollToPosition(0);
            }
        }
        provider.requestData(true);
    }

    public void notifyItemRangeChanged(int pos, int count) {
        if (count <= 0)
            return;
        showLoadingContent();
        baseAdapter.notifyItemRangeChanged(pos, count);
    }

    public void notifyDataSetChanged() {
        baseAdapter.notifyDataSetChanged();
    }

    public void notifyItemRangeInserted(int posIncludeHeaderSize, int count, int lastListCount) {
        if (count <= 0)
            return;
        if (lastListCount == 0) {
            baseAdapter.notifyDataSetChanged();
        } else {
            baseAdapter.notifyItemRangeInserted(posIncludeHeaderSize, count);
            notifyItemRangeChanged(posIncludeHeaderSize + count, lastListCount - posIncludeHeaderSize);
        }
        showLoadingContent();
    }

    public void notifyItemRangeRemoved(boolean showEmptyView, int posIncludeHeaderSize, int count, int lastListCount) {
        if (count <= 0)
            return;
        baseAdapter.notifyItemRangeRemoved(posIncludeHeaderSize, count);
        if (showEmptyView) {
            showLoadingEmpty();
        } else {
            notifyItemRangeChanged(posIncludeHeaderSize, lastListCount - posIncludeHeaderSize - count);
        }
    }

    public void notifyItemMoved(int fromPos, int toPos) {
        if (fromPos < 0 || toPos < 0)
            return;
        baseAdapter.notifyItemMoved(fromPos, toPos);
        baseAdapter.notifyItemChanged(fromPos);
        baseAdapter.notifyItemChanged(toPos);
    }

    public void showLoadMoreState(Error error, int pageId) {
        if (error instanceof EmptyError) {
            finishLoadMoreWithNoMoreData();
        }
        showLoadingContent();
        finishRefreshAndLoading();
    }

    public void showLoadingContent() {
        if (mLoadingAndRetryManager != null) {
            mLoadingAndRetryManager.showLoadingContent();
        }
    }

    public void showLoadingPage() {
        if (mLoadingAndRetryManager != null) {
            mLoadingAndRetryManager.showLoadingPage();
        }
    }
}
