package com.gys.play.entity

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.gys.play.util.Timestamp

class Reply(
    val id: Int,
    val from_uid: Int,
    val from_name: String,
    val content: String,
    val to_name: String = "",
    val to_uid: Int,
    val created: Long,
) {

    fun getTime(): String {
        val t = System.currentTimeMillis() / 1000
        if (t < created) {
            return Timestamp.transToString(created * 1000)
        }
        //秒
        val sec = t - created
        //分钟
        val min = sec / 60
        if (min < 1) {
            return "刚刚"
        }
        if (min < 60) {
            return "${min}分钟前"
        }
        //小时
        val h = min / 60
        if (h < 24) {
            return "${h}小时前"
        }
        //天
        val tt = h / 24
        if (tt < 8) {
            return "${tt}天前"
        }

        return Timestamp.transToString(created * 1000)
    }

    var spannable: SpannableStringBuilder? = null

    fun getComment(): SpannableStringBuilder {
        if (spannable == null) {
            val ssb = SpannableStringBuilder()
            ssb.append(from_name + ":")
            val top = ssb.length
            ssb.setSpan(
                ForegroundColorSpan(Color.parseColor("#ff6352")),
                0,
                top,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            if (!TextUtils.isEmpty(to_name)) {
                ssb.append("回复 ")
                val top1 = ssb.length
                ssb.setSpan(
                    ForegroundColorSpan(Color.parseColor("#999999")),
                    top,
                    top1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                ssb.append(to_name + ":")
                val top2 = ssb.length
                ssb.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ff6352")),
                    top1,
                    top2,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
            val top3 = ssb.length;
            ssb.append(content)
            ssb.setSpan(
                ForegroundColorSpan(Color.parseColor("#999999")),
                top3,
                ssb.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable = ssb
        }
        return spannable!!
    }
}