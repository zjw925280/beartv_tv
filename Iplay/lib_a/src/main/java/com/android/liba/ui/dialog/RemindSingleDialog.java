package com.android.liba.ui.dialog;


import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.liba.R;

public class RemindSingleDialog extends BaseDialog {

    private RemindDialogBuild remindDialogBuild;
    private int background=0;
    protected RemindSingleDialog(RemindDialogBuild remindDialogBuild,int background) {
        super(remindDialogBuild.context);
        this.remindDialogBuild = remindDialogBuild;
        this.background=background;
    }

    @Override
    public View initContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.dialog_remind_single, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);

        tvTitle.setText(remindDialogBuild.title);

        TextView centerView = view.findViewById(R.id.tvContent);
        if (!TextUtils.isEmpty(remindDialogBuild.htmlContent)) {
            centerView.setText(Html.fromHtml(remindDialogBuild.htmlContent));
        } else {
            centerView.setText(remindDialogBuild.content);
        }

        TextView tvSure = view.findViewById(R.id.tvSure);
        tvSure.setText(remindDialogBuild.confirmText);
        tvSure.setSelected(remindDialogBuild.confirmHighLight);
        if(background!=0)
            tvSure.setBackgroundResource(background);
        tvSure.setOnClickListener(v -> {
            if (remindDialogBuild.onConfirmListener != null) {
                remindDialogBuild.onConfirmListener.onClick(tvSure);
            }
            this.dismiss();
        });

        setOnDismissListener(remindDialogBuild.onDismissListener);
    }

}
