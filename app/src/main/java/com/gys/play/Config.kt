package com.gys.play

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.android.liba.util.AppUtil
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.RechargeActivity
import com.gys.play.ui.VIPActivity


object Config {

    const val CHANNEL = "google"//渠道

    //    const val DOWNLOAD_CHECK = -1
    const val DOWNLOAD_START = 0
    const val DOWNLOAD_END = 1

    const val GOOGLE_ORDER_CONSUMPTION_ERROR = -1 //用户支付 APP 消费失败
    const val GOOGLE_ORDER_USER_PAY = 1 //用户支付 未消费
    const val GOOGLE_ORDER_CONSUMPTION = 2 //用户支付 APP 已经消费 未发货
    const val GOOGLE_ORDER_END = 3 //用户支付 APP 已经消费 已发货

    const val NOVEL_ID = "novel_id"
    const val CHAPTER_ID = "chapter_id"
    const val IS_LAUNCH_GO = "is_launch_go"
    const val IS_FIRST = "is_first"
    const val HAS_FREQUENCY = "has_frequency"
    const val GOOGLE_PAY_SUCCESS = "google_pay_success"
    const val FIRST_MUST_READ_BOOKID = "first_must_read_bookid"
    const val WEB_TITLE = "web_title"
    const val WEB_URL = "web_url"
    const val FIRST_GIFT = "first_gift"

    const val JUMP_VIP = 1
    const val JUMP_RECHARGE = 2
    const val JUMP_LINK = 4 //实际上是内部浏览器链接
    const val JUMP_RANK = 5
    const val JUMP_RANK_GIRLS = 6
    const val JUMP_RANK_BOYS = 7
    const val JUMP_FREE_LIMIT = 8
    const val JUMP_GIFT_DIALOG = 9
    const val JUMP_WELFARE = 13
    const val JUMP_LINK_BROWSER = 14 //外部浏览器链接

    //  todo   banner 12 热门推荐 1 排行榜5  服务端返回14   精品好剧 3
    const val HOME_NOVEL_STYLE_1 = 1
    const val HOME_NOVEL_STYLE_2 = 2
    const val HOME_NOVEL_STYLE_3 = 3
    const val HOME_NOVEL_STYLE_4 = 4
    const val HOME_NOVEL_STYLE_5 = 5
    const val HOME_NOVEL_STYLE_6 = 6
    const val HOME_NOVEL_STYLE_7 = 7
    const val HOME_NOVEL_STYLE_8 = 8
    const val HOME_NOVEL_STYLE_9 = 9
    const val HOME_NOVEL_STYLE_10 = 10
    const val HOME_NOVEL_STYLE_11 = 11
    const val HOME_NOVEL_STYLE_12 = 12
    const val HOME_NOVEL_STYLE_13 = 13
    const val HOME_NOVEL_STYLE_14 = 14
    const val HOME_NOVEL_STYLE_15 = 15

    const val HOME_FOR_YOUR_LIST = 0//要为0！！！，默认，没有另外赋值的
    const val HOME_BANNER = 101
    const val HOME_RANK = 102
    const val HOME_MUST_READ = 103
    const val HOME_FOR_YOUR = 104
    const val JINGPING_BOOK = 105
    const val HOME_FREE_LIMIT = 108
    const val HOME_FREE_LIMIT_EVERYDAY = 109
    const val HOME_NEW_BOOK = 110
    const val HOME_OVER_BOOK = 111
    const val HOME_SAVE_SHORTAGE_BOOK = 114//就是个标题
    const val HOME_SEX_18NEW = 115
    const val HOME_OPERATING = 116 //运营位
    const val HOME_SHORT_TV_TYPE = 117 //短剧底部的两个选择 4-播放量 5-评分

    const val Url = "?s="

