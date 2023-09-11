package com.android.liba.ui.widget.conner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.liba.R;

import java.util.ArrayList;
import java.util.List;

public class BackgroundConstraintLayout extends ConstraintLayout {

    private JBackgroundView jBackgroundView;

    public BackgroundConstraintLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BackgroundConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BackgroundConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BackgroundConstraintLayout);

            jBackgroundView = new JBackgroundView();
            //获取自定义属性和默认值
            jBackgroundView.bgColor = a.getColor(R.styleable.BackgroundConstraintLayout_bgColor, jBackgroundView.bgColor);
            jBackgroundView.bgColor_p = a.getColor(R.styleable.BackgroundConstraintLayout_bgColor_p, jBackgroundView.bgColor_p);
            jBackgroundView.lineColor = a.getColor(R.styleable.BackgroundConstraintLayout_lineColor, jBackgroundView.lineColor);
            jBackgroundView.lineColor_p = a.getColor(R.styleable.BackgroundConstraintLayout_lineColor_p, jBackgroundView.lineColor_p);
            jBackgroundView.lineWidth = a.getDimension(R.styleable.BackgroundConstraintLayout_lineWidth, jBackgroundView.lineWidth);
            jBackgroundView.connerHalfWidth = a.getBoolean(R.styleable.BackgroundConstraintLayout_connerHalfWidth, jBackgroundView.connerHalfWidth);
            jBackgroundView.connerHalfHeight = a.getBoolean(R.styleable.BackgroundConstraintLayout_connerHalfHeight, jBackgroundView.connerHalfHeight);
            jBackgroundView.conner = a.getDimension(R.styleable.BackgroundConstraintLayout_conner, jBackgroundView.conner);
            jBackgroundView.connerLeftTop = a.getDimension(R.styleable.BackgroundConstraintLayout_conner_left_top, jBackgroundView.connerLeftTop);
            jBackgroundView.connerLeftBottom = a.getDimension(R.styleable.BackgroundConstraintLayout_conner_left_bottom, jBackgroundView.connerLeftBottom);
            jBackgroundView.connerRightTop = a.getDimension(R.styleable.BackgroundConstraintLayout_conner_right_top, jBackgroundView.connerRightTop);
            jBackgroundView.connerRightBottom = a.getDimension(R.styleable.BackgroundConstraintLayout_conner_right_bottom, jBackgroundView.connerRightBottom);
            jBackgroundView.orientation = a.getInt(R.styleable.BackgroundConstraintLayout_colors_orientation, jBackgroundView.orientation);
            int unSetValue = -999;
            int colorStart = a.getColor(R.styleable.BackgroundConstraintLayout_colors_start, unSetValue);
            int colorStart_p = a.getColor(R.styleable.BackgroundConstraintLayout_colors_start_p, unSetValue);
            int colorCenter = a.getColor(R.styleable.BackgroundConstraintLayout_colors_center, unSetValue);
            int colorCenter_p = a.getColor(R.styleable.BackgroundConstraintLayout_colors_center_p, unSetValue);
            int colorEnd = a.getColor(R.styleable.BackgroundConstraintLayout_colors_end, unSetValue);
            int colorEnd_p = a.getColor(R.styleable.BackgroundConstraintLayout_colors_end_p, unSetValue);
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
