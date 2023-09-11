package com.mybase.libb.ui.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class BaseBottomDialog(context: FragmentActivity, theme: Int) :
    BottomSheetDialog(context, theme) {

    @get:LayoutRes
    protected abstract val layoutId: Int

    val mView: View = LayoutInflater.from(context).inflate(layoutId, null)

    private val mHeight get() = (ScreenUtils.getScreenHeight() * 0.7).toInt()

    protected abstract fun initView(view: View)

    init {
        mView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight)
        setContentView(mView)

        window?.setDimAmount(0f)

        getBehavior().setPeekHeight(mHeight)

    }

}