package com.gys.play.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemHomeCityRankBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.ui.rank.RankActivity

class HomeCityRankListAdapter(list: MutableList<MutableList<BtVideoInfo>>, val channel_id: Int) :
    Adapter<MutableList<BtVideoInfo>>(BR.item, R.layout.item_home_city_rank, list) {

    override fun convert(binding: ViewDataBinding, item: MutableList<BtVideoInfo>, position: Int) {
        if (binding is ItemHomeCityRankBinding) {
            var topText = R.string.hot_list
            var lookAllBack = R.drawable.shape_rect_stroke_fd4a47_12
            var lookAllTextColor = R.color.C_FD4A47
            var backDrawableId = R.mipmap.img_hot
            when (position) {
                0 -> {
                }
                1 -> {
                    backDrawableId = R.mipmap.img_new
                    topText = R.string.new_book_list
                    lookAllBack = R.drawable.shape_rect_stroke_0185fe_12
                    lookAllTextColor = R.color.C_0185FE
                }
                2 -> {
                    backDrawableId = R.mipmap.img_collect
                    topText = R.string.collection_list
                    lookAllBack = R.drawable.shape_rect_stroke_fec754_12
                    lookAllTextColor = R.color.C_FEC754
                }
            }
            val resources = binding.root.resources
            binding.topText.setText(topText)
            binding.lookAll.setTextColor(resources.getColor(lookAllTextColor))
            binding.lookAll.background = resources.getDrawable(lookAllBack)
            binding.rV.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rV.adapter = HomeCityRankItemAdapter(item)
            binding.back.background = resources.getDrawable(backDrawableId)
            binding.lookAll.setOnClickListener {
                RankActivity.start(it.context)
            }
        }
    }
}