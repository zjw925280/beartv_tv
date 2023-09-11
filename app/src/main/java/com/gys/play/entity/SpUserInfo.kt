package com.gys.play.entity

import com.android.liba.context.AppContext
import com.gys.play.CloudMsgUtil
import com.gys.play.util.GsonUtils
import com.gys.play.util.LiveDataUtil

object SpUserInfo {
    private const val USERINFO: String = "USER_INFO"

    private fun checkUserChange(userInfo: UserInfo) {
        val vip = userInfo.isVip()
        val isLoginNew = userInfo.isLogin()
        if (LiveDataUtil.isVip.value != vip) {
            LiveDataUtil.isVip.value = vip
        }
        if (LiveDataUtil.isLogin.value != isLoginNew) {
            LiveDataUtil.isLogin.value = isLoginNew
            if (isLoginNew) {
                CloudMsgUtil.init()
            }
        }
        if (LiveDataUtil.userInfo.value.toString() != userInfo.toString()) {
            LiveDataUtil.userInfo.value = userInfo
        }
    }

    fun getUserInfo(): UserInfo {
        getUserInfoJson()?.let {
            val userInfo = GsonUtils.fromJson(it, UserInfo::class.java)
            checkUserChange(userInfo)
            return userInfo
        }
        val userInfo = UserInfo()
        checkUserChange(userInfo)
        return userInfo
    }

    fun getUserInfoJson(): String? {
        return AppContext.getMMKV().getString(USERINFO, null)
    }

    fun saveUserInfo(userInfoBean: UserInfo) {
        if (userInfoBean.token.isEmpty()) {
            userInfoBean.token = checkToken()
        }
        AppContext.getMMKV().putString(USERINFO, GsonUtils.toJson(userInfoBean))
        checkUserChange(userInfoBean)
    }

    fun clear() {
        AppContext.getMMKV().remove(USERINFO)
        checkUserChange(UserInfo())
    }

    fun isLogin(): Boolean {
        return getUserInfo().isLogin()
    }

    fun isVip(): Boolean {
        return getUserInfo().isVip()
    }

    fun checkId(): String {
        return getUserInfo().id
    }

    fun checkToken(): String = getUserInfo().token

    fun checkUserName(): String = getUserInfo().user_name

    fun checkAvatar(): String = getUserInfo().avatar

    fun checkSign(): String = getUserInfo().sign
//    fun checkCoupon(): String = getUserInfo().coupon
//    fun checkFirstRecharge(): String = getUserInfo().first_recharge
//    fun checkIsCouponExpire(): String = getUserInfo().is_coupon_expire
}