package com.android.liba.ui.dialog;

import android.content.Context;
import android.view.View;

import com.android.liba.R;

public class CancelAccountDialog extends BaseCenterDialog {

    private DialogButtonListener listener;

    protected CancelAccountDialog(Context context, DialogButtonListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public View getContentView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_account, null);
        view.findViewById(R.id.tv_sure_cancel).setOnClickListener(v -> {
            if (listener != null) {
                listener.onSure();
            }
            this.dismiss();
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            this.dismiss();
        });
        return view;
    }

    public static CancelAccountDialog show(Context context, DialogButtonListener listener) {
        CancelAccountDialog dialog = new CancelAccountDialog(context, listener);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
