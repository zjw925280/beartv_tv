package com.android.liba.util.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MyHRecyclerView extends RecyclerView {

    private ItemDecoration mItemDecoration;

    public MyHRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyHRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ListUtil.initRecyclerViewH(getContext(), this);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        if (mItemDecoration != null) {
            removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = decor;
        super.addItemDecoration(decor);
    }
}
