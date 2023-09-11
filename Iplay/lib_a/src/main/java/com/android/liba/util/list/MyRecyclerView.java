package com.android.liba.util.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.jk.OnRecyclerViewTouchScrollListener;


public class MyRecyclerView extends RecyclerView {
    private ItemDecoration mItemDecoration;
    private OnRecyclerViewTouchScrollListener onTouchingListener;
    private int scrollX, scrollY;
    private boolean isTouching;
//    private boolean isTouchIntercept;

    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollX += dx;
                scrollY += dy;
                if (onTouchingListener != null)
                    onTouchingListener.onListen(isTouching, scrollX, scrollY, dx, dy);
            }
        });
    }

    public int getListScrollY() {
        return scrollY;
    }

    public int getListScrollX() {
        return scrollX;
    }

    public void setOnTouchingListener(OnRecyclerViewTouchScrollListener onTouchingListener) {
        this.onTouchingListener = onTouchingListener;
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        if (mItemDecoration != null) {
            removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = decor;
        super.addItemDecoration(decor);
    }

    @Override
    public void addOnScrollListener(@NonNull OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchingListener != null && !isTouching) {
                    onTouchingListener.onListen(isTouching = true, scrollX, scrollY, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onTouchingListener != null && isTouching) {
                    onTouchingListener.onListen(isTouching = false, scrollX, scrollY, 0, 0);
                }
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onTouchingListener != null && !isTouching) {
                    onTouchingListener.onListen(isTouching = true, scrollX, scrollY, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onTouchingListener != null && isTouching) {
                    onTouchingListener.onListen(isTouching = false, scrollX, scrollY, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    //    public void setTouchIntercept(boolean touchIntercept) {
//        isTouchIntercept = touchIntercept;
//    }
}
