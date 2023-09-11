package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.VipOpenRecordInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer

class VipOpenRecordViewModel<T> : NovelBasePresent<T>() {
    fun getPayVipLog(
        page: Int,
        limit: Int,
        notify: BiConsumer<T, BaseListInfo<VipOpenRecordInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_PAY_VIPLOG)
            .add("page", "$page")
            .add("limit", "$limit")
            .build()
        loadData(getApiServer(AppNetService::class.java).getPayVipLog(data), notify)
    }
}