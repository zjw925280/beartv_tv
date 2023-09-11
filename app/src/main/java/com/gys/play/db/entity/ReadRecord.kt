package com.gys.play.db.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ReadRecord(
    @PrimaryKey @ColumnInfo val id: Int,
    val user: String,
    val bookId: String,
    val chapter: String? = null,//最新阅读章节
    val chapterIndex: Int = 0,//最新阅读章节索引
    val createTime: Long
)
