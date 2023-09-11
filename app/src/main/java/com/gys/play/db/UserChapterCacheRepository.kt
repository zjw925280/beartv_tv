package com.gys.play.db

import com.gys.play.Config
import com.gys.play.db.dao.UserChapterCacheDao
import com.gys.play.db.entity.UserChapterCache

class UserChapterCacheRepository(val dao: UserChapterCacheDao) {

    fun getChapterIsDownload(user: String, chapterId: Int): Boolean {
        val chapter = getChapter(user, chapterId)
        if (chapter != null && chapter.status == Config.DOWNLOAD_END) {
            return true
        }
        return false
    }

    fun getChapter(user: String, chapterId: Int) = dao.getChapter(user, chapterId)


    fun insert(data: UserChapterCache) {
        dao.insert(data)
    }

    fun getNeedDownloadChater(user: Int, bookId: Int, chapterId: Int): UserChapterCache? {
        return dao.getChaterStatus(user, bookId, Config.DOWNLOAD_START, chapterId)
    }

    fun getChaterStatusList(user: Int, bookId: Int): List<UserChapterCache> {
        return dao.getChaterStatusList(user, bookId, Config.DOWNLOAD_START)
    }

}