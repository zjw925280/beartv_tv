package com.gys.play.ui.video

import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.VideoDetailBean
import com.mybase.libb.ext.getSafeInt

class VideoBayHelper(private val player: MyVideoPlayer, private val payType: Int) {

    companion object {

        var num = 0

        fun checkNeedBuy(payType: Int, bean: VideoDetailBean.VideoDetailChapterBean): Int {
            if (payType == 1) return if (bean.is_buy == 1) -1 else if (bean.coins.getSafeInt(0) <= 0) -11 else 1

            if (payType == 2) return if (SpUserInfo.getUserInfo().isVip()) -2 else 2

            return 0
        }
    }

    //判断是否购买,0不需要,1金币2vip
    fun checkNeedBuy(bean: VideoDetailBean.VideoDetailChapterBean): Int {
        return checkNeedBuy(payType, bean)
    }

    fun handlerTime(bean: VideoDetailBean.VideoDetailChapterBean, timeL: Long) {
        val time = timeL.div(1000)
        val checkNeedBuy = checkNeedBuy(bean)
        if (checkNeedBuy == 0) return

        if (checkNeedBuy == 1) {
            player.currentPlayer().checkStart(player.currentPlayer().getCurrentPosition())
        } else if (checkNeedBuy == 2) {
            val show = time >= 180
            player.currentPlayer().showVipHint = show
            if (show) {
                player.currentPlayer().seekTo(180 * 1000)
            }
        } else {
            if (player.currentPlayer().showVipHint) {
                player.currentPlayer().showVipHint = false
            }
        }
    }

}