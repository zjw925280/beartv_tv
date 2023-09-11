package com.gys.play.http

import com.android.liba.network.ApiService
import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.Config
import com.gys.play.entity.*
import com.gys.play.http.jj.entity.NovelStyleInfo
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface AppNetService : ApiService {

    @POST(Config.APP_START_CONFIG)
    suspend fun getAppStartConfigSSS(@Header("data") version: String): ServiceConfig

    @POST(Config.APP_LOGIN_OAUTH)
    fun getLoginOauth(@Header("data") version: String): Observable<UserInfo>

    @POST(Config.APP_HOME_POSITION)
    fun appHomePosition(@Header("data") version: String): Observable<BaseListInfo<HomeBannerInfo>>

    @POST(Config.APP_HOME_CHANNEL_CONFIG)
    suspend fun appHomeChannelConfig(@Header("data") version: String): BaseListInfo<NovelStyleInfo>

    @POST(Config.APP_HOME_CLICK)
    fun appHomeClick(@Header("data") version: String): Observable<StatusInfo>

    @POST(Config.APP_USER_GETUSERINFO)
    fun getUserInfo(@Header("data") version: String): Observable<UserInfo>

    @POST(Config.APP_USER_FEEDBACK_REPLY_NUM)
    fun getMsgInfo(@Header("data") version: String): Observable<MsgInfo>

    @POST(Config.APP_USER_CANCEL)
    fun getUserCancel(@Header("data") version: String): Observable<UserInfo>

    @POST(Config.APP_USER_EDIT)
    fun appUserEdit(@Header("data") version: String): Observable<UserInfo>

    @Multipart
    @POST(Config.APP_UPLOAD_INDEX)
    fun uploadImage(
        @Query("service") service: String,
        @Query("lang_type") lang_type: String,
        @Query("version_code") version_code: String,
        @Query("channel") channel: String,
        @Query("os_type") os_type: String,
        @Part file: MultipartBody.Part?,
        @Query("path") path: String,
        @Query("sign") sign: String,
    ): Observable<ImageUrlInfo>

    @Multipart
    @POST(Config.APP_UPLOAD_INDEX)
    fun uploadImageFeedback(
        @Query("service") service: String,
        @Query("lang_type") lang_type: String,
        @Part file: MultipartBody.Part?,
        @Query("path") path: String,
        @Query("sign") sign: String,
        @Query("os_type") os_type: String,
        @Query("version_code") version_code: String,
    ): Observable<ImageUrlInfo>

    @POST(Config.APP_GOODS_INDEX)
    fun getAppGoodsIndex(@Header("data") version: String): Observable<BaseListInfo<GoogsInfo>>

    @POST(Config.APP_PAY_CREATEORDER)
    fun getAppPayCreateOrder(@Header("data") version: String): Observable<OrderInfo>

    @POST(Config.APP_PAY_VERIFYORDER)
    fun appPayVerifyOrder(@Header("data") version: String): Observable<SimpleReturn>

    @POST(Config.APP_HOME_CHANNEL)
    fun getAppHomeChannel(@Header("data") version: String): Observable<BaseListInfo<HomeChannelInfo>>

    @POST(Config.APP_RANK)
    suspend fun getRank(@Header("data") version: String): BaseListInfo<BtVideoInfo>

    @POST(Config.APP_RANK)
    fun getBookRank(@Header("data") version: String): Observable<BaseListInfo<BtVideoInfo>>

    @POST(Config.APP_SHORTTV_LISTBY)
    suspend fun getAppShortTvListBy(@Header("data") version: String): BaseListInfo<BtVideoInfo>

    @POST(Config.APP_BOOK_SEARCH)
    fun getBookSearch(@Header("data") version: String): Observable<BaseListInfo<BtVideoInfo>>

    @POST(Config.APP_BOOK_CATEGORY_INDEX)
    fun getBookCategoryIndex(@Header("data") version: String): Observable<BookCategoryIndexInfo>

    @POST(Config.APP_BOOK_CATEGORY_INDEX)
    suspend fun getCategoryIndex(@Header("data") version: String): BookCategoryIndexInfo

    @POST(Config.APP_BOOK_CATEGORY_INDEX)
    suspend fun getBookCategoryIndexSSS(@Header("data") data: String): BookCategoryIndexInfo

    @POST(Config.APP_BOOK_CATEGORY_LIST)
    fun getBookCategoryList(@Header("data") version: String): Observable<BaseListInfo<BtVideoInfo>>

    @POST(Config.APP_BOOK_CATEGORY_LIST)
    suspend fun getCategoryList(@Header("data") version: String): BaseListInfo<BtVideoInfo>

    @POST(Config.APP_FEEDBACK_MAIN_TYPE)
    fun getFeedbackType(@Header("data") version: String): Observable<BaseListInfo<FeedbackTypeInfo>>

    @POST(Config.APP_FEEDBACK_MAIN_ADD)
    fun addFeedback(@Header("data") data: String): Observable<StatusInfo>

    @POST(Config.APP_USER_GIFTSTATUS)
    fun getUserGiftStatus(@Header("data") data: String): Observable<GiftInfo>

    @POST(Config.APP_USER_GIFTSTATUS)
    suspend fun getUserGiftStatusSSS(@Header("data") data: String): GiftInfo

    @POST(Config.APP_USER_GETGIFT)
    fun userGetGift(@Header("data") data: String): Observable<StatusInfo>

    @POST(Config.APP_USER_GETGIFT)
    suspend fun userGetGiftSSS(@Header("data") data: String): StatusInfo

    @POST(Config.APP_PAY_VIPLOG)
    fun getPayVipLog(@Header("data") data: String): Observable<BaseListInfo<VipOpenRecordInfo>>

    @POST(Config.APP_USER_RECHARGELOG)
    fun getUserRechargeLog(@Header("data") data: String): Observable<BaseListInfo<RechargeRecordInfo>>

    @POST(Config.APP_USER_CONSUMELOG)
    fun getUserConsumeLog(@Header("data") data: String): Observable<BaseListInfo<ConsumeRecordInfo>>

    @POST(Config.UN_AGREE_COMMENT)
    fun unAgreeComment(@Header("data") version: String): Observable<SimpleReturn>

    @POST(Config.GUEST)
    fun guest(@Header("data") version: String): Observable<UidReturn>

    @POST(Config.PAY_ERROR)
    fun payError(@Header("data") version: String): Observable<SimpleReturn>

    @POST(Config.APP_WOW_DELSUBSCRIBE)
    suspend fun delWowSubscribe(@Header("data") version: String): SimpleReturn

    @POST(Config.APP_SHORTTV_DELPLAYLOG)
    suspend fun delPlayLog(@Header("data") version: String): SimpleReturn

    @POST(Config.APP_WOW_SUBSCRIBELIST)
    suspend fun getWowSubscribeList(@Header("data") version: String): BaseListInfo<HistryInfo>

    @POST(Config.APP_SHORTTV_TVPLAYLOG)
    suspend fun getShorTvLog(@Header("data") version: String): BaseListInfo<HistryInfo>

    @POST(Config.APP_HOME_RECOMMEND_MORE)
    suspend fun getNovelMoreListSSS(@Header("data") version: String): NovelInfoListData

    @POST("App.ShortTv.Detail")
    suspend fun getVideoDetail(@Header("data") version: String): VideoDetailBean

    @POST("App.Wow.Index")
    suspend fun getWowList(@Header("data") version: String): BaseListInfo<WowDetailBean>

    @POST("App.Comment.CommentList")
    suspend fun getCommentList(@Header("data") version: String): BaseListInfo<CommentInfo>

    @POST("App.ShortTv.GuessLike")
    suspend fun getGuessList(@Header("data") version: String): BaseListInfo<BtVideoInfo>

    @POST("App.Wow.Info")
    suspend fun getLikeDetail(@Header("data") version: String): WowLikeDetailBean

    @POST("App.Wow.Like")
    suspend fun setLike(@Header("data") version: String): StatusInfo

    @POST("App.Wow.Subscribe")
    suspend fun addSubscribe(@Header("data") version: String): StatusInfo

    @POST("App.Comment.AgreeComment")
    suspend fun addAgreeComment(@Header("data") version: String): StatusInfo

    @POST("App.Comment.UnAgreeComment")
    suspend fun removeAgreeComment(@Header("data") version: String): StatusInfo

    @POST(Config.ADD_COMMENT)
    suspend fun addComment(@Header("data") version: String): StatusInfo

    @POST(Config.APP_READ_LOG)
    suspend fun addReadLog(@Header("data") data: String): StatusInfo

    @POST("App.ShortTv.Buy")
    suspend fun payVideo(@Header("data") data: String): StatusInfo

    @POST("App.Comment.MyCommentList")
    suspend fun myCommentList(@Header("data") data: String): BaseListInfo<CommentInfo>

    @POST("App.Comment.BatchDelComment")
    suspend fun deleteComment(@Header("data") data: String): StatusInfo

}