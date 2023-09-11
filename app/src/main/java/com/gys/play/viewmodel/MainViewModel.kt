package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.HomeBannerInfo
import com.gys.play.entity.SimpleReturn
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.UserInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import com.tencent.mmkv.MMKV
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

class MainViewModel<T> : NovelBasePresent<T>() {

    fun appPayVerifyOrder(
        order_no: String,
        google_product_id: String,
        google_purchase_token: String,
        notify: Consumer<SimpleReturn>
    ) {
        val data = NovelRequestArgs.init(Config.APP_PAY_VERIFYORDER)
            .add("order_no", order_no)
            .add("google_product_id", google_product_id)
            .add("google_purchase_token", google_purchase_token)
            .build()
        loadDataWithNoLifecyle(
            getApiServer(AppNetService::class.java).appPayVerifyOrder(data),
            notify
        )
    }

    fun getUserInfo(
        notify: Consumer<UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_GETUSERINFO)
            .build()
        loadDataWithNoLifecyle(getApiServer(AppNetService::class.java).getUserInfo(data), notify)
    }

    fun guest() {
        if (SpUserInfo.isLogin()) {
            return
        }
        val uid = MMKV.defaultMMKV()!!.getString("userUid", null)
        uid ?: let {
            val data = NovelRequestArgs.init(Config.GUEST).build()
            loadDataWithNoLifecyle(
                getApiServer(AppNetService::class.java).guest(data)
            ) {
                MMKV.defaultMMKV()!!.putString("userUid", it.uid)
            }
        }
    }

    fun payError(orderNo: String, responseCode: Int, debugMessage: String) {
        val data = NovelRequestArgs.init(Config.PAY_ERROR)
            .add("order_no", orderNo)
            .add("res_code", "$responseCode")
            .add("res_msg", debugMessage)
            .build()
        loadDataWithNoLifecyle(
            getApiServer(AppNetService::class.java).payError(data)
        ) {

        }
    }

    fun getHomePositionAdInfo(
        position_type: Int = 4,
        notify: BiConsumer<T, BaseListInfo<HomeBannerInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_HOME_POSITION)
            //        运营版位：1-我的中部 2、小喇叭通知 3- 悬浮窗 4-首页广告
            .add("position_type", "$position_type")
            .build()

        loadData(getApiServer(AppNetService::class.java).appHomePosition(data), notify)
    }
}