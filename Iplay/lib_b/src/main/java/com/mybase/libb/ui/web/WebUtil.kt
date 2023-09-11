package com.mybase.libb.ui.web

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView



object WebUtil {

    @SuppressLint("SetJavaScriptEnabled")
    fun init(web: WebView){

        val mWebSettings = web.settings
        mWebSettings.setSupportZoom(false)
        mWebSettings.builtInZoomControls = false
        mWebSettings.defaultTextEncodingName = "utf-8"
        mWebSettings.javaScriptEnabled = true
        mWebSettings.defaultFontSize = 16
        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mWebSettings.setGeolocationEnabled(true)   //允许访问地址

        //允许访问多媒体
        mWebSettings.allowFileAccess = true
        mWebSettings.allowFileAccessFromFileURLs = true
        mWebSettings.allowUniversalAccessFromFileURLs = true

        mWebSettings.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }
}