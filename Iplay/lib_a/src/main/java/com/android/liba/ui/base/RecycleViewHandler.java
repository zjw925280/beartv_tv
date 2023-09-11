package com.android.liba.ui.base;



import com.android.liba.exception.Error;
import com.android.liba.ui.base.loading.LoadingViewListener;

import java.util.List;


public interface RecycleViewHandler<D> extends LoadingViewListener {

    void closeLoadingDialog();

    void initViewAfterGetData();

    void showLoadingContent();

    void showLoadMoreState(Error error, int pageId);

    void showListItems(boolean refresh, List<D> dataList);

    void notifyDataSetChanged();

    void notifyItemRangeChanged(int pos, int count);

    void notifyItemRangeInserted(int posIncludeHeaderSize, int count, int lastListCount);

    void notifyItemRangeRemoved(boolean showEmptyView, int posIncludeHeaderSize, int count, int lastListCount);

    void notifyItemMoved(int fromPos, int toPos);

    int getFooterViewSize();

    int getHeaderViewSize();

}
