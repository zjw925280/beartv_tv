package com.android.liba.ui.dialog;

import androidx.recyclerview.widget.RecyclerView;

public interface AgreementPrivacyListener {
    void onSure();

    void onCancel();

    void onContent(RecyclerView rvPermission);

    void onAgreement();

    void onPrivacy();
}
