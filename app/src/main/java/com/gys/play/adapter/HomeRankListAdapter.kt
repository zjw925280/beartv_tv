package com.gys.play.adapter

import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.BR
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemHomeRankPageBinding
import com.gys.play.entity.BtVideoInfo

class HomeRankListAdapter(list: ArrayList<BtVideoInfo>) :
    Adapter<BtVideoInfo>(BR.item, R.layout.item_home_rank_page, list) {

    override fun convert(binding: ViewDataBinding, item: BtVideoInfo, position: Int) {
        val binding = binding as ItemHomeRankPageBinding
        when (position) {
            0 -> binding.imgRankNo.setImageResource(R.mipmap.icon_one)
            1 -> binding.imgRankNo.setImageResource(R.mipmap.icon_two)
            2 -> binding.imgRankNo.setImageResource(R.mipmap.icon_three)
            else -> binding.imgRankNo.setImageResource(R.mipmap.icon_number)
        }
        binding.tvPos.text = (position + 1).toString()
    }
}