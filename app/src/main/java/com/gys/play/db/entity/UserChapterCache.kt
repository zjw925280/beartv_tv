package com.gys.play.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户缓存章节id
 */
@Entity(tableName = "user_chapter_cache")
data class UserChapterCache(
    // 由user 和 chapterId 拼接 user_chapter_Id
    @PrimaryKey @ColumnInfo val key: String,
    val user: Int,
    val bookId: Int,
    val chapterId: Int,
    val update: Long,
    var status: Int,//状态，是否下载 0 默认值，未下载 1已下载
) {
}
