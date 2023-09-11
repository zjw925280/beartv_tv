package com.gys.play.entity

import com.gys.play.Config
import com.gys.play.util.StringUtils

/*(id=157,
 goods_name=2100 Coins,
  amount=29.99,
   total_coins=2520,
    pay_time=1641862740,
 pay_type=Google)*/
data class RechargeRecordInfo(
    var id: Int,
    var goods_name: String,
    var amount: String,
    var total_coins: Int,
    var pay_time: Long,
    var pay_type: String
) {
    fun getTime(): String {
        return StringUtils.dateConvert(pay_time * 1000, Config.FORMAT_TIME_YMDHM)
    }
}