package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo

import com.gys.play.Config
import com.gys.play.entity.RechargeRecordInfo
import com.gys.play.fragment.open_record.RechargeListFragment
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer

class RechargeViewModel: NovelBasePresent<RechargeListFragment>() {
    fun getUserRechargeLog(month:String,page:Int,limit:Int,notify: BiConsumer<RechargeListFragment, BaseListInfo<RechargeRecordInfo>>){
        val data = NovelRequestArgs.init(Config.APP_USER_RECHARGELOG)
            .add("month",month)
            .add("page","$page")
            .add("limit","$limit")
            .build()
        loadData(getApiServer(AppNetService::class.java).getUserRechargeLog(data), notify)
    }
}