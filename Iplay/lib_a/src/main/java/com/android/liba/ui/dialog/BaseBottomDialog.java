package com.android.liba.ui.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.android.liba.R;

public abstract class BaseBottomDialog extends AlertDialog {

    protected Context mContext;

    protected BaseBottomDialog(Context context) {
        this(context, R.style.BottomDialogStyle);
    }

    protected BaseBottomDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义Dialog布局和参数
        setContentView(getContentView());
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.shareDialogStyle);
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public abstract View getContentView();

}
