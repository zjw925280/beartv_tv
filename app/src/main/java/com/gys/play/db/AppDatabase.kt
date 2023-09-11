package com.gys.play.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gys.play.entity.AutoPayChapter
import com.dm.cartoon.sql.Search
import com.gys.play.db.dao.*
import com.gys.play.db.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = arrayOf(
        Order::class,
        Book::class,
        UserChapterCache::class,
        Search::class,
        UserCacheTask::class,
        AutoPayChapter::class,
    ), version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun bookDao(): BookDao
    abstract fun userChapterCache(): UserChapterCacheDao
    abstract fun SearchDao(): SearchDao
    abstract fun userCacheTaskDao(): UserCacheTaskDao
    abstract fun AutoPayChapterDao(): AutoPayChapterDao

    private class DataCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDataBase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "novel"
                ).addCallback(DataCallback(scope))
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}