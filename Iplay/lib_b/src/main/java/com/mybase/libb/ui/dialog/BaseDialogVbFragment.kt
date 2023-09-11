package com.mybase.libb.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.mybase.libb.ext.inflateBindingWithGeneric

abstract class BaseDialogVbFragment<Vb : ViewBinding> : BaseDialogFragment() {
    override val layoutId: Int
        get() = 0

    private var _binding: Vb? = null
    val mBind: Vb get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBindingWithGeneric(inflater)
        mView = _binding!!.root
        return mView
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}