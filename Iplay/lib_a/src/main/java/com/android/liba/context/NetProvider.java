package com.android.liba.context;


import com.android.liba.network.ApiService;

import java.util.List;

import okhttp3.Interceptor;

public interface NetProvider {
    String getBaseUrl();
    String getDominName();
    String getIpAddr();
    Class<? extends ApiService> getApiServer();

    default List<Interceptor> getCustomInterceptor() {
        return null;
    }

    void onHttpError(Throwable t);

}
