package com.android.liba.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.liba.ui.base.StatusBarCompat;

public class StatusBar extends AppCompatTextView {
    public StatusBar(@NonNull Context context) {
        super(context);
    }

    public StatusBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setMeasuredDimension(sizeWidth, StatusBarCompat.getStatusBarHeight(getContext()));
        } else {
            setMeasuredDimension(sizeWidth, 0);
        }
    }
}
