package com.gys.play.ui.helpfeedback

import android.content.Context
import android.os.Build
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.network.protocol.RequestArgs
import com.android.liba.util.AppUtil
import com.gys.play.Config
import com.gys.play.entity.FeedbackTypeInfo
import com.gys.play.entity.ImageUrlInfo
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.StatusInfo
import com.gys.play.http.AppNetService
import com.gys.play.http.NovelRequestArgs
import com.gys.play.viewmodel.NovelBasePresent
import io.reactivex.functions.BiConsumer
import okhttp3.MultipartBody

class HelpAndFeedbackViewModel : NovelBasePresent<HelpAndFeedbackActivity>() {

    fun getFeedbackType(notify: BiConsumer<HelpAndFeedbackActivity, BaseListInfo<FeedbackTypeInfo>>) {
        val data = NovelRequestArgs.init(Config.APP_FEEDBACK_MAIN_TYPE)
            .build()
        loadData(getApiServer(AppNetService::class.java).getFeedbackType(data), notify)
    }

    fun uploadImage(
        service: String,
        lang_type: String,
        field: MultipartBody.Part,
        sign: String,
        os_type: String,
        version_code: String,
        notify: BiConsumer<HelpAndFeedbackActivity, ImageUrlInfo>
    ) {
        loadData(
            getApiServer(AppNetService::class.java).uploadImageFeedback(
                service,
                lang_type,
                field,
                "feedback",
                sign,
                os_type,
                version_code
            ), notify
        )
    }

    fun addFeedback(
        context: Context,
        phone: String,
        opinion: String,
        type: String,
        images: String,
        notify: BiConsumer<HelpAndFeedbackActivity, StatusInfo>
    ) {
        val args =
            RequestArgs.getOsType(Config.APP_FEEDBACK_MAIN_ADD, AppUtil.getVersionCode(context))
                .add("user_id", SpUserInfo.getUserInfo().id)
                .add("brand", Build.BRAND)                           //品牌
                .add("model", Build.MODEL)                           //型号
                .add("sys", Build.VERSION.RELEASE)                   //系统
                .add("channel", Config.CHANNEL)    //渠道
                .add("version", AppUtil.getVersionName(context))     //版本名
                .add("phone", phone)                                 //联系方式
                .add("opinion", opinion)                             //反馈意见
                .add("type", type)                                   //反馈类型
                .add("images", images)                               //反馈图片列表，分割
                .build()
        loadData(getApiServer(AppNetService::class.java).addFeedback(args), notify)
    }
}