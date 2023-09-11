package com.android.liba.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.android.liba.util.StatusBarUtils
import com.android.liba.util.UIHelper
import com.tencent.mmkv.MMKV

abstract class BindingActivity<P : BasePresent<*>, T : ViewBinding>(val layout: Int) :
    BaseActivity<P>(), LifecycleObserver {
    protected val TAG = this.javaClass.simpleName

    override fun getLayoutId(): Int = layout
    lateinit var binding: T

    override fun initView(savedInstanceState: Bundle?, fragmentView: View?) {
        binding = getViewDataBinding()
        initView()
        initData()
        StatusBarUtils.fullScreen(this, isStatusTextBlank())
        lifecycle.addObserver(this)
    }

    fun isStatusTextBlank() = true

    abstract fun initView()

    abstract fun initData()

    fun getMMKV(): MMKV? {
        return MMKV.defaultMMKV()
    }

    /**
     * 监听所有生命周期变化
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected open fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        UIHelper.showLog("onLifecycleChanged", "$TAG  >>  $event")
    }
}