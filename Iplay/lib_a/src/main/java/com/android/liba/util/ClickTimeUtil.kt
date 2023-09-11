package com.android.liba.util

import android.view.View

/**
 *点击时间间隔的静态工具类
 */
object ClickTimeUtil {
    private const val TAG = "ClickTimeUtil"

    /**
     *  key  view.id
     *  value Pair.first 间隔时间  Pari.second 上次记录时间
     */
    private val map = hashMapOf<Int, Pair<Int, Long>>()

    /**
     * 默认点击间隔1秒
     */
    var CLICK_INTERVAL = 1000

    /**
     * 设置时间间隔默认毫秒
     */
    fun setClickTime(v: View, listener: View.OnClickListener) {
        setClickTime(v, CLICK_INTERVAL, listener)
    }

    /**
     * @param v view
     * @param time 间隔时间 毫秒单位
     * @param listener 回调
     */
    fun setClickTime(v: View, time: Int, listener: View.OnClickListener) {
        if (v == null) {
            return
        }
        val id = v.id
        val clickListener = View.OnClickListener {
            UIHelper.showLog(TAG, "ClickTimeUtil clickListener: " + System.currentTimeMillis())
            //获取时间信息
            var pair = map.get(id)
            if (pair == null) {
                pair = Pair(time, 0)
                map.put(id, pair)
            }
            //判断点击时间
            val currentTime = System.currentTimeMillis()
            val t = currentTime - pair.first - pair.second
            UIHelper.showLog(TAG, "clickTime: $currentTime ${pair.first} ${pair.second} $t id $id")
            if (t > 0) {
                pair = Pair(time, currentTime)
                map.put(id, pair)
                listener.onClick(it)
            }
        }
        v.setOnClickListener(clickListener)
    }
}