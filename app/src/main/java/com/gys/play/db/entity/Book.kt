package com.gys.play.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gys.play.MyApplication
import com.gys.play.R

@Entity(tableName = "book")
data class Book(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo var pay_type: Int = 0,
    @ColumnInfo var vip: Int = 0,
    @ColumnInfo var score: Double = 0.0,
    @ColumnInfo var chapter_id: Int = 0,
    @ColumnInfo var total_views: Double = 0.0,
    @ColumnInfo var total_favors: Double = 0.0,
    @ColumnInfo var category_name: String = "",
    @ColumnInfo var thumb: String = "",
    @ColumnInfo var name: String = "",
    @ColumnInfo var author_name: String = "",
    @ColumnInfo var author_id: String = "",
    @ColumnInfo var description: String = "",
    @ColumnInfo var finish_txt: String = "",
    @ColumnInfo var tag: String = "",
    @ColumnInfo var word_txt: String = "",
    @ColumnInfo var bookshelf: Int = 0,
    @ColumnInfo var had_read: Int = 0,
    @ColumnInfo var finish: Int,
    @ColumnInfo var lang_type: Int, //1-中文 2-英文 3-繁体
    var downloadNumber: Int = 0,
) {
    fun getDownloadNumberText(): String {
        return MyApplication.getInstance().getActivityResources()
            .getString(R.string.download_number_chapter, "$downloadNumber")
    }
}
