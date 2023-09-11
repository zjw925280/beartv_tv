package com.gys.play.entity

data class TabInfo(
    var title: String,
    var selimage: Int,
    var nolimage: Int,
    var wow_src: Int,
) {
    var select = false

    var wowModel = true

    val selimage_src get() = if (wowModel) wow_src else selimage

    val nolimage_src get() = if (wowModel) wow_src else nolimage
}