package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.*
import com.gys.play.fragment.home.HomeFragment
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer

class HomeModel : NovelBasePresent<HomeFragment>() {

    fun getHomeChannel(
        notify: BiConsumer<HomeFragment, BaseListInfo<HomeChannelInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_HOME_CHANNEL)
            .build()

        loadData(getApiServer(AppNetService::class.java).getAppHomeChannel(data), notify)
    }

    fun getUserGiftStatus(notify: BiConsumer<HomeFragment, GiftInfo>) {
        val data = NovelRequestArgs.init(Config.APP_USER_GIFTSTATUS)
            .build()
        loadData(getApiServer(AppNetService::class.java).getUserGiftStatus(data), notify)
    }

    fun userGetGift(notify: BiConsumer<HomeFragment, StatusInfo>) {
        val data = NovelRequestArgs.init(Config.APP_USER_GETGIFT)
            .build()
        loadData(getApiServer(AppNetService::class.java).userGetGift(data), notify)
    }

    fun getfloatInfo(
        position_type: Int = 3,
        notify: BiConsumer<HomeFragment, BaseListInfo<HomeBannerInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_HOME_POSITION)
            .add("position_type", "$position_type")
            .build()

        loadData(getApiServer(AppNetService::class.java).appHomePosition(data), notify)
    }

    fun getUserInfo(
        notify: BiConsumer<HomeFragment, UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_GETUSERINFO)
            .build()

        loadData(getApiServer(AppNetService::class.java).getUserInfo(data), notify)
    }
}