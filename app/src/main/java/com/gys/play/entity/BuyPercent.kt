package com.gys.play.entity

data class BuyPercent(
    val chapter_num: Int = -1,
    val percent: Int = 0,
    val unit_price: Int = 0,
    val can_buy_num: Int = 0
) {
    fun saleCoins(): String {
        var off = percent / 100f
        var coins = 0f
        coins = off * chapter_num * unit_price
        var i = Math.round(coins) //四舍五入
        return i.toString()
    }

    fun oldCoins(): Int {
        var coins = 0
        coins = chapter_num * unit_price
        return coins
    }
}