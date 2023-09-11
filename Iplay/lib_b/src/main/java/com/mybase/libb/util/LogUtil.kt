package com.mybase.libb.util

import com.blankj.utilcode.util.LogUtils
import com.mybase.libb.BuildConfig


object LogUtil {

    private var switch = BuildConfig.DEBUG

    private var TAG = "TKLogUtil"

    fun setLogSwitch(switch: Boolean) {
        this.switch = switch
    }


    fun setLogTag(tag: String) {
        this.TAG = tag
    }

    fun e(vararg array: Any?) {
        if (!switch) return
        LogUtils.e(TAG, array)
    }

    fun d(vararg array: Any?) {
        if (!switch) return
        LogUtils.d(TAG, array)
    }

    fun i(vararg array: Any?) {
        if (!switch) return
        LogUtils.i(TAG, array)
    }

}