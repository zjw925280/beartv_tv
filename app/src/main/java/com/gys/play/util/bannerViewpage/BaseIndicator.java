package com.gys.play.util.bannerViewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class BaseIndicator extends View {

    public BaseIndicator(Context context) {
        super(context);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setState(boolean b);
}
