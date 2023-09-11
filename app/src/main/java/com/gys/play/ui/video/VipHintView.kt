package com.gys.play.ui.video

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.gys.play.R
import com.gys.play.ui.VIPActivity
import com.mybase.libb.ext.setNoDouble

class VipHintView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    //用view实现更贴近需求

    val inflate = LayoutInflater.from(context).inflate(R.layout.pop_video_vip_hint, null)

    init {

        addView(inflate, LayoutParams(-2, -2).also {
            it.gravity = Gravity.CENTER
        })

        setBackgroundColor(Color.TRANSPARENT)

        setOnClickListener {}

        setOnTouchListener { _, _ -> true }

        onViewCreated()
    }

    private fun onViewCreated() {


        inflate.findViewById<View>(R.id.goVip).setNoDouble {
            VIPActivity.start(context)
        }

    }

    fun setBackListener(listener: OnClickListener) {
        inflate.findViewById<View?>(R.id.tvExit)?.setOnClickListener(listener)
    }


}