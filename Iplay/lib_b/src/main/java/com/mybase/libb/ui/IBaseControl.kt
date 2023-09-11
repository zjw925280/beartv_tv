package com.mybase.libb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.mybase.libb.R


class IBaseControl(
    val base: IBase,
    val savedInstanceState: Bundle?,
    val owner: LifecycleOwner,
    val layoutInflater: LayoutInflater,
    val layoutId: Int
) {


    fun initControl() {

        val mState = base.findViewById<ViewGroup>(R.id.lib_viewContent)

        if (layoutId == 0) {
            base.inflateBinding(layoutInflater, mState)
        } else {
            layoutInflater.inflate(layoutId, mState)
        }

        base.initTitle()?.let {
            base.findViewById<FrameLayout>(R.id.lib_title).apply {
                visibility = View.VISIBLE
                addView(
                    it.root,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
            }
        }


    }

    fun initOther() {
        base.initView(savedInstanceState)

        base.observeUi(owner)

        base.initData()
    }


}