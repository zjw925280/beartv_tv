package com.gys.play.entity

data class FreeLimitInfo(
    var start_time: Long = 0,
    var end_time: Long = 0,
    var remaining_time: Int = 0,
    var items: MutableList<BtVideoInfo>? = null
)