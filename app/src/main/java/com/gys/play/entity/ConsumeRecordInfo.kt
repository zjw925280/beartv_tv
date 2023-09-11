package com.gys.play.entity

import com.gys.play.Config
import com.gys.play.util.StringUtils

data class ConsumeRecordInfo(
    var id: Int,
    var tv_title: String,
    var chapter_title: String,
    var coins: Int,
    var created: Long,
) {
    fun getTime(): String {
        return StringUtils.dateConvert(created * 1000, Config.FORMAT_TIME_YMDHM)
    }
}