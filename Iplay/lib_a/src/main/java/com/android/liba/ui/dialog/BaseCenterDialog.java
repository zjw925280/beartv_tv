package com.android.liba.ui.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.liba.R;

import androidx.appcompat.app.AlertDialog;

public abstract class BaseCenterDialog extends AlertDialog {

    protected Context mContext;

    protected BaseCenterDialog(Context context) {
        this(context, R.style.TopDialogStyle2);
    }

    protected BaseCenterDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义Dialog布局和参数

        View view = getContentView();
        setContentView(view);

        WindowManager.LayoutParams lParams = getWindow().getAttributes();
        lParams.gravity = Gravity.CENTER;
        lParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lParams);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    @Override
    public void dismiss() {
        if (isShowing())
            super.dismiss();
    }

    @Override
    public void show() {
        if (!isShowing())
            super.show();
    }

    public abstract View getContentView();
}
