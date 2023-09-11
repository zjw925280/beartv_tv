package com.gys.play.entity

import android.content.Context
import com.android.liba.context.AppContext
import com.android.liba.util.AppUtil
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.util.dialog.UpdateDialog

class ServiceConfig(
    var mobile_login: Int = 0,//是否显示电话登录
    var facebook_login: Int = 0,//是否显示电话登录
    var google_forced_update_version: Int = 0, //	整型	google低于此版本强制升级
    var google_prompt_update_version: Int = 0, //	整型	google低于此版本提示升级
    var google_update_version_code: String = "", //升级的版本号
    var google_latest_version: Int = 0,//		整型	google最新版本
    var google_prompt_update_txt: String? = null, //升级文案
    var google_download_url: String? = null,//升级链接，当应用内升级时，就是apk下载链接；外部浏览器升级时，就是外跳链接
    var google_update_type: Int? = 0,//升级弹窗类型 0：去Google市场升级，1：应用内下载升级，2：外部浏览器下载升级
    var google_update_packagename: String? = null,//升级的包名，当是去Google市场升级的时候，如果有传 update_packagename，则跳到指定市场的指定应用页面（不一定是自己这个应用）。如果没传，就是跳自己这个应用
    var download_switch: Int = 0,//		整型	下说下载按钮开关    1-开启   0-关闭
    var language_switch: Int = 1,//		整型	多语言切换开关：1-开启 0-关闭
    var home_popup_frequency: Int = 0, //首页弹框   0-每次都弹  1-每天弹一次
    var ad_type: Int = 0,//0-关闭  1-google  2-穿山甲
    var unlock_by_ad: Int = 0,//是否可用广告解锁章节开关 ，1-开启，0-关闭
    var unlock_by_ad_day_times: Int = 0,//每天可用解锁次数
    var pay_mode_switch: Int = 0,//支付模式开关 0:-Google，1-H5支付页面
    var per_unlock_need_ad_times: Int = 0,//解锁一个章节需要看广告数量
    var vip_month_amount: Float = 0f,//每个月的VIP价格
    var vip_day_amount: String = "0",//每天的VIP价格
    var service_email: String? = null,//客服邮箱
    var view_ad_get_coin_times: Int = 0,//看广告得次数
    var view_ad_get_coin_per: Int = 0,//看一次广告获得
    var default_language: Int = 0, //加个默认语言配置 2-英文 3-繁体
) {
    fun showUpdateDialog(context: Context, isNotify: Boolean = false): UpdateDialog? {
        val versionCode = AppUtil.getVersionCode(MyApplication.getInstance())
        if (versionCode < google_forced_update_version || versionCode < google_prompt_update_version) {
            var rescissible = true
            if (versionCode < google_forced_update_version) {
                rescissible = false
            }
            return UpdateDialog.show(
                context,
                rescissible,
                google_prompt_update_txt,
                google_download_url,
                if (google_update_type == null) 0 else google_update_type!!,
                google_update_packagename,
                google_update_version_code
            )
        } else if (isNotify) {
            AppContext.showToast(context.getString(R.string.already_newest_version))
        }
        return null
    }

    fun isNeedUpdate(): Boolean {
        val versionCode = AppUtil.getVersionCode(MyApplication.getInstance())
        return versionCode < google_forced_update_version || versionCode < google_prompt_update_version
    }

    fun isCloseLanguageSwitch() = language_switch == 0
    override fun toString(): String {
        return "ServiceConfig(mobile_login=$mobile_login, facebook_login=$facebook_login, google_forced_update_version=$google_forced_update_version, google_prompt_update_version=$google_prompt_update_version, google_latest_version=$google_latest_version, download_switch=$download_switch, language_switch=$language_switch, home_popup_frequency=$home_popup_frequency, google_prompt_update_txt='$google_prompt_update_txt')"
    }
}