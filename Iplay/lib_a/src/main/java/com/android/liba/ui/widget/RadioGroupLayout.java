package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;


import com.android.liba.R;
import com.android.liba.jk.OnListener;

import java.util.ArrayList;
import java.util.List;


public class RadioGroupLayout extends LinearLayout implements View.OnClickListener {
    private final List<View> childViewList = new ArrayList<>();
    private OnListener<Integer> onSelectListener;
    private OnListener<Integer> onSelectIgnoreRepeatListener;
    private int mCurrentPosition;
    private boolean isClickCheckChangeEnable = true;//是否允许点击切换选择

    public RadioGroupLayout(Context context) {
        super(context);
    }

    public RadioGroupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioGroupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setClickCheckChangeEnable(boolean clickCheckChangeEnable) {
        isClickCheckChangeEnable = clickCheckChangeEnable;
    }

    /**
     * 点击其他选中时才会回调
     */
    public void setOnSelectListener(OnListener<Integer> onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    /**
     * 忽略重复点击同一个的点击回调
     */
    public void setOnSelectIgnoreRepeatListener(OnListener<Integer> onSelectIgnoreRepeatListener) {
        this.onSelectIgnoreRepeatListener = onSelectIgnoreRepeatListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh();
    }

    public synchronized void refresh() {
        childViewList.clear();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            if (layoutParams instanceof RadioGroupLayoutParams) {
                RadioGroupLayoutParams lp = (RadioGroupLayoutParams) layoutParams;
                if (!lp.ignoreSelect) {
                    childViewList.add(childAt);
                }
            }
        }

        for (int i = 0; i < childViewList.size(); i++) {
            View child = childViewList.get(i);
            child.setSelected(i == mCurrentPosition);
            child.setTag(i);
            child.setOnClickListener(this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        refresh();
    }

    public void clearCheck() {
        for (int i = 0; i < childViewList.size(); i++) {
            childViewList.get(i).setSelected(false);
        }
        mCurrentPosition = -1;
    }

    public int getCheckPosition() {
        return mCurrentPosition;
    }

    public void check(int position) {
        check(position, false);
    }

    public void check(int position, boolean isForce) {
        check(position, isForce, true);
    }

    /**
     * @param isForce  默认false
     * @param isNotice 默认true
     */
    public void check(int position, boolean isForce, boolean isNotice) {
        if (childViewList == null || childViewList.size() == 0) {
            return;
        }
        boolean isShouldQuFan = false;
        if (position < 0) {
            isShouldQuFan = true;
            position = Math.abs(position);
        }
        if (position > childViewList.size() - 1) {
            position = position % childViewList.size();
        }
        if (isShouldQuFan) {
            position = childViewList.size() - position;
        }
        if (mCurrentPosition != position || isForce) {
            mCurrentPosition = position;
            for (int i = 0; i < childViewList.size(); i++) {
                childViewList.get(i).setSelected(i == position);
            }

            if (onSelectListener != null && isNotice) {
                onSelectListener.onListen(position);
            }
        }
        if (onSelectIgnoreRepeatListener != null) {
            onSelectIgnoreRepeatListener.onListen(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (!isClickCheckChangeEnable) {
            return;
        }
        int position = (int) v.getTag();
        check(position);
    }

    public void setSimpleLayout(SimpleLayout simpleLayout) {
        removeAllViews();
        if (simpleLayout == null) return;
        for (int i = 0; i < simpleLayout.getCount(); i++) {
            View inflate = LayoutInflater.from(getContext()).inflate(simpleLayout.getLayoutId(), this, false);
            addView(inflate);
        }
    }

    public interface SimpleLayout {
        int getLayoutId();

        int getCount();
    }

    public void bindViewPager(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int childCount = childViewList == null ? 0 : childViewList.size();
                if (childCount > 0) check(position % childCount, true);
            }
        });
        int childCount = childViewList == null ? 0 : childViewList.size();
        if (childCount > 0) check(0, true);

        setOnSelectListener(new OnListener<Integer>() {
            @Override
            public void onListen(Integer position) {
                viewPager.setCurrentItem(position);
            }
        });
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RadioGroupLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new RadioGroupLayoutParams(lp);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new RadioGroupLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof RadioGroupLayoutParams;
    }

    public static class RadioGroupLayoutParams extends LayoutParams {

        private boolean ignoreSelect;

        public RadioGroupLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RadioGroupLayout_Layout);
            //获取设置在子控件上的位置属性
            ignoreSelect = a.getBoolean(R.styleable.RadioGroupLayout_Layout_layout_ignoreSelect, ignoreSelect);
            a.recycle();
        }

        public RadioGroupLayoutParams(int width, int height) {
            super(width, height);
        }

        public RadioGroupLayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public RadioGroupLayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public RadioGroupLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public RadioGroupLayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
