package com.gys.play.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AutoPayChapter")
data class AutoPayChapter(
    @PrimaryKey val id: Int,
    val isAuto: Int = 0
)