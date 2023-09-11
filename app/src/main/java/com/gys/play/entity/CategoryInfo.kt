package com.gys.play.entity

data class CategoryInfo(
    var category_title: String = "",
    var items: MutableList<BtVideoInfo>? = null
)