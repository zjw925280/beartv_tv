package com.android.liba.util.list;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.util.UIHelper;


public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int left;
    private int top;
    private int right;
    private int bottom;

    public SpacesItemDecoration(int space) {
        left = top = right = bottom = space;
    }

    public SpacesItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.bottom = bottom;
        outRect.top = top;
    }

    public static void setListLeftRightSpace(RecyclerView recyclerView, int leftRightDp) {
        int leftRightPx = UIHelper.dip2px(recyclerView.getContext(), leftRightDp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(leftRightPx, 0, leftRightPx, 0));
    }

    public static void setSpace(RecyclerView recyclerView, int spaceDp) {
        int spacePx = UIHelper.dip2px(recyclerView.getContext(), spaceDp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacePx));
    }

    public static void setGridSpace(RecyclerView recyclerView, int roundDp, int itemDp) {
        int roundPx = UIHelper.dip2px(recyclerView.getContext(), roundDp);
        int itemPx = UIHelper.dip2px(recyclerView.getContext(), itemDp);
        recyclerView.addItemDecoration(new SpacesItemDecoration(itemPx / 2));
        int leftRound = roundPx - itemPx / 2;
        recyclerView.setPadding(leftRound, leftRound, leftRound, leftRound);
    }
}
