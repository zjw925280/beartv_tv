package com.android.liba.ui.base;


import android.os.Bundle;
import android.view.ViewStub;


import com.android.liba.R;
import com.android.liba.ui.base.listgroup.RecycleViewConfig;
import com.android.liba.ui.base.listgroup.holder.listProvider;
import com.android.liba.ui.base.loading.LoadingAndRetryManager;

import java.util.List;

public abstract class BaseListActivity< P extends BaseListPresent,D> extends BaseActivity<P> implements  listProvider<D> {

    BaseListManager<D> listManager=new BaseListManager<D>(this) ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecycleViewConfig config = new RecycleViewConfig();
        ViewStub viewStub = findViewById(getContainerId(config));
        viewStub.setLayoutResource(config.isRefreshEnable() ? R.layout.layout_list_refresh_loadmore : R.layout.layout_list);
        listManager.onCreateView(null,viewStub,config)  ;
    }
   public  BaseListManager getRecycleViewManager()
   {
       return listManager;
   }


    public void requestData(Boolean status)
    {
        getPresenter().requestData(status);
    }
    public List<D> getData(){
        return  getPresenter().getListData();
    }



    public void onLoadMoreRequested() {
        getPresenter().requestData(false);
    }

    public void requestRefreshData(boolean showLoadingPage) {
        getPresenter().requestData(true);
    }

    public void onReady() {

    }

    public LoadingAndRetryManager getLoadingAndRetryManager() {
        return null;
    }
}
