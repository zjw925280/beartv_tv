package com.android.liba.ui.base.listgroup.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.ui.base.BaseListManager;
import com.android.liba.ui.base.listgroup.RecycleViewConfig;
import com.android.liba.ui.base.loading.LoadingAndRetryManager;

import java.util.List;

public interface listProvider<D> {
    int getContainerId(RecycleViewConfig config);

    RecyclerView.Adapter getListAdapter(RecyclerView parentView, List<D> data);

    List<D> getData();

    void requestData(Boolean status);

    void onReady();

    BaseListManager getRecycleViewManager();

    LoadingAndRetryManager getLoadingAndRetryManager();
}
