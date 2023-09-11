package com.android.liba.network;

import com.android.liba.BuildConfig;
import com.android.liba.context.NetProvider;
import com.android.liba.network.log.LogQuestInterceptor;
import com.android.liba.util.AESImpl;
import com.android.liba.util.UIHelper;

import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {
    private final OkHttpClient client;
    NetProvider provider;
    private static ApiManager apiManager = null;

    public static Secret getSecret() {
        return AESImpl.getInstance();
    }

    public static ApiManager init(NetProvider provider) {
        apiManager = new ApiManager(provider);
        return apiManager;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public NetProvider getNetProvider() {
        return provider;
    }

    public static ApiManager getApiManager() {
        return apiManager;
    }

    private ApiManager(NetProvider provider) {
        this.provider = provider;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .dns(hostname -> {
                    try {
                        return Dns.SYSTEM.lookup(hostname);
                    } catch (UnknownHostException exception) {
                        if (hostname.contains(provider.getDominName())) {

                            return Arrays.asList(InetAddress.getAllByName(provider.getIpAddr()));
                        } else {
                            throw exception;
                        }
                    }
                })
                //添加应用拦截器
                .connectTimeout(35, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (provider.getCustomInterceptor() != null && !provider.getCustomInterceptor().isEmpty()) {
            for (Interceptor i : provider.getCustomInterceptor()) {
                builder.addInterceptor(i);
            }
        }
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LogQuestInterceptor())
                    .addInterceptor(new KtLoggerInterceptor());
        }
        builder.addInterceptor(new CommonInterceptor())
                .addInterceptor(new FilterInterceptor());
        //.addNetworkInterceptor(new OkhttpInterceptor().interceptor());
//                .addInterceptor(new KtLoggerInterceptor());
        client = builder
                .build();
        AESImpl.init(this);

    }

    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(provider.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    // 创建网络接口请求实例
    public <T extends ApiService> T createApi() {
        Retrofit retrofit = getRetrofit();
        UIHelper.showLog("MyApplication", "createApi Config.BaseUrl = " + provider.getBaseUrl());
        UIHelper.showLog("MyApplication", "createApi retrofit = " + retrofit);
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{provider.getApiServer()}, new ApiProxyHandler(retrofit.create(provider.getApiServer())));
    }

    public String getBaseUrl() {
        return provider.getBaseUrl();
    }

//    public String getAdMainName() {
//        return "ad.88765.com";
//    }

    public void onHttpError(Throwable t) {
        provider.onHttpError(t);
    }
}
