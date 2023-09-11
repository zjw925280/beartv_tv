package com.gys.play

import CHttp
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.multidex.MultiDex
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import com.android.liba.context.AbstractApplication
import com.android.liba.exception.Error
import com.android.liba.util.UIHelper
import com.appsflyer.AppsFlyerLib
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.Utils
import com.dm.cartoon.sql.SearchRepository
import com.drake.brv.utils.BRV
import com.drake.statelayout.StateConfig
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.gys.play.db.AppDatabase
import com.gys.play.db.DataViewModel
import com.gys.play.entity.SpUserInfo
import com.gys.play.http.AppNetService
import com.gys.play.ui.login.LoginFragHelper
import com.gys.play.util.KvUserInfo
import com.gys.play.util.LiveDataUtil
import com.gys.play.util.MulLanguageUtil
import com.gys.play.util.dialog.WaitingDialog
import com.gys.play.viewmodel.NovelBasePresent
import com.jccppp.start.LaunchUtil
import com.lzx.pref.KvPrefModel
import com.lzx.pref.Serializer
import com.mybase.libb.coroutines.ErrorInfo
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.ssl.HttpsUtils
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


class MyApplication : AbstractApplication() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDataBase(this, applicationScope) }
    val searchRepository by lazy { SearchRepository(database.SearchDao()) }
    val dataViewModel by lazy { DataViewModel(this) }

    override fun getBaseUrl() = Config.BaseUrl
    override fun getDominName() = Config.DoMainName
    override fun getIpAddr() = Config.IP
    override fun getApiServer() = AppNetService::class.java

    private val activityList = mutableListOf<Activity>()

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(
                context
            ).also { it.setColorSchemeResources(R.color.key_color) }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(
                context
            )
        }
        StateConfig.errorLayout = R.layout.state_load_error
        StateConfig.loadingLayout = R.layout.state_load_loading
        StateConfig.emptyLayout = R.layout.state_load_empty
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        instance = this
        super.onCreate()

//        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        GSYVideoType.setRenderType(GSYVideoType.SUFRACE)

        BRV.modelId = BR.item

        UIHelper.showLog("启动", "MyApplication onCreate")

        KvPrefModel.initKvPref(this, object : Serializer {
            override fun serializeToJson(value: Any?): String? {
                return GsonUtils.toJson(value)
            }

            override fun deserializeFromJson(json: String?, type: Type): Any? {
                return GsonUtils.fromJson(json, type)
            }
        })
        UIHelper.showLog("启动", "MyApplication onCreate 1")

        LaunchUtil.init({
            KvUserInfo.isLogin()
        }, { a, i ->
            LoginFragHelper.load(a) {
                i.isLogin()
            }
        })

        CHttp.init { t, showToast ->
            object : ErrorInfo(t, showToast) {
                override fun otherError(throwable: Throwable): String {
                    if (throwable is Error) {
                        errorCode = throwable.code

                        val show = NovelBasePresent.controlErr(errorCode, throwable.message)

                        return if (show) throwable.message ?: "" else ""
                    }
                    return ""
                }
            }
        }

        RxHttpPlugins.init(getDefaultOkHttpClient()).setDebug(false, false)

        BRV.modelId = BR.item

        pay = GooglePayUtils(this)
        pay.init()
        firebaseAnalytics = FirebaseAnalyticsUtil(Firebase.analytics)
        WebView(this).destroy()
        LiveDataUtil.userInfo.value = SpUserInfo.getUserInfo()

        UIHelper.showLog("启动", "MyApplication onCreate 2")

        AppsFlyerLib.getInstance().init("sonQmPTYMNpFgvFjTMuUq8", null, this)

        AppsFlyerLib.getInstance().setDebugLog(true)
        AppsFlyerLib.getInstance().start(this)

        val environment: String = AdjustConfig.ENVIRONMENT_PRODUCTION
//        val environment: String = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, AdjustUtil.appToken, environment)
        if (BuildConfig.DEBUG)
            config.setLogLevel(LogLevel.ERROR)
        Adjust.onCreate(config)

        UIHelper.showLog("启动", "MyApplication onCreate 3")

        ActivityUtils.addActivityLifecycleCallbacks(object : Utils.ActivityLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity) {
                super.onActivityCreated(activity)
                activityList.add(activity)
                MulLanguageUtil.checkSetLanguage(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                super.onActivityDestroyed(activity)
                activityList.remove(activity)
            }

            override fun onActivityResumed(activity: Activity) {
                Adjust.onResume()
                if (activity is FragmentActivity) {
                    WaitingDialog.hide(activity)
                }
            }

            override fun onActivityPaused(activity: Activity) {
                Adjust.onPause()
                if (activity is FragmentActivity) {
                    WaitingDialog.hide(activity)
                }
            }
        })
        UIHelper.showLog("启动", "MyApplication onCreate 4")

    }

    private fun getDefaultOkHttpClient(): OkHttpClient {
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    companion object {
        private lateinit var instance: MyApplication
        private lateinit var pay: GooglePayUtils
        private lateinit var firebaseAnalytics: FirebaseAnalyticsUtil

        @JvmStatic
        fun getInstance(): MyApplication {
            return instance
        }

        @JvmStatic
        fun getPay(): GooglePayUtils {
            return pay
        }

        @JvmStatic
        fun getAnalytics(): FirebaseAnalyticsUtil {
            return firebaseAnalytics
        }
    }

    /**
     * 通过 MainActivity.get 获取当前语言的 resources
     *语言切换的时候首页会顶掉所有的Activity，这时候 resources 已经变成切换的语言
     *  application由于没有重新创建， resoures还是旧的,会导致有部分通过Myapplication获取语言的页面文字错乱
     *  ---要用Activity的Resource，不能用Application的Context的Resource
     */
    fun getActivityResources(): Resources {
        if (activityList.size > 0) return activityList[0].resources
        return super.getResources()
    }

    fun goHome() {
        for (item in activityList) {
            if (item !is MainActivity) {
                item.finish()
            }
        }
    }

    fun getActivity(): Activity? {
        for (item in activityList) {
            if (!item.isFinishing) return item
        }
        return null
    }

    fun isCurrentChina(): Boolean {
        return R.string.lang_type.getString() == "3"
    }

    fun isCurrentEnglish(): Boolean {
        return R.string.lang_type.getString() == "2"
    }

}

fun Int.getString() = MyApplication.getInstance().getActivityResources().getString(this)

