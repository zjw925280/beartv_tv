package com.gys.play.ui.video

import androidx.viewpager2.widget.ViewPager2
import net.lucode.hackware.magicindicator.MagicIndicator


inline fun MyVideoPlayer.currentPlayer(): MyVideoPlayer {
    val mPlayer = currentPlayer as MyVideoPlayer
    return mPlayer
}

fun MagicIndicator.bind(vp2: ViewPager2) {
    vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@bind.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            this@bind.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@bind.onPageScrollStateChanged(state)
        }
    })
}