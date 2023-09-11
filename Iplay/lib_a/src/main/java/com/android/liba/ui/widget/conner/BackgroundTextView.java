package com.android.liba.ui.widget.conner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import com.android.liba.R;

import java.util.ArrayList;
import java.util.List;

public class BackgroundTextView extends AppCompatTextView {

    private JBackgroundView jBackgroundView;

    public BackgroundTextView(Context context) {
        super(context);
        init(context, null);
    }

    public BackgroundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BackgroundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BackgroundTextView);

            jBackgroundView = new JBackgroundView();
            //获取自定义属性和默认值
            jBackgroundView.bgColor = a.getColor(R.styleable.BackgroundTextView_bgColor, jBackgroundView.bgColor);
            jBackgroundView.bgColor_p = a.getColor(R.styleable.BackgroundTextView_bgColor_p, jBackgroundView.bgColor_p);
            jBackgroundView.lineColor = a.getColor(R.styleable.BackgroundTextView_lineColor, jBackgroundView.lineColor);
            jBackgroundView.lineColor_p = a.getColor(R.styleable.BackgroundTextView_lineColor_p, jBackgroundView.lineColor_p);
            jBackgroundView.lineWidth = a.getDimension(R.styleable.BackgroundTextView_lineWidth, jBackgroundView.lineWidth);
            jBackgroundView.connerHalfWidth = a.getBoolean(R.styleable.BackgroundTextView_connerHalfWidth, jBackgroundView.connerHalfWidth);
            jBackgroundView.connerHalfHeight = a.getBoolean(R.styleable.BackgroundTextView_connerHalfHeight, jBackgroundView.connerHalfHeight);
            jBackgroundView.conner = a.getDimension(R.styleable.BackgroundTextView_conner, jBackgroundView.conner);
            jBackgroundView.connerLeftTop = a.getDimension(R.styleable.BackgroundTextView_conner_left_top, jBackgroundView.connerLeftTop);
            jBackgroundView.connerLeftBottom = a.getDimension(R.styleable.BackgroundTextView_conner_left_bottom, jBackgroundView.connerLeftBottom);
            jBackgroundView.connerRightTop = a.getDimension(R.styleable.BackgroundTextView_conner_right_top, jBackgroundView.connerRightTop);
            jBackgroundView.connerRightBottom = a.getDimension(R.styleable.BackgroundTextView_conner_right_bottom, jBackgroundView.connerRightBottom);
            jBackgroundView.orientation = a.getInt(R.styleable.BackgroundTextView_colors_orientation, jBackgroundView.orientation);
            int unSetValue = -999;
            int colorStart = a.getColor(R.styleable.BackgroundTextView_colors_start, unSetValue);
            int colorStart_p = a.getColor(R.styleable.BackgroundTextView_colors_start_p, unSetValue);
            int colorCenter = a.getColor(R.styleable.BackgroundTextView_colors_center, unSetValue);
            int colorCenter_p = a.getColor(R.styleable.BackgroundTextView_colors_center_p, unSetValue);
            int colorEnd = a.getColor(R.styleable.BackgroundTextView_colors_end, unSetValue);
            int colorEnd_p = a.getColor(R.styleable.BackgroundTextView_colors_end_p, unSetValue);
            List<Integer> tempColors = new ArrayList<>();
            if (colorStart != unSetValue) {
                tempColors.add(colorStart);
            }
            if (colorCenter != unSetValue) {
                tempColors.add(colorCenter);
            }
            if (colorEnd != unSetValue) {
                tempColors.add(colorEnd);
            }
            if (tempColors.size() > 0) {
                jBackgroundView.colors = new int[tempColors.size()];
                for (int i = 0; i < tempColors.size(); i++) {
                    int color = tempColors.get(i);
                    jBackgroundView.colors[i] = color;
                }
            }
            tempColors.clear();
            if (colorStart_p != unSetValue) {
                tempColors.add(colorStart_p);
            }
            if (colorCenter_p != unSetValue) {
                tempColors.add(colorCenter_p);
            }
            if (colorEnd_p != unSetValue) {
                tempColors.add(colorEnd_p);
            }
            if (tempColors.size() > 0) {
                jBackgroundView.colors_p = new int[tempColors.size()];
                for (int i = 0; i < tempColors.size(); i++) {
                    int color = tempColors.get(i);
                    jBackgroundView.colors_p[i] = color;
                }
            }

            a.recycle();
            jBackgroundView.checkSetBg(this);

            int gravity = getGravity();
            int defaultGravity = Gravity.TOP | Gravity.START;
            if (gravity == defaultGravity) {
                setGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (jBackgroundView != null) {
            jBackgroundView.onSizeChange(this, w, h);
        }
    }

    public JBackgroundView getBackgroundView() {
        return jBackgroundView;
    }

}
