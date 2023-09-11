package com.mybase.libb.ui.web

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner


open class BaseJsInterface(mActivity: FragmentActivity) {

    private val deliver = Handler(Looper.getMainLooper())

    init {
        mActivity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                deliver.removeCallbacksAndMessages(null)
            }
        })
    }

    fun runUi(r: Runnable) {
        deliver.post(r)
    }

    fun runUi(time: Int, r: Runnable) {
        deliver.postDelayed(r, time.toLong())
    }

}