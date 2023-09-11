package com.gys.play.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.gys.play.R;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {

        super(context, R.style.BaseDialog);
        init();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        init();
    }

    private void init() {

        normalize();
    }


    private void normalize() {
        fixBackground();
        fixSize();
    }


    private void fixBackground() {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    private void fixSize() {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        DisplayMetrics metrics = new DisplayMetrics();

        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int[] frameSize = onSetupDialogFrameSize(metrics.widthPixels, metrics.heightPixels);

        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = frameSize[0];
        params.height = frameSize[1];
        window.setAttributes(params);
    }


    protected abstract int[] onSetupDialogFrameSize(int screenWidth, int screenHeight);
}
