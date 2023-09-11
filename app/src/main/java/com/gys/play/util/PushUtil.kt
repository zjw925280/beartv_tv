package com.gys.play.util

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.android.liba.util.AppUtil
import com.gys.play.CloudMsgUtil
import com.gys.play.R
import com.gys.play.WebActivity

object PushUtil {

    fun checkPushIntent(activity: FragmentActivity, intent: Intent) {
        CloudMsgUtil.showLog("checkPushIntent activity = ${activity.javaClass.simpleName}", null)
        val type = intent.getStringExtra("type")
        val url = intent.getStringExtra("url")
        val title = intent.getStringExtra("title")
        val bookId = intent.getStringExtra("bookId")
        CloudMsgUtil.showLog(
            "checkPushIntent type = $type,url = $url,title = $title,bookId = $bookId",
            null
        )
        if (type.isNullOrEmpty()) return
        when (type) {
            "url" -> {
                if (url.isNullOrEmpty()) return
                WebActivity.start(
                    activity,
                    if (title.isNullOrEmpty()) activity.getString(R.string.link) else title,
                    url + "?name=" + AppUtil.getAppName(activity) + "&lang_type=" + activity.getString(
                        R.string.lang_type
                    )
                )
            }
            "novel" -> {
                if (bookId.isNullOrEmpty()) return
                try {
                    val bookIdInt = bookId.toInt()
                    if (bookIdInt == 0) return
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}