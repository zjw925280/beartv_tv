package com.gys.play.http.jj.entity

import com.gys.play.entity.BtVideoInfo
import com.gys.play.entity.NovelRankList

data class NovelStyleInfo(
    val id: Int,
    val name: String,
    val channel_id: Int,
    val style: Int, //展示样式 1-竖排3，2-竖排6，3-竖排1横排3，4-竖排3横排3，5-排行榜样式，6-水平滑动，7-列表样式，8-横1竖水平滑动，9-竖水平滑动，10-分类
    val icon: String,
    val is_more: Int,//是否显示更多 0-不显示 1-显示
    val default_limit: Int,
    val items: MutableList<BtVideoInfo>?,
    val remaining_time: Int,
    val rank_list: NovelRankList
) {

    fun getNovelInfo(): BtVideoInfo {
        val element = BtVideoInfo(datatype = style, id = id, title = name)
        element.icon = icon
        element.is_more = is_more
        element.remaining_time = remaining_time
        if (items == null) {
            element.rank_list = rank_list
        } else {
            element.list = items
            for (i in 0 until items.size) {
                items[i].position = i
            }
        }
        return element
    }

    override fun toString(): String {
        return "NovelStyleInfo(channel_id=$channel_id, icon='$icon', id=$id, items=$items, name='$name', style=$style)"
    }
}
