package com.mybase.libb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding


interface IBase {

    fun initView(savedInstanceState: Bundle?)
    fun <T : View> findViewById(@IdRes id: Int): T
    fun initTitle(): ViewBinding?

    fun observeUi(owner: LifecycleOwner)
    fun inflateBinding(inflater: LayoutInflater, vg: ViewGroup)
    fun initData(tag: Any? = null)

    fun showProgress(txt: String? = "加载中...")
    fun dismissProgress()

}