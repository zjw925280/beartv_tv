package com.gys.play.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemRankBinding
import com.gys.play.entity.BtVideoInfo

class RankListAdapter(list: ArrayList<BtVideoInfo>) :
    Adapter<BtVideoInfo>(BR.item, R.layout.item_rank, list) {

    var type = 1

    override fun convert(binding: ViewDataBinding, item: BtVideoInfo, position: Int) {
        if (binding is ItemRankBinding) {
            item.position = position
//            val positionBack: Int
//            val positionTextColor: Int
//            when (position) {
//                0 -> {
//                    positionBack = R.mipmap.icon_one
//                    positionTextColor = R.color.C_FFA706
//                }
//                1 -> {
//                    positionBack = R.mipmap.icon_two
//                    positionTextColor = R.color.C_9DBFDA
//                }
//                2 -> {
//                    positionBack = R.mipmap.icon_three
//                    positionTextColor = R.color.C_E2BD83
//                }
//                else -> {
//                    positionBack = 0
//                    positionTextColor = R.color.C_9a9a9a
//                }
//            }
//            if (positionBack == 0) {
//                binding.tvRankNoBack.visibility = View.GONE
//            } else {
//                binding.tvRankNoBack.visibility = View.VISIBLE
//                binding.tvRankNoBack.setImageResource(positionBack)
//            }
//            binding.tvRankNo.text = "${position + 1} "
//            binding.tvRankNo.setTextColor(binding.root.resources.getColor(positionTextColor))
//            binding.tvRankNum.text = " " + ConversionUtil.LongToWString(item.total_views) + "k"
            when (type) {
                1 -> {
                    binding.tpye3.visibility = View.GONE
                }
                2 -> {
                    binding.tpye3.visibility = View.GONE
                    binding.imageRightTop.setImageResource(R.mipmap.rank_type2)
                }
                3 -> {
                    binding.tpye.visibility = View.GONE
                    binding.imageRightTop.setImageResource(R.mipmap.rank_type3)
                }
            }
        }
    }
}