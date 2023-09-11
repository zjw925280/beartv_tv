package com.android.liba.util.js

import android.R
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface


open class BaseJsInterface(val mActivity: Activity, val callBack: BaseJsCallBack) {

    var mHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun finishActivity() {
        mHandler.post {
            if (!callBack.call("finishActivity")) {
                mActivity.finish()
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    @JavascriptInterface
    fun returnback() {
        mHandler.post {
            if (!callBack.call("returnback")) {
                mActivity.finish()
                mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    @JavascriptInterface
    fun backWeb() {
        mHandler.post { callBack.call("backWeb") }
    }

    @JavascriptInterface
    fun goToMarket() {
        mHandler.post { callBack.call("goToMarket") }
    }

    @JavascriptInterface
    fun shareLink() {
        mHandler.post { callBack.call("shareLink") }
    }

    @JavascriptInterface
    fun videoDownload(url: String?, img: String?) {
        mHandler.post { callBack.call("videoDownload", url, img) }
    }

    @JavascriptInterface
    fun checkinstall() {
        mHandler.post { callBack.call("checkinstall") }
    }



    @JavascriptInterface
    fun checkPayOrderState() { //充值完回调
        mHandler.post { callBack.call("checkPayOrderState") }
    }

    @JavascriptInterface
    fun vipUmeng(vip: String?) {
        mHandler.post { callBack.call("vipUmeng") }
    }

    @JavascriptInterface
    fun checkWeChat() {
        mHandler.post { callBack.call("checkWeChat") }
    }

    @JavascriptInterface
    fun getFreeVip() {
        mHandler.post { callBack.call("getFreeVip") }
    }



}