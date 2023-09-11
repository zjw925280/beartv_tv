package com.android.liba.ui.widget.conner;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.android.liba.R;

public class TipView extends BackgroundTextView {

    private final int unSetValue = -999;
    private int textStyle_n = unSetValue;
    private int textStyle_p = unSetValue;
    private float textSize_n = unSetValue;
    private float textSize_p = unSetValue;
    private int textColor_n = unSetValue;
    private int textColor_p = unSetValue;

    public TipView(Context context) {
        super(context);
        init(context, null);
    }

    public TipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TipView);

            textStyle_n = a.getInt(R.styleable.TipView_textStyle_n, textStyle_n);
            textStyle_p = a.getInt(R.styleable.TipView_textStyle_p, textStyle_p);

            textSize_n = a.getDimension(R.styleable.TipView_textSize_n, textSize_n);
            textSize_p = a.getDimension(R.styleable.TipView_textSize_p, textSize_p);

            textColor_n = a.getColor(R.styleable.TipView_textColor_n, textColor_n);
            textColor_p = a.getColor(R.styleable.TipView_textColor_p, textColor_p);

            a.recycle();
        }
        if (textStyle_n != unSetValue ||
                textStyle_p != unSetValue ||
                textSize_n != unSetValue ||
                textSize_p != unSetValue ||
                textColor_n != unSetValue ||
                textColor_p != unSetValue) {
            JBackgroundView backgroundView = getBackgroundView();
            if (backgroundView != null) {
                backgroundView.setOnSelectedChangeListener(isSelected -> {
                    if (isSelected) {
                        onStatusSelected();
                    } else {
                        onStatusNormal();
                    }
                });
            }
        }
    }

    private void onStatusSelected() {
        if (textSize_p != unSetValue) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize_p);
        }
        if (textColor_p != unSetValue) {
            setTextColor(textColor_p);
        }
        if (textStyle_p != unSetValue) {
            TextPaint paint = getPaint();
            if (textStyle_p == 0) {
                paint.setFakeBoldText(false);
                paint.setTextSkewX(0);
            } else if (textStyle_p == 1)
                paint.setFakeBoldText(true);
            else if (textStyle_p == 2)
                paint.setTextSkewX(-0.5f);
        }
    }

    private void onStatusNormal() {
        if (textSize_n != unSetValue) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize_n);
        }
        if (textColor_n != unSetValue) {
            setTextColor(textColor_n);
        }
        if (textStyle_n != unSetValue) {
            TextPaint paint = getPaint();
            if (textStyle_n == 0) {
                paint.setFakeBoldText(false);
                paint.setTextSkewX(0);
            } else if (textStyle_n == 1)
                paint.setFakeBoldText(true);
            else if (textStyle_n == 2)
                paint.setTextSkewX(-0.5f);
        }
    }

}
