package com.gys.play.adapter

import androidx.databinding.ViewDataBinding
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemHomeCityRankItemBinding
import com.gys.play.entity.BtVideoInfo

class HomeCityRankItemAdapter(list: MutableList<BtVideoInfo>) :
    Adapter<BtVideoInfo>(BR.item, R.layout.item_home_city_rank_item, list) {

    override fun convert(binding: ViewDataBinding, item: BtVideoInfo, position: Int) {
        if (binding is ItemHomeCityRankItemBinding) {
            var positionBack = 0
            var positionTextColor = 0
            when (position) {
                0 -> {
                    positionBack = R.mipmap.icon_one
                    positionTextColor = R.color.red_F42330
                }
                1 -> {
                    positionBack = R.mipmap.icon_tow
                    positionTextColor = R.color.red_F85303
                }
                2 -> {
                    positionBack = R.mipmap.icon_three
                    positionTextColor = R.color.red_FFB600
                }
                else -> {
                    positionBack = R.mipmap.icon_number
                    positionTextColor = R.color.C_CECECE
                }
            }
            binding.position.setBackgroundResource(positionBack)
            binding.position.text = "${position + 1} "
            binding.position.setTextColor(binding.root.resources.getColor(positionTextColor))
            binding.tvNumber.text = item.getTotalViewsStr()
        }
    }
}