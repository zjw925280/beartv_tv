package com.gys.play.db

import com.gys.play.db.dao.UserCacheTaskDao
import com.gys.play.db.entity.UserCacheTask

class UserCacheTaskRepository(val dao: UserCacheTaskDao) {

    @Suppress("RedundantSuspendModifier")
    fun insert(order: UserCacheTask) {
        dao.insert(order)
    }

    fun del(user: Int, bookId: Int) {
        dao.delete(user, bookId)
    }

}