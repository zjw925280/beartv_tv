package com.gys.play.entity

import com.gys.play.Config
import com.gys.play.util.StringUtils

data class TaskRecordInfo(
    var id: Int,
    var user_id: Int,
    var type: Int,
    var source: String,
    var coins: Int,
    var remark: String,
    var desc: String,
    var created: Long
) {
    fun getTime(): String {
        return StringUtils.dateConvert(created * 1000, Config.FORMAT_TIME_YMDHM)
    }

    fun getReward(): String {
        var s = ""
        s = "+" + coins
        return s
    }
}