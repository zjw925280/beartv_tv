package com.android.liba.ui.widget.select;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.android.liba.R;


public class SelectorLayout extends FrameLayout {

    public SelectorLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public SelectorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private class MyStateListDrawable extends StateListDrawable {
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean stateChange = super.onStateChange(stateSet);
            if (stateChange) {
//                boolean isPress = false;
//                for (int state : stateSet) {
//                    if (state == android.R.attr.state_pressed) {
//                        isPress = true;
//                        break;
//                    }
//                }
//                int childCount = getChildCount();
//                if (childCount == 2) {
//                    getChildAt(0).setVisibility(isPress ? VISIBLE : GONE);
//                    getChildAt(1).setVisibility(isPress ? GONE : VISIBLE);
//                }
                checkChild();
            }
            return stateChange;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkChild();
    }

    private void checkChild() {
        int childCount = getChildCount();
        if (childCount == 2) {
            //之前是反着的，现在纠正免得别扭
            getChildAt(0).setVisibility(isSelected() ? GONE : VISIBLE);
            getChildAt(1).setVisibility(isSelected() ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkChild();
    }

    private void init() {
        Drawable transDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.trans, null);
//        Drawable transDrawable = getResources().getDrawable(R.drawable.trans);
        setBackGroundSelector(transDrawable, transDrawable);
    }

    public void setBackGroundSelector(Drawable normalDrawer, Drawable pressDrawer) {
        if (normalDrawer == null && pressDrawer == null) {
            return;
        }
        StateListDrawable background_drawable = new MyStateListDrawable();
        background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                normalDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_focused,},
                pressDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_selected},
                pressDrawer);
        background_drawable.addState(new int[]{android.R.attr.state_pressed},
                pressDrawer);
        setBackground(background_drawable);
    }
}
