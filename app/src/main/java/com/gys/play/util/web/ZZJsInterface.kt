package com.atlight.novel.util.web

import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import com.android.liba.context.AppContext
import com.android.liba.util.GsonUtil
import com.android.liba.util.UIHelper
import com.android.liba.util.WebUtil
import com.gys.play.Config
import com.gys.play.SpServiceConfig
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.GoldOpenRecordActivity
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.VipOpenRecordActivity
import com.gys.play.ui.helpfeedback.HelpAndFeedbackActivity
import com.gys.play.util.LiveDataUtil
import com.gys.play.util.PayUtil


class ZZJsInterface(
    val activity: FragmentActivity,
    private val webView: WebView,
    private val mWebUtil: WebUtil
) {
    private var isLogin = false
    private val TAG = "ZZJsInterface"

    init {
        isLogin = isLogin()
        LiveDataUtil.isLogin.observe(activity) {
            val isLoginNew = isLogin()
            if (isLogin != isLoginNew) {
                isLogin = isLoginNew
                if (isLogin) {
                    onLogin()
                }
            }
        }
    }

    @JavascriptInterface
    fun getUserInfo(): String {
        UIHelper.showLog(TAG, "getUserInfo")
        //获取用户json字符串信息
        return SpUserInfo.getUserInfoJson().toString()
    }

    @JavascriptInterface
    fun goBack() {
        UIHelper.showLog(TAG, "goBack")
        //web返回上个页面，如果已经是第一个了，则关闭
        mWebUtil.defBackWeb()
    }

    @JavascriptInterface
    fun goLogin() {
        UIHelper.showLog(TAG, "goLogin")
        //去登录
        activity.startActivity(Intent(activity, LoginActivity::class.java))
    }

    @JavascriptInterface
    fun isLogin(): Boolean {
        UIHelper.showLog(TAG, "isLogin")
        //判断是否已登录
        return SpUserInfo.isLogin()
    }

    private fun onLogin() {
        UIHelper.showLog(TAG, "onLogin")
        //登录成功调用--原生调用H5
        webView.evaluateJavascript(
            "javascript:onLogin()", null
        )
    }

    @JavascriptInterface
    fun finishActivity() {
        UIHelper.showLog(TAG, "finishActivity")
        //关闭页面--标题栏返回键的功能
        activity.finish()
    }

    @JavascriptInterface
    fun onPaySuccess() {
        UIHelper.showLog(TAG, "onPaySuccess")
        //支付成功时调用
        //TODO 好像没有要做什么
    }

    @JavascriptInterface
    fun goVipOpenRecord() {
        UIHelper.showLog(TAG, "goVipOpenRecord")
        //VIP开通记录
        activity.startActivity(Intent(activity, VipOpenRecordActivity::class.java))
    }

    @JavascriptInterface
    fun goGoldOpenRecord() {
        UIHelper.showLog(TAG, "goGoldOpenRecord")
        //明细记录
        activity.startActivity(Intent(activity, GoldOpenRecordActivity::class.java))
    }

    @JavascriptInterface
    fun goEmail() {
        UIHelper.showLog(TAG, "goEmail")
        //遇到问题，去发送邮件
        val serviceEmail = SpServiceConfig.getServiceEmail()
        if (serviceEmail.isNullOrEmpty()) {
            return
        }
        activity.startActivity(
            Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:$serviceEmail"))
        )
    }

    @JavascriptInterface
    fun goFeedback() {
        UIHelper.showLog(TAG, "goFeedback")
        //帮助反馈
        activity.startActivity(Intent(activity, HelpAndFeedbackActivity::class.java))
    }

    @JavascriptInterface
    fun goGooglePay(orderInfo: String) {
        UIHelper.showLog(TAG, "goGooglePay $orderInfo")
        val googlePayInfo =
            GsonUtil.instance().fromJson(orderInfo, PayUtil.GooglePayInfo::class.java)
        PayUtil.doGooglePay(
            activity,
            googlePayInfo.google_product_id,
            googlePayInfo.price,
            googlePayInfo.order_no,
            googlePayInfo.user_id
        )
    }

    @JavascriptInterface
    fun showToast(text: String) {
        UIHelper.showLog(TAG, "showToast $text")
        //Toast
        AppContext.showToast(text)
    }

    @JavascriptInterface
    fun goUserAgreement() {
        //跳转用户协议
        UIHelper.showLog(TAG, "goUserAgreement")
        Config.toUserRulePage(activity)
    }

    @JavascriptInterface
    fun goPrivacyPolicy() {
        //跳转隐私政策
        UIHelper.showLog(TAG, "goPrivacyPolicy")
        Config.toPrivacyPage(activity)
    }

    @JavascriptInterface
    fun goVipServiceAgreement() {
        //跳转vip服务协议
        UIHelper.showLog(TAG, "goVipServiceAgreement")
        Config.toVipRulePage(activity)
    }
}