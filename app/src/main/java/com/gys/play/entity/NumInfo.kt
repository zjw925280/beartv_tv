package com.gys.play.entity

data class NumInfo(
    val can_buy_num: Int,
    val can_download_num: Int,
    val unit_price: Int,
    val buy_percent: List<BuyPercent>
)
