package com.mybase.libb.ui.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mybase.libb.ext.inflateBindingWithGeneric
import com.mybase.libb.ui.BaseActivity

abstract class BaseVbActivity<Vb : ViewBinding> : BaseActivity(0) {

    lateinit var mBind: Vb

    override fun inflateBinding(inflater: LayoutInflater, vg: ViewGroup) {
        mBind = inflateBindingWithGeneric(inflater)
        vg.addView(mBind.root)
    }

}