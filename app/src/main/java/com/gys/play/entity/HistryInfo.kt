package com.gys.play.entity

import android.view.View
import com.mybase.libb.ext.notEmpty
import com.mybase.libb.ui.rv.IBaseItem

data class HistryInfo(
    val id: String,
    val title: String?,//短剧封面图
    val cover: String,//视频标题
    val wow_id: Int,//wow视频ID
    val tv_id: Int,//短剧ID
    val chapter_id: Int,//剧集ID
    val pay_type: Int,//1-付费 2-VIP
    val play_num: String,//视频播放量
    val progress: String,//视频播放量
    val chapter_title: String,//剧集标题
) : IBaseItem {
    var isSelect = false
    var topText: String? = null

    fun getTitleText(): String {
        return title ?: chapter_title
    }

    fun isVip(): Boolean = pay_type == 2

    fun getText2(): String {
        if (play_num.notEmpty()) {
            return "$play_num 次播放"
        }
        return "已看${progress}%"
    }

    fun getTopVisibile() = if (topText.isNullOrEmpty()) View.GONE else View.VISIBLE
}