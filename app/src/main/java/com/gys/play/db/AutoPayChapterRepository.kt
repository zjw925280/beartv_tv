package com.gys.play.db

import com.gys.play.db.dao.AutoPayChapterDao
import com.gys.play.entity.AutoPayChapter
import kotlinx.coroutines.flow.Flow

class AutoPayChapterRepository(val dao: AutoPayChapterDao) {
    val allDatas: Flow<List<AutoPayChapter>> = dao.getAll()

    fun insert(data: AutoPayChapter) = dao.insert(data)

    fun findId(id: Int): AutoPayChapter {
        var data = dao.findId(id)
        if (data == null) {
            data = AutoPayChapter(id)
        }
        return data
    }

    fun delete(data: AutoPayChapter) {
        dao.delete(data)
    }
}