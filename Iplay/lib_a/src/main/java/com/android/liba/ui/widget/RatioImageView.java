package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.android.liba.R;


public class RatioImageView extends AppCompatImageView {

    private float mRatioWidth;
    private float mRatioHeight;

    private int type;

    public RatioImageView(Context context) {
        this(context,null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mRatioWidth = Float.valueOf(t.getInteger(R.styleable.RatioLayout_ratioWidthLayout,1));
        mRatioHeight = Float.valueOf(t.getInteger(R.styleable.RatioLayout_ratioHeightLayout,1));
        type = t.getInt(R.styleable.RatioLayout_ratioType,0);
        t.recycle();
    }

    public void resetSize(int ratioWidth,int ratioHeight) {
        mRatioWidth = ratioWidth;
        mRatioHeight = ratioHeight;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        if (type == 0) {
            float mRatio = mRatioHeight / mRatioWidth;
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = (int) Math.ceil(widthSize * mRatio);
            int newHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec,newHeightSpec);
        } else {
            float mRatio = mRatioWidth / mRatioHeight;
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthSize = (int) Math.ceil(heightSize * mRatio);
            int newWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            super.onMeasure(newWidthSpec,heightMeasureSpec);
        }
    }
}
