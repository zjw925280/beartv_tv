package com.gys.play.entity

data class MyCommentBookInfo(
    val book_id: Int,
    val thumb: String = "",
    val name: String = "",
    val category_name: String = "",
    val author_name: String = ""
)