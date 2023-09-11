package com.gys.play.util

import android.content.Context

class DensityUtilKt {
    companion object {
        fun dip2px(context: Context, dp: Float): Int {
            val density = context.resources.displayMetrics.density
            return (density * dp + 0.5f).toInt()
        }

        fun px2dip(context: Context, px: Int): Int {
            val density = context.resources.displayMetrics.density
            val dp = (px / density + 0.5).toInt()
            return dp
        }
    }
}