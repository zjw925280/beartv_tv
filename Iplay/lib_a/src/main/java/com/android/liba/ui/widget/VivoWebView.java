package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class VivoWebView extends WebView {

    public VivoWebView(Context context) {
        super(getFixedContext(context));
    }

    public VivoWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
    }

    public VivoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
    }

    // To fix Android Lollipop WebView problem create a new configuration on that Android version only

    private static Context getFixedContext(Context context) {

        if (Build.BRAND.equalsIgnoreCase("vivo")&&(Build.VERSION.SDK_INT == 21 || Build.VERSION.SDK_INT == 22)) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }
}