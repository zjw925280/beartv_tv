package com.android.liba.util;

import okhttp3.OkHttpClient;

public class OkHttpClientUtil {
    private static OkHttpClientUtil okHttpClientUtil;
    private static OkHttpClient okHttpClient;

    private OkHttpClientUtil() {
        okHttpClient = new OkHttpClient();
    }

    public static OkHttpClientUtil instance() {
        if (okHttpClientUtil == null || okHttpClient == null) {
            okHttpClientUtil = new OkHttpClientUtil();
        }
        return okHttpClientUtil;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
