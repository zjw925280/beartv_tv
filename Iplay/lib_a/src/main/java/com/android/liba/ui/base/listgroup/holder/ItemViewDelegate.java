package com.android.liba.ui.base.listgroup.holder;



public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
    boolean isFullSpan();
    int getSpanCount();

}
