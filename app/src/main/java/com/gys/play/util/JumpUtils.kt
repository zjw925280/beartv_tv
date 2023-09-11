package com.gys.play.util

import android.content.Context
import com.android.liba.util.UIHelper
import com.gys.play.Config
import com.gys.play.WebActivity
import com.gys.play.ui.rank.RankActivity

object JumpUtils {
    fun jump(
        context: Context?,
        type: Int,
        position_id: Int,
        bookId: Int = 0,
        title: String? = "",
        url: String? = ""
    ) {
        context?.run {
            when (type) {
                Config.JUMP_VIP -> Config.toVip(context)
                Config.JUMP_RECHARGE -> Config.toRecharge(context)
                Config.JUMP_LINK -> WebActivity.start(context, title, url)
                Config.JUMP_RANK -> RankActivity.start(context)
                Config.JUMP_RANK_GIRLS -> RankActivity.start(context)
                Config.JUMP_RANK_BOYS -> RankActivity.start(context)
                Config.JUMP_FREE_LIMIT -> {}
                Config.JUMP_WELFARE -> {
                }
                Config.JUMP_LINK_BROWSER -> {
                    UIHelper.openWebBrowser(context, url)
                }
            }
        }
    }
}