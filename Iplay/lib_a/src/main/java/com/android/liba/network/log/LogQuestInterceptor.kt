package com.android.liba.network.log

import okhttp3.Interceptor
import okhttp3.Response


class LogQuestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        // Prints the request start log
        LogUtil.logQuest(request)
        // Record the request start time
        request = request.newBuilder()
            .tag(LogTime::class.java, LogTime())
            .build()
        return chain.proceed(request)
    }
}