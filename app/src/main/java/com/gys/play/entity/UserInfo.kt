package com.gys.play.entity

import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.util.StringUtils
import java.io.Serializable

/**
 * Created by Koi.
 * Date: 2021/8/11
 * explain:
 */
data class UserInfo(
    var id: String = "",
    var user_name: String = "",
    var sign: String = "",
    var sex: String = "",
    var birthday: String = "",
    var coins: String = "0",
    var status: String = "",
    var avatar: String = "",
    var vip_status: Int = -1, //是否vip   0-不是会员 1-会员 2-会员已过期
    var vip_expires_time: Long = 0, //会员有效期
    var vip_expires_text: String = "",
    var token: String = "",
    var vip_month_amount: Float = 0f,
    var is_author: Boolean = false,
    var is_new: Int? = 0,
    var is_first_recharge: Int = 1   //1 是首冲 0 不是

//    var ios_ingot: String,
//    var android_ingot: String,
//    var ticket: String,
//    var coupon: String,
//    var first_recharge: String, //是否首充 1-是首充 0-不是首充
//    var is_coupon_expire: String,
//    var is_audit_avatar: String,

) : Serializable {

    fun isLogin() = !id.isNullOrEmpty() && !token.isNullOrEmpty()


    fun isVip(): Boolean {
        return vip_status == 1
    }

    fun isFirstRecharge(): Boolean {
        return is_first_recharge == 1
    }

    fun getSexType(type: String): String {
        val context = MyApplication.getInstance().getActivityResources()
        when (type) {
            "1" -> return context.getString(R.string.male)
            "2" -> return context.getString(R.string.female)
            else -> return context.getString(R.string.unknown)
        }
    }

    fun getSexType(): String {
        return getSexType(sex)
    }

    fun remainingDays(): Int {
        val time = vip_expires_time - System.currentTimeMillis() / 1000
        return (time / 60 / 60 / 24).toInt()
    }

    fun getVipTime(): String {
        return StringUtils.dateConvert(
            vip_expires_time * 1000,
            Config.FORMAT_TIME_YMD
        )
    }

    fun getVipLeftDay(): Int {
        val currentTimeS = System.currentTimeMillis() / 1000
        val l = vip_expires_time - currentTimeS
        return if (l <= 0) {
            0
        } else {
            val daySeconds = 60 * 60 * 24
            val i = l % daySeconds
            val j = l / daySeconds

            if (i <= 0) {
                j.toInt()
            } else {
                (j + 1).toInt()
            }
        }
    }

    override fun toString(): String {
        return "UserInfo(id='$id', user_name='$user_name', sign='$sign', sex='$sex', birthday='$birthday', coins='$coins', status='$status', avatar='$avatar', vip_status=$vip_status, vip_expires_time=$vip_expires_time, vip_expires_text='$vip_expires_text', token='$token', vip_month_amount=$vip_month_amount, is_author=$is_author, is_new=$is_new, is_first_recharge=$is_first_recharge)"
    }

}
