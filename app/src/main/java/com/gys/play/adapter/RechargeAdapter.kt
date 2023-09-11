package com.gys.play.adapter

import android.graphics.Color
import androidx.databinding.ViewDataBinding
import com.coorchice.library.SuperTextView
import com.mybase.libb.ext.getColor
import com.mybase.libb.ext.notEmpty
import com.mybase.libb.ext.showGONE
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemRechargeBinding
import com.gys.play.entity.CommodityInfo

class RechargeAdapter(list: MutableList<CommodityInfo> = mutableListOf(), type: Int = 1) :
    Adapter<CommodityInfo>(BR.item, R.layout.item_recharge, list) {

    var listener: OnSetSelClickListener? = null
    var type = 1   //1:首充数据   2：非首充数据

    init {
        this.type = type
    }

    override fun convert(binding: ViewDataBinding, item: CommodityInfo, position: Int) {
        super.convert(binding, item, position)
        binding as ItemRechargeBinding

        var topText = item.googsInfo.getTopText()

        if (item.googsInfo.give_coins > 0) {
            binding.text2.text = "+${item.googsInfo.give_coins}"
        } else {
            binding.text2.text = " "
        }


        if (!topText.notEmpty()) {
            topText = item.googsInfo.vip_activity_tag
        }
        binding.top.showGONE(topText.notEmpty())
        if (topText.notEmpty()) {
            binding.top.text = topText
        }
        binding.layout.isClickable = true
        binding.layout.setLayoutBackground(Color.parseColor("#FEF8FA"))
        if (item.isSelect) {
            binding.layout.setStrokeColor(Color.parseColor("#EA1E5C"))
//            binding.bottom.setTextColor(R.color.white.getColor())
//            binding.bottom.setBackgroundResource(R.drawable.shape_rect_ea1e5c_10)
        } else {
            binding.layout.setStrokeColor(R.color.transparent.getColor())
//            binding.bottom.setTextColor(R.color.C_06133C.getColor())
//            binding.bottom.setBackgroundResource(R.color.transparent.getColor())
        }

        binding.top.shaderMode = SuperTextView.ShaderMode.LEFT_TO_RIGHT
        binding.top.isShaderEnable = true
        binding.layout.setOnClickListener {
            listener?.let {
                it.onSelClickListener(position, type)
            }
        }
    }

    interface OnSetSelClickListener {
        fun onSelClickListener(position: Int, type: Int)
    }
}