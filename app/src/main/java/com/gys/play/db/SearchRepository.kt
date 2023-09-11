package com.dm.cartoon.sql

import androidx.annotation.WorkerThread
import com.gys.play.db.SearchDao
import kotlinx.coroutines.flow.Flow


class SearchRepository(val dao: SearchDao) {
    val allDatas: Flow<List<Search>> = dao.getAllData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun insert(data: Search) {
        dao.insert(data)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun deletall() {
        dao.deletall()
    }
}