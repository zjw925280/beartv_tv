package com.gys.play.util.CustomHead;

import static android.view.View.MeasureSpec.EXACTLY;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshComponent;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleComponent;
import com.scwang.smart.refresh.layout.util.SmartUtil;

public class MyClassicsAbstract<T extends MyClassicsAbstract> extends SimpleComponent implements RefreshComponent {

    protected int mPaddingTop = 20;
    protected int mPaddingBottom = 20;
    protected int mMinHeightOfContent = 0;

    protected MyClassicsAbstract(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final View thisView = this;
        if (mMinHeightOfContent == 0) {
            mPaddingTop = thisView.getPaddingTop();
            mPaddingBottom = thisView.getPaddingBottom();
            if (mPaddingTop == 0 || mPaddingBottom == 0) {
                int paddingLeft = thisView.getPaddingLeft();
                int paddingRight = thisView.getPaddingRight();
                mPaddingTop = mPaddingTop == 0 ? SmartUtil.dp2px(20) : mPaddingTop;
                mPaddingBottom = mPaddingBottom == 0 ? SmartUtil.dp2px(20) : mPaddingBottom;
                thisView.setPadding(paddingLeft, mPaddingTop, paddingRight, mPaddingBottom);
            }
            final ViewGroup thisGroup = this;
            thisGroup.setClipToPadding(false);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == EXACTLY) {
            final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            if (parentHeight < mMinHeightOfContent) {
                final int padding = (parentHeight - mMinHeightOfContent) / 2;
                thisView.setPadding(thisView.getPaddingLeft(), padding, thisView.getPaddingRight(), padding);
            } else {
                thisView.setPadding(thisView.getPaddingLeft(), 0, thisView.getPaddingRight(), 0);
            }
        } else {
            thisView.setPadding(thisView.getPaddingLeft(), mPaddingTop, thisView.getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMinHeightOfContent == 0) {
            final ViewGroup thisGroup = this;
            for (int i = 0; i < thisGroup.getChildCount(); i++) {
                final int height = thisGroup.getChildAt(i).getMeasuredHeight();
                if (mMinHeightOfContent < height) {
                    mMinHeightOfContent = height;
                }
            }
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return 200;//延迟500毫秒之后再弹回
    }
}
