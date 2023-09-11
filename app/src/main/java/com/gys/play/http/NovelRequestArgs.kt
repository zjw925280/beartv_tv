package com.gys.play.http

import com.android.liba.network.protocol.RequestArgs
import com.android.liba.util.AppUtil
import com.gys.play.Config
import com.gys.play.MyApplication.Companion.getInstance
import com.gys.play.R
import com.gys.play.entity.SpUserInfo

class NovelRequestArgs(service: String, version_code: Int, osType: String) :
    RequestArgs(service, version_code, osType) {
    companion object {
        fun init(service: String): NovelRequestArgs {
            val data = NovelRequestArgs(service, AppUtil.getVersionCode(getInstance()), "android")
            data.add("user_id", SpUserInfo.checkId())
            data.add("token", SpUserInfo.checkToken())
            data.add("channel", Config.CHANNEL)
            data.add("package", getInstance().packageName)
            //2-英语 3-繁体
            data.add(
                "lang_type",
                getInstance().getActivityResources().getString(R.string.lang_type)
            )
            return data
        }
    }

    //不需要打印了
//    override fun build(): String {
//        if (BuildConfig.DEBUG) {
//            val s = Gson().toJson(buildMap())
//            UIHelper.showLog("NovelRequestArgs", "json $s")
//        }
//        return super.build()
//    }
}