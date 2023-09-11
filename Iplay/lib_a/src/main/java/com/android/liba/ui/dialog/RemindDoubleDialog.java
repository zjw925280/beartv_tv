package com.android.liba.ui.dialog;


import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.liba.R;


public class RemindDoubleDialog extends BaseDialog {

    private final RemindDialogBuild remindDialogBuild;
    private int background = 0;

    protected RemindDoubleDialog(RemindDialogBuild remindDialogBuild) {
        super(remindDialogBuild.context);
        this.remindDialogBuild = remindDialogBuild;
    }

    protected RemindDoubleDialog(RemindDialogBuild remindDialogBuild, int backround) {
        super(remindDialogBuild.context);
        this.remindDialogBuild = remindDialogBuild;
        this.background = backround;
    }

    @Override
    public View initContentView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.dialog_remind_double, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(remindDialogBuild.title);
        tvTitle.setVisibility(TextUtils.isEmpty(remindDialogBuild.title) ? View.GONE : View.VISIBLE);

        TextView centerView = view.findViewById(R.id.tvContent);
        if (!TextUtils.isEmpty(remindDialogBuild.htmlContent)) {
            centerView.setText(Html.fromHtml(remindDialogBuild.htmlContent));
        } else {
            centerView.setText(remindDialogBuild.content);
        }

        TextView tvSure = view.findViewById(R.id.tvSure);
        tvSure.setText(remindDialogBuild.confirmText);
        tvSure.setSelected(remindDialogBuild.confirmHighLight);
        if (background != 0)
            tvSure.setBackgroundResource(background);
        tvSure.setOnClickListener(v -> {
            if (remindDialogBuild.onConfirmListener != null) {
                remindDialogBuild.onConfirmListener.onClick(tvSure);
            }
            this.dismiss();
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvCancel.setText(remindDialogBuild.cancelText);
        tvCancel.setSelected(remindDialogBuild.cancelHighLight);

        tvCancel.setOnClickListener(v -> {
            if (remindDialogBuild.onCancelListener != null) {
                remindDialogBuild.onCancelListener.onClick(v);
            }
            this.dismiss();
        });
        setOnDismissListener(remindDialogBuild.onDismissListener);
    }

}
