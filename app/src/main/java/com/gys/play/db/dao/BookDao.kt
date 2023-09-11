package com.gys.play.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gys.play.db.entity.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("select * from book ")
    fun getAll(): Flow<List<Book>>

    @Query("select * from book where id =:id ")
    fun getBook(id: Int): Book

    /**
     * REPLACE 直接替换旧的
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: Book)

    /**
     * 移除
     */
    @Query("update book set bookshelf = 0 where id in (:ids )")
    fun removeBookshelf(ids :String)

}