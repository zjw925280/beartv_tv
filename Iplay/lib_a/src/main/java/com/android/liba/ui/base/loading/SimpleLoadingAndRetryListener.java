package com.android.liba.ui.base.loading;


import android.view.View;
import android.widget.TextView;

import com.android.liba.R;
import com.android.liba.exception.Error;
import com.android.liba.exception.NetworkError;

public abstract class SimpleLoadingAndRetryListener extends OnLoadingAndRetryListener {


    protected TextView tvErrorText, tvErrorContent;
    protected TextView tvReloadButton;

    private LoadState loadState;

    public SimpleLoadingAndRetryListener() {
        this(LoadState.DEFAULT);
    }

    public SimpleLoadingAndRetryListener(LoadState loadState) {
        this.loadState = loadState;
    }

    @Override
    public int generateLoadingLayoutId() {
        return R.layout.loading_loading;
    }

    @Override
    public int generateRetryLayoutId() {
        return R.layout.loading_retry_default;
    }

    @Override
    public int generateEmptyLayoutId() {
        return R.layout.loading_empty_default;
    }

    @Override
    public void setEmptyEvent(View emptyView) {
        TextView tvErrorIcon = emptyView.findViewById(R.id.tvErrorIcon);
        tvErrorIcon.setText(LoadState.getEmptyString(loadState));
        int drawableRes;
        if ((drawableRes = LoadState.getEmptyDrawable(loadState)) != 0) {
            tvErrorIcon.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0);
        }
    }

    @Override
    public void setLoadingEvent(View loadingViewLayout) {
    }

    @Override
    public void setRetryEvent(View retryView) {
        tvReloadButton = retryView.findViewById(R.id.tvReloadButton);
        tvErrorText = retryView.findViewById(R.id.tvErrorText);
        tvErrorContent = retryView.findViewById(R.id.tvErrorContent);
        tvReloadButton.setOnClickListener(v -> onRetryClickListener());
    }

    @Override
    public void setRetryViewData(Error error) {
        if (! (error instanceof NetworkError)) {
            tvErrorText.setText("Visible in the test phase,errorCode:" + error.code + ", " + error.getMessage());
         //   tvErrorText.setVisibility(View.VISIBLE);
        } else {
            tvErrorText.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadingListener(boolean show) {
    }

    public abstract void onRetryClickListener();
}
