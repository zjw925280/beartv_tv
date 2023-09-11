package com.gys.play.viewmodel

import com.android.liba.network.protocol.BaseListInfo
import com.google.firebase.auth.FirebaseAuth
import com.gys.play.Config
import com.gys.play.entity.HomeBannerInfo
import com.gys.play.entity.MsgInfo
import com.gys.play.entity.UserInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer


class MineViewModel<T> : NovelBasePresent<T>() {

    fun getLoginOauth(
        uid: String,
        notify: BiConsumer<T, UserInfo>
    ) {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful()) {
                val idToken: String? = it.getResult().getToken()
                // Send token to your backend via HTTPS
                val data = NovelRequestArgs.init(Config.APP_LOGIN_OAUTH)
                    .add("uid", "$uid")
                    .add("token", "$idToken")
                    .build()
                loadData(getApiServer(AppNetService::class.java).getLoginOauth(data), notify)
            } else {
                FirebaseAuth.getInstance().signOut()
            }
        }

    }

    fun getUserInfo(
        notify: BiConsumer<T, UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_GETUSERINFO)
            .build()

        loadData(getApiServer(AppNetService::class.java).getUserInfo(data), notify)
    }

    fun appHomePosition(
        position_type: Int = 1,
        notify: BiConsumer<T, BaseListInfo<HomeBannerInfo>>
    ) {
        val data = NovelRequestArgs.init(Config.APP_HOME_POSITION)
            //        运营版位：1-我的中部
            .add("position_type", "$position_type")
            .build()

        loadData(getApiServer(AppNetService::class.java).appHomePosition(data), notify)
    }

    fun getMsgInfo(
        notify: BiConsumer<T, MsgInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_FEEDBACK_REPLY_NUM)
            .build()
        loadData(getApiServer(AppNetService::class.java).getMsgInfo(data), notify)
    }

    fun appUserEdit(
        value: String,
        notify: BiConsumer<T, UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_EDIT)
            .add("field", "prefer")
            .add("value", value).build()
        loadData(getApiServer(AppNetService::class.java).appUserEdit(data), notify)
    }
}