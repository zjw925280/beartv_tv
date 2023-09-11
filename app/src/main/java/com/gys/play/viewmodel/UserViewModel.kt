package com.gys.play.viewmodel


import com.android.liba.exception.Error
import com.android.liba.util.AppUtil
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.entity.ImageUrlInfo
import com.gys.play.entity.UserInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import io.reactivex.functions.BiConsumer
import okhttp3.MultipartBody

class UserViewModel<T> : NovelBasePresent<T>() {

    fun getLoginOauth(
        uid: String,
        idToken: String,
        notify: BiConsumer<T, UserInfo>,
        onError: BiConsumer<T, Error>
    ) {
        val data = NovelRequestArgs.init(Config.APP_LOGIN_OAUTH)
            .add("uid", "$uid")
            .add("token", "$idToken")
            .build()
        loadData(getApiServer(AppNetService::class.java).getLoginOauth(data), notify)
    }

    fun getUserCancel(
        notify: BiConsumer<T, UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_CANCEL).build()
        loadData(getApiServer(AppNetService::class.java).getUserCancel(data), notify)
    }

    fun appUserEdit(
        field: String,
        value: String,
        notify: BiConsumer<T, UserInfo>
    ) {
        val data = NovelRequestArgs.init(Config.APP_USER_EDIT)
            .add("field", field)
            .add("value", value).build()
        loadData(getApiServer(AppNetService::class.java).appUserEdit(data), notify)
    }

    fun uploadImage(
        field: MultipartBody.Part,
        sign: String,
        notify: BiConsumer<T, ImageUrlInfo>
    ) {
        loadData(
            getApiServer(AppNetService::class.java).uploadImage(
                Config.APP_UPLOAD_INDEX,
                MyApplication.getInstance().getActivityResources().getString(R.string.lang_type),
                "" + AppUtil.getVersionCode(MyApplication.getInstance()),
                Config.CHANNEL,
                "android",
                field,
                "avatar",
                sign
            ), notify
        )
    }

}