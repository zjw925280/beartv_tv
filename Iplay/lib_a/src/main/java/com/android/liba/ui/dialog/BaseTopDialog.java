package com.android.liba.ui.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.android.liba.R;


public abstract class BaseTopDialog extends AlertDialog {

    protected Context mContext;

    protected BaseTopDialog(Context context) {
        this(context, R.style.TopDialogStyle);
    }

    protected BaseTopDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义Dialog布局和参数
        setContentView(getContentView());
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
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
