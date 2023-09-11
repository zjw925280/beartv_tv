package com.android.liba.ui.base.loading;


import com.android.liba.exception.Error;


public interface LoadingViewListener {

    void showLoadingPage();

    void showLoadingRetry(Error error);

    void showLoadingContent();

    void showLoadingEmpty();

}
