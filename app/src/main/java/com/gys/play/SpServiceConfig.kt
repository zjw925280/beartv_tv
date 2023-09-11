package com.gys.play

import com.android.liba.context.AppContext
import com.gys.play.entity.ServiceConfig
import com.gys.play.entity.SpUserInfo
import com.gys.play.util.GsonUtils

object SpServiceConfig {
    private const val SERVICE_CONFIG = "SERVICE_CONFIG"
    private var serviceConfig: ServiceConfig? = null

    fun isConfigPrepared(): Boolean {
        val json = AppContext.getMMKV().getString(SERVICE_CONFIG, null)
        return !json.isNullOrEmpty()
    }

    fun getServiceConfig(): ServiceConfig {
        serviceConfig?.run {
            return this
        }

        val json = AppContext.getMMKV().getString(SERVICE_CONFIG, null)
        json?.run {
            return GsonUtils.fromJson(this, ServiceConfig::class.java)
        }
        return ServiceConfig()
    }

    fun save(data: ServiceConfig) {
        serviceConfig = data
        AppContext.getMMKV().putString(SERVICE_CONFIG, GsonUtils.toJson(data))
    }

    fun isHideFacebook(): Boolean {
        return getServiceConfig().facebook_login != 1
    }

    fun isHidePhone(): Boolean {
        return getServiceConfig().mobile_login != 1
    }

    fun isHideDownload(): Boolean {
        return getServiceConfig().download_switch != 1
    }

    fun isShowAd(): Boolean {
        if (SpUserInfo.getUserInfo().isVip()) {
            return false
        }
        return getServiceConfig().ad_type != 0
    }

    fun isShowGgAd(): Boolean {
        if (SpUserInfo.getUserInfo().isVip()) {
            return false
        }
        return getServiceConfig().ad_type == 1
    }

    fun isShowTtAd(): Boolean {
        return getServiceConfig().ad_type == 2
    }

    fun isCanUnlockByAd(): Boolean {
        if (!isShowAd()) return false
        return getServiceConfig().unlock_by_ad == 1
    }

    fun getDayMaxUnlockCount(): Int {
        return getServiceConfig().unlock_by_ad_day_times
    }

    fun isGooglePay(): Boolean {
        return getServiceConfig().pay_mode_switch == 0
    }

    fun getUnlockNeedAdCount(): Int {
        var perUnlockNeedAdTimes = getServiceConfig().per_unlock_need_ad_times
        if (perUnlockNeedAdTimes == 0) perUnlockNeedAdTimes = 2
        return perUnlockNeedAdTimes
    }

    fun getVipMonthAmount(): Float {
        return getServiceConfig().vip_month_amount
    }

    fun getVipDayAmount(): String {
        return getServiceConfig().vip_day_amount
    }

    fun getServiceEmail(): String? {
        return getServiceConfig().service_email
    }

    fun getViewAdGetCoinTimes(): Int {
        return getServiceConfig().view_ad_get_coin_times
    }

    fun getViewAdGetCoinPer(): Int {
        return getServiceConfig().view_ad_get_coin_per
    }

    fun getDefaultLanguage(): Int {
        return getServiceConfig().default_language
    }

    fun isNeedUpdate(): Boolean {
        return getServiceConfig().isNeedUpdate()
    }
}