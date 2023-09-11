package com.gys.play.ui.video

import android.view.View
import android.widget.TextView
import com.drake.brv.BindingAdapter
import com.gys.play.R
import com.gys.play.entity.VideoDetailBean
import com.mybase.libb.ext.getColor
import com.mybase.libb.ext.showGONE
import com.mybase.libb.ext.showVISIBLE
import kotlin.properties.Delegates

class ChooseVideoAdapter : BindingAdapter() {

    var playIndex = -1

    var payType by Delegates.notNull<Int>()

    init {

        addType<VideoDetailBean.VideoDetailChapterBean>(R.layout.item_video_anthology)


        onBind {

            val model = getModel<VideoDetailBean.VideoDetailChapterBean>()

            findView<TextView>(R.id.tvNum).also {
                it.text = "${modelPosition.plus(1)}"
                it.setTextColor((if (playIndex == layoutPosition) R.color.C_00ACFF else R.color.C_52576B).getColor())
            }
            findView<View>(R.id.ivStart).showGONE(playIndex == layoutPosition)


            val checkNeedBuy = VideoBayHelper.checkNeedBuy(payType, model)

            when (checkNeedBuy) {
                -1, 1 -> {
                    findView<View>(R.id.ivVip).showGONE()
                    findView<TextView>(R.id.tvCoin).let {
                        it.showVISIBLE()
                        it.text = "${model.coins}金幣"
                    }
                }
                -2, 2 -> {
                    findView<View>(R.id.ivVip).showVISIBLE()
                    findView<View>(R.id.tvCoin).showGONE()
                }
                else -> {
                    findView<View>(R.id.ivVip).showGONE()
                    findView<View>(R.id.tvCoin).showGONE()
                }
            }
        }
    }
}