package com.gys.play.entity

import android.graphics.Color
import com.gys.play.MyApplication
import com.gys.play.R

data class SignListData(
    val items: List<SignInfo>,
    val sign_days: Int,
    val is_sign_today: Int,//今天是否已签到 0：未签到，1：已签到
    val is_double_today: Int//今天是否可翻倍  0：不能翻倍，1：可以翻倍
) {
    /**
     * 今天是否已签到
     */
    fun isTodaySigned(): Boolean {
        return is_sign_today == 1
    }

    /**
     * 今天是否已翻倍
     */
    fun isTodayDoubled(): Boolean {
        return is_double_today == 0
    }

    /**
     * 今天是否可翻倍
     */
    fun isTodayDoubleEnable(): Boolean {
        return is_double_today == 1
    }
}

data class SignInfo(val days: Int, val type: Int, val coins: Int, val vip_day: Int) {
    //days 从1开始
    var sign_days: Int = 0
    var is_sign_today: Int = 0
    fun isSigned(): Boolean {
        return sign_days >= days
    }

    fun getItemBgColor(): Int {
        return if (isSigned()) {
            Color.parseColor("#FF604F")
        } else {
            Color.parseColor("#FFEFED")
        }
    }

    fun getTextColor(): Int {
        return if (isSigned()) {
            Color.parseColor("#ffffff")
        } else {
            Color.parseColor("#FF604F")
        }
    }

    fun isItemShowCoin(): Boolean {
        if (isSigned()) return false
        return type == 1
    }

    fun isItemShowVip(): Boolean {
        if (isSigned()) return false
        return type == 2
    }

    fun getRewardStr(): String {
        return if (type == 1) {
            MyApplication.getInstance().getActivityResources()
                .getString(R.string.sign_coins_item_text, coins)
        } else {
            MyApplication.getInstance().getActivityResources().getString(R.string.sign_vip, vip_day)
        }
    }
}
