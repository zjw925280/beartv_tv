package com.gys.play.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户缓存章节id
 */
@Entity(tableName = "user_cache_task")
data class UserCacheTask(
    @PrimaryKey @ColumnInfo val id: Int? = null,
    val user: Int,
    val bookId: Int,
    val page: Int,//对比服务端章节信息到第几页 //每页一百条数据
)