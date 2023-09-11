package com.gys.play.db.dao

import androidx.room.*
import com.gys.play.entity.AutoPayChapter
import kotlinx.coroutines.flow.Flow

@Dao
interface AutoPayChapterDao {
    @Query("select * from AutoPayChapter ")
    fun getAll(): Flow<List<AutoPayChapter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: AutoPayChapter)

    @Query("select * from AutoPayChapter where id = :id")
    fun findId(id: Int): AutoPayChapter

    @Delete
    fun delete(autoPayChapter: AutoPayChapter)
}