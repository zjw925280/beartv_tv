package com.android.liba.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * RecyclerView 显示所有item，解决布局嵌套时只显示一个item
 * 嵌套 RecyclerView 使用，避免嵌套时只出现一个item问题
 */
public class MaxRecyclerView extends RecyclerView {
    public MaxRecyclerView(@NonNull @NotNull Context context) {
        super(context);
        setNestedScrollingEnabled(false); //禁止滑动 减少卡顿
    }

    public MaxRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(false);//禁止滑动 减少卡顿
    }

    public MaxRecyclerView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNestedScrollingEnabled(false);//禁止滑动 减少卡顿
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
