package com.android.liba.ui.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;

import com.android.liba.R;

public abstract class BaseDialog extends AlertDialog implements View.OnClickListener {

    private View top;
    private View bottom;

    private boolean closeOutside;
    protected Context mContext;

    protected BaseDialog(Context context) {
        this(context, true);
    }

    protected BaseDialog(Context context, boolean closeOutside) {
        super(context, R.style.fullscreen_dialog);
        this.closeOutside = closeOutside;
        this.mContext = context;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        ViewGroup.LayoutParams clp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View content = initContentView(layoutInflater);

        View layout = layoutInflater.inflate(getParentLayoutId(), null);

        top = layout.findViewById(R.id.dialog_top);
        bottom = layout.findViewById(R.id.dialog_bottom);
        top.setOnClickListener(this);
        bottom.setOnClickListener(this);

        LinearLayout contentLayout = layout.findViewById(R.id.dialog_content);
        contentLayout.addView(content, clp);

        setContentView(layout, clp);
        setCancelable(true);
        setCanceledOnTouchOutside(true);


        WindowManager.LayoutParams lParams = getWindow().getAttributes();
        lParams.gravity = Gravity.CENTER;
        lParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lParams.alpha = 1.0f;
        lParams.dimAmount = 0.0f;
        getWindow().setAttributes(lParams);
    }

    @Override
    public void onClick(View v) {
        if (closeOutside) {
            this.dismiss();
        }
    }

    public @LayoutRes
    int getParentLayoutId() {
        return R.layout.dialog_base_default;
    }

    public abstract View initContentView(LayoutInflater inflater);

}
