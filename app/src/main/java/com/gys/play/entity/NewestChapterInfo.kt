package com.gys.play.entity

data class NewestChapterInfo(
    var id: Int = 0,
    var sort: Int = 0,
    var book_id: Int = 0,
    var coins: Int = 0,
    var title: String = "",
    var updated: Long = 0
)
