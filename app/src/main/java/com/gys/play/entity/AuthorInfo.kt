package com.gys.play.entity

data class AuthorInfo(
    var id: Int = 0,
    var avatar: String = "",
    var sign: String = "",
    var author_name: String = "",
    var book_list: MutableList<BtVideoInfo>
)
