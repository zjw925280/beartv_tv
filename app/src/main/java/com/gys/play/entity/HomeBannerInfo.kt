package com.gys.play.entity

import android.content.Context
import com.gys.play.util.JumpUtils

data class HomeBannerInfo(
    var id: Int,
    var position_id: Int,
    var title: String = "",
    var jump_url: String? = "",
    var thumb: String? = "",
    var jump_type: Int,
    var book_id: Int
) {
    fun jump(context: Context?) {
        JumpUtils.jump(context, jump_type, position_id, book_id, title, jump_url)
    }
}
