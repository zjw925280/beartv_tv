package com.gys.play.viewmodel

import android.content.Intent
import com.android.liba.context.AppContext
import com.android.liba.ui.base.BasePresent
import com.android.liba.util.UIHelper
import com.firebase.ui.auth.AuthUI
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.LoginActivity
import io.reactivex.Observable
import io.reactivex.functions.Consumer

open class NovelBasePresent<T> : BasePresent<T>() {

    override fun <T : Any?> loadDataWithNoLifecyle(
        observable: Observable<T>?,
        onNext: Consumer<T>?
    ) {
        loadDataWithNoLifecyle(observable, onNext) {
            handlerErro(null, it)
        }
    }

    open fun <T : Any?> loadDataWithNoLifecyle(
        observable: Observable<T>?,
        onNext: Consumer<T>?,
        onError: Consumer<Throwable>
    ) {
        observable?.subscribe(onNext, onError)
    }

    override fun handlerErro(view: T?, error: Throwable) {
        if (error is com.android.liba.exception.Error) {
//            401		未登录
//            402		账号锁定
//            403		token 过期，或其他异常
            val controlErr = controlErr(error.code, error.message)
            if (controlErr)
                AppContext.showToast(error.message)
        }
        error.printStackTrace()
    }

    companion object {
        fun controlErr(code: Int, msg: String?): Boolean {
            when (code) {
                400 -> {//普通错误

                }
                401 -> {
                    MyApplication.getInstance().run {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    return false
                }
                402 -> {
                }
                403 -> {
                    //登录错误 退出当前账户
                    try {
                        AuthUI.getInstance().signOut(MyApplication.getInstance())
                    } catch (e: Throwable) {
                        UIHelper.showLog("Http", "403 signOut Throwable $e")
                    }
                    SpUserInfo.clear()
                    return false
                }
                421 -> {
                    MyApplication.getAnalytics().setError(msg)
                    return false
                }
                420 -> {
                    MyApplication.getAnalytics().setError(msg)
                    return false
                }
                430 -> {
                    MyApplication.getInstance().run {
                        if (!SpUserInfo.isLogin()) {
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        } else {
                            Config.toRecharge(this)
                            AppContext.showToast(getString(R.string.enough_coins))
                        }
                    }
                    return false
                }
                999 -> {
                    val activity = MyApplication.getInstance().getActivity()
                    if (activity != null) {
                        Config.toHttp999ErrorPage(activity)
                    } else {
                        Config.toHttp999ErrorPage(MyApplication.getInstance())
                    }
                    return false
                }
            }
            return true
        }
    }
}