package com.gys.play.entity

import com.gys.play.db.entity.Book

data class VideoDetailInfo(
    var id: Int = 0,
    var pay_type: Int = 0,
    var is_vip: Int = 0,
    var score: Double = 0.0,
    var chapter_id: Int = 0,
    var total_views: Double = 0.0,
    var total_favors: Double = 0.0,
    var category_name: String = "",
    var thumb: String = "",
    var name: String = "",
    var author_name: String = "",
    var author: String = "",
    var author_id: String = "",
    var description: String = "",
    var finish_txt: String = "",
    var tag: String = "",
    var word_txt: String = "",
    var is_bookshelf: Int,
    var had_read: Int,
    var finish: Int,
    var lang_type: Int, //1-中文 2-英文 3-繁体
    var newest_chapter: NewestChapterInfo,
    var guess_like: MutableList<BtVideoInfo>,
    var is_score: Int,//  是否评分 1-已评分 0-未评分

) {
    fun getIntScore(): Int {
        if (score > 5) {
            return 5
        }
        return score.toInt()
    }

    fun isAddBookshelf(): Boolean {
        return is_bookshelf == 1
    }

    fun isVip(): Boolean = is_vip == 1
    fun getBook(): Book {
        return Book(
            id,
            pay_type,
            is_vip,
            score,
            chapter_id,
            total_views,
            total_favors,
            category_name,
            thumb,
            name,
            author_name,
            author_id,
            description,
            finish_txt,
            tag,
            word_txt,
            is_bookshelf,
            had_read,
            finish,
            lang_type,
        )
    }

}
