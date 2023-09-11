package com.gys.play.db

import androidx.lifecycle.AndroidViewModel
import com.android.liba.util.MyLiveData
import com.gys.play.MyApplication

class DataViewModel(val application: MyApplication) : AndroidViewModel(application) {
    val TAG = "DataViewModel"

    val userCacheTaskRepository by lazy { UserCacheTaskRepository(application.database.userCacheTaskDao()) }
    val orderRepository by lazy { OrderRepository(application.database.orderDao()) }
    val bookRepository by lazy { BookRepository(application.database.bookDao()) }
    val userChapterCacheRepository by lazy { UserChapterCacheRepository(application.database.userChapterCache()) }

    val isRefreashBookShelf: MyLiveData<Boolean> = MyLiveData<Boolean>()
    val isFinishWelfare: MyLiveData<Boolean> = MyLiveData<Boolean>()
    val isBatchPay: MyLiveData<Boolean> = MyLiveData<Boolean>()

}