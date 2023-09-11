package com.gys.play.entity

data class NovelRankList(
    val must_read: MutableList<BtVideoInfo>,
    val over: MutableList<BtVideoInfo>,
    val newest: MutableList<BtVideoInfo>
) {
    fun rank() {
        rank(must_read)
        rank(over)
        rank(newest)
    }

    private fun rank(list: MutableList<BtVideoInfo>?) {
        list?.run {
            var i = 0
            for (item in list) {
                item.position = i++
            }
        }
    }
}
