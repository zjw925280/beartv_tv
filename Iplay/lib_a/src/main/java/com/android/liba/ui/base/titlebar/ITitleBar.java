package com.android.liba.ui.base.titlebar;

import android.app.Activity;
import android.view.ViewGroup;

public interface ITitleBar {
    int getTitleLayout();
    void initTitleView(Activity activity, ViewGroup view, TitleBarConfig titleBarConfig);
}
