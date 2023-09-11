package com.gys.play.http.jj

import android.content.Context
import android.text.TextUtils
import com.android.liba.network.compose.NetworkCompose
import com.android.liba.util.ReflexUtil
import okhttp3.Cache
import okhttp3.CacheControl.Companion.FORCE_NETWORK
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

abstract class BaseHttpMethods<Service> {
    val httpService: Service
    val okHttpClient: OkHttpClient
    private val baseUrl:String

    init {
        baseUrl = getBaseUrl()
        if (TextUtils.isEmpty(baseUrl)) {
            throw NullPointerException("getBaseUrl should not return null")
        }
        val connectTimeOutSecond = getConnectTimeOutSecond()
        if (connectTimeOutSecond == 0) {
            throw NullPointerException("getConnectTimeOutSecond should not return 0")
        }
        val readTimeOutSecond = getReadTimeOutSecond()
        if (readTimeOutSecond == 0) {
            throw NullPointerException("getReadTimeOutSecond should not return 0")
        }
        //手动创建一个OkHttpClient并设置超时时间
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(connectTimeOutSecond.toLong(), TimeUnit.SECONDS)
        builder.readTimeout(readTimeOutSecond.toLong(), TimeUnit.SECONDS)
//        builder.hostnameVerifier(HostnameVerifier { hostname, session ->
//            if (TextUtils.isEmpty(hostname)) {
//                return@HostnameVerifier false
//            }
//            val enableHostArray = getEnableHostArray()
//            if (enableHostArray == null || enableHostArray.size == 0) true else Arrays.asList(*enableHostArray)
//                .contains(hostname)
//        })
        builder.addInterceptor(JJLoggerInterceptor())
        builder.addNetworkInterceptor(Interceptor { chain ->

            //添加网络拦截器
            val request = chain.request()
            val response = chain.proceed(request)
            if (!NetworkCompose.isNetworkAvailable()) {
                val maxStale = 60 * 60 * 24 * 7 // 没网 就1周可用
                response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            } else {
                response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", FORCE_NETWORK.toString())
                    .build()
            }
        })
        //data下
        val cacheDirectory = File(getContext().cacheDir, "HttpCache")
        builder.cache(Cache(cacheDirectory, 1024 * 1024 * 30))
        //sd卡下
        okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create()) //只能用于请求String等基本类型数据
            .addConverterFactory(GsonConverterFactory.create()) //只能请求json
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //rxjava2
            .baseUrl(baseUrl)
            .build()
        httpService = retrofit.create(getServiceClass())
    }

    abstract fun getContext(): Context

    private fun getServiceClass(): Class<Service> {
        return ReflexUtil.getParadigmClass(javaClass, 0)
    }

    /**
     * 必须重写
     */
    protected abstract fun getBaseUrl(): String

    /**
     * 必须重写
     */
    protected open fun getConnectTimeOutSecond(): Int {
        return 10
    }

    protected open fun getReadTimeOutSecond(): Int {
        return 10
    }

    //如果需要用cookie再说
//        builder.cookieJar(object : CookieJar {
//            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//                this@BaseHttpMethods.saveFromResponse(url, cookies)
//            }
//
//            override fun loadForRequest(url: HttpUrl): List<Cookie> {
//                return this@BaseHttpMethods.loadForRequest(url)
//            }
//        })
//    protected abstract fun saveFromResponse(httpUrl: HttpUrl?, list: List<Cookie?>?)
//
//    protected abstract fun loadForRequest(httpUrl: HttpUrl?): List<Cookie?>?
}