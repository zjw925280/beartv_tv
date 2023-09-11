package com.android.liba.util.list;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.liba.util.UIHelper;


public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {

    public MyStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            UIHelper.showLog(getClass().getSimpleName(), "onLayoutChildren Exception: " + e);
            e.printStackTrace();
        }
    }
}
