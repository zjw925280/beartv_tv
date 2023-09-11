package com.gys.play.ui.video

import android.app.Activity
import com.shuyu.gsyvideoplayer.utils.OrientationUtils

class MyOrientationUtils(activity: Activity, private val player: MyVideoPlayer) :
    OrientationUtils(activity, player) {

    override fun setEnable(enable: Boolean) {
        if (player.showBayCoin) {
            super.setEnable(false)
            return
        }
        super.setEnable(enable)
    }
}