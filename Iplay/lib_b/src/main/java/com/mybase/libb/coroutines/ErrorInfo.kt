package com.mybase.libb.coroutines

import android.text.TextUtils
import com.blankj.utilcode.util.NetworkUtils
import com.google.gson.JsonSyntaxException
import com.mybase.libb.ext.showToast
import okhttp3.internal.http2.StreamResetException
import rxhttp.wrapper.exception.HttpStatusCodeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException


abstract class ErrorInfo(throwable: Throwable, var showToast: Boolean = true) {

    var errorCode: Int = 0
    var errorMsg = ""

    init {
        if (throwable is java.net.UnknownHostException) {
            if (NetworkUtils.isConnected()) {
                errorMsg = "请求连接超时,请检查当前网络"
            } else {
                errorMsg = "网络连接不可用，请稍后重试！"
            }
        } else if (throwable is ConnectException || throwable is SocketTimeoutException || throwable is TimeoutException) {
            //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
            errorMsg = "服务器连接失败"
        } else if (throwable is ConnectException) {
            errorMsg = "网络不给力，请稍候重试！"
        } else if (throwable is CancellationException||throwable is StreamResetException) {
            showToast = false
        } else if (throwable is JsonSyntaxException) { //请求成功，但Json语法异常,导致解析失败
            errorMsg = "数据解析失败,请稍后再试"
        } else if (throwable is HttpStatusCodeException) { //请求失败异常
            val code = throwable.getLocalizedMessage()
            errorMsg = if ("416" == code) {
                "请求范围不符合要求"
            } else {
                throwable.message ?: "未知错误"
            }
        } else {
            errorMsg = otherError(throwable)
        }
        throwable.printStackTrace()
        if (showToast && !TextUtils.isEmpty(errorMsg)) {
            showToast(errorMsg)
        }
    }

    abstract fun otherError(throwable: Throwable): String
}