package com.mybase.libb.ui.web

import android.webkit.WebView
import com.just.agentweb.WebChromeClient


class LibWebChromeClient : WebChromeClient(){

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)


    }


}