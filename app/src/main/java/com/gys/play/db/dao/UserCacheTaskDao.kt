package com.gys.play.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gys.play.db.entity.UserCacheTask

@Dao
interface UserCacheTaskDao {
    /**
     * REPLACE 直接替换旧的
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: UserCacheTask)

    @Query("delete from user_cache_task where user =:user and bookId =:bookId ")
    fun delete(user: Int, bookId: Int)

}