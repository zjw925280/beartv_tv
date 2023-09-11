package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.GoogsInfo
import com.gys.play.entity.OrderInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer

class GoogsViewModel<T> : NovelBasePresent<T>() {

    fun getAppVIPGoodsIndex(
        notify: BiConsumer<T, BaseListInfo<GoogsInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_GOODS_INDEX)
            .add("goods_type", "2")
            .build()
        loadData(getApiServer(AppNetService::class.java).getAppGoodsIndex(data), notify)
    }

    fun getAppGoodsIndex(
        notify: BiConsumer<T, BaseListInfo<GoogsInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_GOODS_INDEX)
            .add("goods_type", "1")
            .build()
        loadData(getApiServer(AppNetService::class.java).getAppGoodsIndex(data), notify)
    }

    fun getAppPayCreateOrder(
        goods_id: Int,
        bookId: Int,
        chapter_id: Int,
        notify: BiConsumer<T, OrderInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_PAY_CREATEORDER)
            .add("goods_id", "$goods_id")
        if (bookId > 0) {
            data.add("tv_id", "$bookId")
        }
        if (chapter_id > 0) {
            data.add("chapter_id", "$chapter_id")
        }
        loadData(getApiServer(AppNetService::class.java).getAppPayCreateOrder(data.build()), notify)
    }
}