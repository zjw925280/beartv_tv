package com.gys.play.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.liba.util.UIHelper;
import com.android.liba.util.list.ListUtil;


public class ViewPager2ChildTouchLayout extends FrameLayout {

    private ViewPager2 viewPager2;
    private RecyclerView recyclerView;
    private int mTouchSlop;

    public ViewPager2ChildTouchLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ViewPager2ChildTouchLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPager2ChildTouchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ViewPager2ChildTouchLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        findViewPager2(this);
        findRecyclerView(this);
    }

    private void findRecyclerView(View view) {
        if (view instanceof ViewGroup) {
            if (view instanceof RecyclerView) {
                recyclerView = (RecyclerView) view;
                return;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                findRecyclerView(viewGroup.getChildAt(i));
            }
        }
    }

    private void findViewPager2(View view) {
        ViewParent parent = view.getParent();
        if (parent == null) return;
        if (parent instanceof ViewPager2) {
            viewPager2 = (ViewPager2) parent;
        } else {
            if (parent instanceof View) {
                findViewPager2((View) parent);
            }
        }
    }

    private float mStartX, mStartY;
    private boolean isRecyclerViewHOrientation;
    private boolean isViewPager2HOrientation;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (viewPager2 == null || recyclerView == null) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                isRecyclerViewHOrientation = ListUtil.isRecyclerViewHOrientation(recyclerView);
                isViewPager2HOrientation = viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL;
                UIHelper.showLog("ViewPager2ChildTouchLayout","isRecyclerViewHOrientation = "+isRecyclerViewHOrientation);
                UIHelper.showLog("ViewPager2ChildTouchLayout","isViewPager2HOrientation = "+isViewPager2HOrientation);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = Math.abs(endX - mStartX);
                float distanceY = Math.abs(endY - mStartY);
                if (isViewPager2HOrientation && isRecyclerViewHOrientation) {
                    boolean mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY;
                    getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
                    UIHelper.showLog("ViewPager2ChildTouchLayout","can H scroll");
                } else if (!isViewPager2HOrientation && !isRecyclerViewHOrientation){
                    boolean mIsViewPager2Drag = distanceY > mTouchSlop && distanceY > distanceX;
                    getParent().requestDisallowInterceptTouchEvent(mIsViewPager2Drag);
                    UIHelper.showLog("ViewPager2ChildTouchLayout","can V scroll");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
