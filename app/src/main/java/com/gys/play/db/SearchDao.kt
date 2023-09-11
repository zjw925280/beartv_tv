package com.gys.play.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dm.cartoon.sql.Search
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Query("select * from search ORDER by time desc limit 10 ")
    fun getAllData(): Flow<List<Search>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(search: Search)

    @Query(value = "delete from search")
    fun deletall()

}