package com.android.liba.ui.base.listgroup;


import android.content.Context;
import android.view.LayoutInflater;

import com.android.liba.ui.base.listgroup.holder.ItemViewDelegate;
import com.android.liba.ui.base.listgroup.holder.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTypeAdapter<T> extends MixTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public SingleTypeAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                SingleTypeAdapter.this.convert(holder, t, position);
            }

            @Override
            public boolean isFullSpan() {
                return false;
            }

            @Override
            public int getSpanCount() {
                return 0;
            }
        });
    }
    public SingleTypeAdapter(final Context context, final int layoutId) {
        this(context, layoutId,new ArrayList<T>());

    }
    protected abstract void convert(ViewHolder holder, T t, int position);
}
