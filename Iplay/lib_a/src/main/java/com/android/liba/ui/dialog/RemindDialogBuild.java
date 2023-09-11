package com.android.liba.ui.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.StringRes;

import com.android.liba.R;


public class RemindDialogBuild {

    protected Context context;

    protected String title;
    protected String content;
    protected String confirmText;
    protected String cancelText;
    protected boolean cancelHighLight;
    protected boolean confirmHighLight;
    protected View.OnClickListener onCancelListener;
    protected View.OnClickListener onConfirmListener;
    protected DialogInterface.OnDismissListener onDismissListener;
    protected String htmlContent;
    private int background=0;

    private RemindDialogBuild(Context context) {
        this.context = context;
        setTitle(R.string.dialog_title);
        setCancel(R.string.dialog_cancel);
        setConfirm(R.string.dialog_sure);
    }
    private RemindDialogBuild(Context context, int background) {
        this(context);
        this.background=background;
    }
    public static RemindDialogBuild create(Context context) {
        return new RemindDialogBuild(context);
    }
    public static RemindDialogBuild create(Context context, int background) {
        return new RemindDialogBuild(context,background);
    }
    private String getString(@StringRes int stringRes) {
        return context.getString(stringRes);
    }

    public RemindDialogBuild setTitle(@StringRes int titleRes) {
        return setTitle(getString(titleRes));
    }

    public RemindDialogBuild setTitle(String title) {
        this.title = title;
        return this;
    }

    public RemindDialogBuild setContent(@StringRes int contentRes) {
        return setContent(getString(contentRes));
    }

    public RemindDialogBuild setContent(String content) {
        this.content = content;
        return this;
    }

    public RemindDialogBuild setHtmlContent(String content) {
        this.htmlContent = content;
        return this;
    }

    public RemindDialogBuild setCancel(@StringRes int cancelText) {
        return setCancel(getString(cancelText));
    }

    public RemindDialogBuild setCancel(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public RemindDialogBuild setConfirm(@StringRes int confirmTextRes) {
        return setConfirm(getString(confirmTextRes));
    }

    public RemindDialogBuild setConfirm(String confirmText) {
        this.confirmText = confirmText;
        return this;
    }

    public RemindDialogBuild setConfirmHighLight(boolean confirmHighLight) {
        this.confirmHighLight = confirmHighLight;
        return this;
    }

    public RemindDialogBuild setCancelHighLight(boolean cancelHighLight) {
        this.cancelHighLight = cancelHighLight;
        return this;
    }

    public RemindDialogBuild setOnCancelListener(View.OnClickListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    public RemindDialogBuild setOnConfirmListener(View.OnClickListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public RemindDialogBuild setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public RemindDoubleDialog buildDouble() {
        RemindDoubleDialog remindDialog = new RemindDoubleDialog(this,background);
        remindDialog.show();
        return remindDialog;
    }

    public RemindSingleDialog buildSingle() {
        RemindSingleDialog remindDialog = new RemindSingleDialog(this,background);
        remindDialog.show();
        return remindDialog;
    }
}