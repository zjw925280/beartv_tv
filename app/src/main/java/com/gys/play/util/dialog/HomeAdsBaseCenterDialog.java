package com.gys.play.util.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

import com.gys.play.R;

public abstract class HomeAdsBaseCenterDialog extends AlertDialog {

    protected Context mContext;

    protected HomeAdsBaseCenterDialog(Context context) {
        this(context, R.style.TopDialogStyle2);
    }

    protected HomeAdsBaseCenterDialog(Context context, int theme) {
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
        getWindow().setWindowAnimations(R.style.load_huodong_dialog);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
