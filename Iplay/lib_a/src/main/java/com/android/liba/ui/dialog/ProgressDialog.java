package com.android.liba.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.android.liba.R;
import com.android.liba.context.AppContext;


public class ProgressDialog extends Dialog {

    boolean cancelAble;
    private TextView tvContent;
    private BackPressListener mBackPressListener;

    protected ProgressDialog(Context context) {
        super(context, R.style.ProgressDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        setCancelable(cancelAble);
        setCanceledOnTouchOutside(false);
        tvContent = findViewById(R.id.tvContent);
    }

    public void setLoadingText(String loadingText) {
        if (tvContent != null)
            tvContent.setText(loadingText == null ? AppContext.getInstance().getString(R.string.loading_dialog) : loadingText);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressListener != null) {
            mBackPressListener.onBackPress();
        } else {
            super.onBackPressed();
        }
    }

    public static ProgressDialog show(Context context) {
        return show(context, true);
    }

    public static ProgressDialog show(Context context, boolean cancelAble) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.cancelAble = cancelAble;
        dialog.show();
        return dialog;
    }

    public void setBackPressListener(BackPressListener backPressListener) {
        mBackPressListener = backPressListener;
    }

    public interface BackPressListener {
        void onBackPress();
    }

}
