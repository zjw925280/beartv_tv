package com.mybase.libb.ui.vb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mybase.libb.ext.inflateBindingWithGeneric
import com.mybase.libb.ui.BaseFragment


abstract class BaseVbFragment<Vb : ViewBinding> : BaseFragment() {

    private var _binding: Vb? = null
    val mBind: Vb get() = _binding!!
    override fun layoutId() = 0
    var isInitBinding:Boolean = false

    override fun inflateBinding(inflater: LayoutInflater, vg: ViewGroup) {
        _binding = inflateBindingWithGeneric(inflater)
        vg.addView(_binding?.root)
        isInitBinding = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isInitBinding = false
    }
}