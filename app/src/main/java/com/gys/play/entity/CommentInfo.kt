package com.gys.play.entity

import com.mybase.libb.ui.rv.IBaseItem

/**
 *id	整型	评论ID
book_id	整型	小说ID
user_id	整型	用户ID
user_name	字符串	用户昵称
parent_user_id	整型	被回复的用户ID
parent_user_name	字符串	被回复的用户昵称
content	字符串	评论内容
agree_count	整型	点赞数量
is_agree	整型	是否点赞
followCommentCount	整型	跟评数量
followCommentList	字符串	跟评列表
 */
data class CommentInfo(
    val id: Int,
    val book_id: Int,
    val type: Int,
    val resource_id: String,
    val user_id: Int,
    val user_name: String,
    val avatar: String,
    val parent_user_id: Int,
    val parent_user_name: String,
    val content: String,
    var agree_count: Int,
    var is_agree: Int,
    var is_vip: Int,
    var followCommentCount: Int,
    val followCommentList: List<CommentInfo>,
    val time_str: String,
    val book: MyCommentBookInfo,
    var selected: Boolean,
) : IBaseItem {
    fun getTime(): String {
        return time_str
    }

    fun isVip(): Boolean {
        return is_vip == 1
    }

    val agree get() = is_agree == 1
}