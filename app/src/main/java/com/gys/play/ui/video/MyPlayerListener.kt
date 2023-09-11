package com.gys.play.ui.video

import com.gys.play.entity.VideoDetailBean

interface MyPlayerListener {

    fun playChange(position: Int)

    //第几集,金币
    fun showBayForCoin(index: Int, bean: VideoDetailBean.VideoDetailChapterBean)

    fun showChoosePop()

}