package com.dm.cartoon.sql

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Search(
    @PrimaryKey val name: String,
    val time: Long = System.currentTimeMillis()
)