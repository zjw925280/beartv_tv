package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo

import com.gys.play.Config
import com.gys.play.entity.ConsumeRecordInfo
import com.gys.play.fragment.open_record.ConsumeListFragment
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer

class ConsumeViewModel : NovelBasePresent<ConsumeListFragment>() {
    fun getUserConsumeLog(
        month: String,
        page: Int,
        limit: Int,
        notify: BiConsumer<ConsumeListFragment, BaseListInfo<ConsumeRecordInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_CONSUMELOG)
            .add("month", month)
            .add("page", "$page")
            .add("limit", "$limit")
            .build()
//        UIHelper.showLog(data.toString())
        loadData(getApiServer(AppNetService::class.java).getUserConsumeLog(data), notify)
    }
}