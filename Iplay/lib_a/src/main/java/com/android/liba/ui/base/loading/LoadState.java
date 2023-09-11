package com.android.liba.ui.base.loading;


import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.android.liba.R;

public enum LoadState {
    DEFAULT;

    public static @StringRes
    int getEmptyString(LoadState loadState) {
        return R.string.loading_error_empty;
    }

    public static @DrawableRes
    int getEmptyDrawable(LoadState loadState) {
        return 0;
    }

    //获取加载更多布局
    public static @LayoutRes
    int getLoadingMoreViewLayout(LoadState loadState) {
        return R.layout.load_more_default;
    }

    //获取加载更多没有数据文案
    public static @StringRes int getLoadingMoreNoDataText(LoadState loadState, int pageId) {
        return R.string.loading_more_no_data;
    }

    //加载更多出现异常时的文案
    public static @StringRes int getLoadMoreErrorText(LoadState loadState, boolean isNetworkError) {
        return R.string.loading_more_error_default;
    }
}
