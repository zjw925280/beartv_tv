package com.mybase.libb.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.BarUtils


class StatusHeightView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attr, defStyleAttr) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        BarUtils.getStatusBarHeight().let {
            setMeasuredDimension(it,it)
        }
    }
}