    /**
     * 提示，网络请求999时打开页面
     */
    fun toHttp999ErrorPage(activity: Context) {
        AuthorWebActivity.start(
            activity,
            activity.getString(R.string.tips),
            BaseUrl + Url + "App.Site.Warning&user_id="
                    + SpUserInfo.getUserInfo().id
                    + "&os_type=android" + "&lang_type="
                    + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 消息中心
     */
    fun toMessageCenterPage(activity: FragmentActivity) {
        AuthorWebActivity.start(
            activity,
            activity.getString(R.string.message_center),
            BaseUrl + Url + "App.Feedback_Main.lists&user_id="
                    + SpUserInfo.getUserInfo().id
                    + "&os_type=android" + "&lang_type="
                    + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 用戶服務協議   用户协议
     */
    fun toUserRulePage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.user_agreement),
            BaseUrl + Url + "App.Agreement.Index&company=popular-novel&name=" +
                    AppUtil.getAppName(activity) + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 隐私政策   隐私政策
     */
    fun toPrivacyPage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.privacy_policy),
            BaseUrl + Url + "App.Privacy.Index&company=popular-novel&name=" +
                    AppUtil.getAppName(activity) + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * vip服务协议
     */
    fun toVipRulePage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.terms_for_vips),
            BaseUrl + Url + "App.Agreement.Vip&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 活動通用總則
     */
    fun toActivityCommonPage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.activity_common_rule),
            BaseUrl + Url + "App.Agreement.Activity" + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 賬號服務協議
     */
    fun toAccountServiceRulePage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.account_service_rule),
            BaseUrl + Url + "App.Agreement.Accounts" + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * UGC条款
     */
    fun toUGCRulePage(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.ugc_rule),
            BaseUrl + Url + "App.Agreement.Ugc" + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }

    /**
     * 軟件許可及服務協議
     */
    fun toSoftRule(activity: FragmentActivity) {
        WebActivity.start(
            activity,
            activity.getString(R.string.soft_rule),
            BaseUrl + Url + "App.Agreement.Software" + "&lang_type=" + activity.getString(R.string.lang_type)
        )
    }


    const val APP_USHARE = Url + "App.Agreement.Ushare&lang_type="

    const val DoMainName = "www.beet-tv.com"
    const val BaseUrl: String = "http://$DoMainName/"

    var IP = DoMainName

    //        const val s = "?s="
    const val s = ""

    //登录
    const val APP_LOGIN_OAUTH = s + "App.Login.Oauth"

    const val APP_HOME_POSITION = s + "App.Home.Position"

    //首页样式配置
    const val APP_HOME_CHANNEL_CONFIG = s + "App.Home.ChannelConfig"

    //首页短剧底部列表 TV-2  4-播放量 5-评分
    const val APP_SHORTTV_LISTBY = s + "App.ShortTv.ListBy"

    // 接口描述： 统计运营位点击次数
    const val APP_HOME_CLICK = s + "App.Home.Click"

    //配置
    const val APP_START_CONFIG = s + "App.Start.Config"

    //获取用户信息
    const val APP_USER_GETUSERINFO = s + "App.User.GetUserInfo"

    //注销
    const val APP_USER_CANCEL = s + "App.User.Cancel"

    //编辑用户
    const val APP_USER_EDIT = s + "App.User.Edit"

    //图片文件上传
    const val APP_UPLOAD_INDEX = s + "App.Upload.Index"

    //支付商品列表
    const val APP_GOODS_INDEX = s + "App.Goods.Index"

    //创建订单
    const val APP_PAY_CREATEORDER = s + "App.Pay.CreateOrder"

    //验证订单
    const val APP_PAY_VERIFYORDER = s + "App.Pay.VerifyOrder"

    //首页频道列表
    const val APP_HOME_CHANNEL = s + "App.Home.Channel"


    const val FORMAT_TIME_YMDHM = "yyyy-MM-dd HH:mm"
    const val FORMAT_TIME_YMD = "yyyy-MM-dd"

    //首页排行榜
    const val APP_RANK = s + "App.ShortTv.Rank"

    //搜索
    const val APP_BOOK_SEARCH = s + "App.ShortTv.Search"

    //分类筛选条件
    const val APP_BOOK_CATEGORY_INDEX = s + "App.Category.Index"

    //分类筛选列表
    const val APP_BOOK_CATEGORY_LIST = s + "App.Category.FilterList"

    //意見反饋類型
    const val APP_FEEDBACK_MAIN_TYPE = s + "App.Feedback_Main.Type"

    //反馈意见API
    const val APP_FEEDBACK_MAIN_ADD = s + "App.Feedback_Main.Add"

    //新人礼包状态
    const val APP_USER_GIFTSTATUS = s + "App.User.GiftStatus"

    //领取新人礼包
    const val APP_USER_GETGIFT = s + "App.User.GetGift"

    //添加阅读历史记录
    const val APP_READ_LOG = s + "App.ShortTv.ReadLog"

    //我的-消息-小红点
    const val APP_USER_FEEDBACK_REPLY_NUM = s + "App.User.FeedbackReplyNum"

    //vip开通记录
    const val APP_PAY_VIPLOG = s + "App.Pay.VipLog"

    //用户充值记录
    const val APP_USER_RECHARGELOG = s + "App.User.RechargeLog"

    //用户消费记录
    const val APP_USER_CONSUMELOG = s + "App.User.ConsumeLog"

    //小说评论
    //发布评论
    const val ADD_COMMENT = "App.Comment.AddComment"

    //取消点赞评价
    const val UN_AGREE_COMMENT = "App.BookComment.UnAgreeComment"

    //新增用户
    const val GUEST = "App.Home.Guest"

    //支付错误
    const val PAY_ERROR = "App.Pay.PayError"

    //VIP开通页面
    const val VIP_URL = Url + "App.Payment_Main.Vip"

    //充值页面
    const val RECHARGE_URL = Url + "App.Payment_Main.Charge"

    //查看更多
    const val APP_HOME_RECOMMEND_MORE = s + "App.Home.RecommendMore"

    //    短剧观看历史 TV-2
    const val APP_SHORTTV_TVPLAYLOG = s + "App.ShortTv.TvPlayLog"

    //    wow订阅列表 TW
    const val APP_WOW_SUBSCRIBELIST = s + "App.Wow.SubscribeList"

    // 批量删除观看记录 TV-2
    const val APP_SHORTTV_DELPLAYLOG = s + "App.ShortTv.DelPlayLog"

    //    批量删除wow订阅 TW
    const val APP_WOW_DELSUBSCRIBE = s + "App.Wow.DelSubscribe"

    fun toVip(context: Context?, bookId: Int = 0, chapterId: Int = 0) {
        if (SpServiceConfig.isGooglePay()) {
            VIPActivity.start(context, bookId, chapterId)
        } else {
            AuthorWebActivity.start(
                context,
                context?.getString(R.string.vip_membership),
                BaseUrl + VIP_URL + getVipOrRechargeParamStr(bookId, chapterId)
            )
        }
    }

    fun toRecharge(context: Context?, bookId: Int = 0, chapterId: Int = 0) {
        if (SpServiceConfig.isGooglePay()) {
            RechargeActivity.start(context, bookId, chapterId)
        } else {
            AuthorWebActivity.start(
                context,
                context?.getString(R.string.about_book_coins),
                BaseUrl + RECHARGE_URL + getVipOrRechargeParamStr(bookId, chapterId)
            )
        }
    }

    private fun getVipOrRechargeParamStr(bookId: Int = 0, chapterId: Int = 0): String {
        val sb = StringBuilder()
        sb.append("&lang_type=")
        sb.append(MyApplication.getInstance().getActivityResources().getString(R.string.lang_type))
        sb.append("&os_type=")
        sb.append("android")
        sb.append("&version_code=")
        sb.append(AppUtil.getVersionCode(MyApplication.getInstance()))
        sb.append("&book_id=")
        sb.append(bookId)
        sb.append("&chapter_id=")
        sb.append(chapterId)
        sb.append("&channel=")
        sb.append(CHANNEL)
        sb.append("&package=")
        sb.append(MyApplication.getInstance().packageName)
        if (SpUserInfo.isLogin()) {
            sb.append("&user_id=")
            sb.append(SpUserInfo.checkId())
            sb.append("&token=")
            sb.append(SpUserInfo.checkToken())
        }
        return sb.toString()
    }
}