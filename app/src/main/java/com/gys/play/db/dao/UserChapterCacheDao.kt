package com.gys.play.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gys.play.db.entity.Book
import com.gys.play.db.entity.UserChapterCache

@Dao
interface UserChapterCacheDao {

    @Query("select * from user_chapter_cache where user =:user and chapterId =:chapterId")
    fun getChapter(user: String, chapterId: Int): UserChapterCache

    /**
     * 获取用户缓存的书籍信息
     */
    @Query("select b.*, count(chapterId) as downloadNumber  from book b inner join user_chapter_cache u where b.id = u.bookId and u.status = 1 and u.user =:user group by b.id")
    fun getBookCache(user: String): List<Book>

    /**
     *获取章节
     */
    @Query("select * from user_chapter_cache where `key` =:key and status =:status")
    fun getChater(key: String, status: Int): List<UserChapterCache>

    /**
     *获取一条章节数据
     */
    @Query("select * from user_chapter_cache where user =:user and bookId =:bookId and status =:status and chapterId >:chapter limit 1")
    fun getChaterStatus(user: Int, bookId: Int, status: Int, chapter: Int): UserChapterCache

    /**
     *获取所有符合条件的数据
     */
    @Query("select * from user_chapter_cache where user =:user and bookId =:bookId and status =:status ")
    fun getChaterStatusList(user: Int, bookId: Int, status: Int): List<UserChapterCache>


    /**
     *  需要设置一个主键，不然不会自动替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: UserChapterCache)

}