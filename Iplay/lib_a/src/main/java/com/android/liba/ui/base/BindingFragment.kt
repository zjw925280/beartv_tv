package com.android.liba.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.android.liba.util.UIHelper
import com.tencent.mmkv.MMKV

abstract class BindingFragment<P : BasePresent<*>, T : ViewBinding>
    : BaseFragment<P>(), LifecycleObserver {
    protected val TAG = this.javaClass.simpleName

    lateinit var binding: T


    private lateinit var mContext: Context

    val mActivity: FragmentActivity get() = mContext as FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun initView(savedInstanceState: Bundle?, fragmentView: View?) {
        super.initView(savedInstanceState, fragmentView)
        binding = getViewDataBinding()
        initView()
        initData()
        viewLifecycleOwner.lifecycle.addObserver(this)
    }

    abstract fun initData()

    abstract fun initView()

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