package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.BtVideoInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import com.gys.play.ui.search.SearchActivity
import io.reactivex.functions.BiConsumer

class SearchModel : NovelBasePresent<SearchActivity>() {


    fun getBookSearch(
        keyword: String,
        page: Int,
        notify: BiConsumer<SearchActivity, BaseListInfo<BtVideoInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_BOOK_SEARCH)
            .add("keyword", keyword)
            .add("page", "$page")
            .add("limit", "20")
            .build()

        loadData(getApiServer(AppNetService::class.java).getBookSearch(data), notify)
    }

    fun getBookRank(
        notify: BiConsumer<SearchActivity, BaseListInfo<BtVideoInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_RANK)
            .add("sort_type", "2")
            .add("channel_type", "0")
            .add("limit", "20")
            .build()

        loadData(getApiServer(AppNetService::class.java).getBookRank(data), notify)
    }
}