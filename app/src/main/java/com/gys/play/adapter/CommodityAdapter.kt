package com.gys.play.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import com.coorchice.library.SuperTextView
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemCommodityBinding
import com.gys.play.entity.CommodityInfo
import com.mybase.libb.ext.getColor

class CommodityAdapter(list: MutableList<CommodityInfo> = mutableListOf()) :
    Adapter<CommodityInfo>(BR.item, R.layout.item_commodity, list) {

    override fun convert(binding: ViewDataBinding, item: CommodityInfo, position: Int) {
        super.convert(binding, item, position)
        binding as ItemCommodityBinding
        binding.layout.isClickable = true
        binding.layout.setLayoutBackground(Color.parseColor("#FEF8FA"))
        binding.layout.isClickable = false
        if (item.isSelect) {
            binding.layout.setStrokeColor(Color.parseColor("#EA1E5C"))
            binding.bottom.setTextColor(R.color.white.getColor())
            binding.bottom.setBackgroundResource(R.drawable.shape_rect_ea1e5c_10)
        } else {
            binding.layout.setStrokeColor(R.color.transparent.getColor())
            binding.bottom.setTextColor(R.color.C_06133C.getColor())
            binding.bottom.setBackgroundResource(R.color.transparent.getColor())
        }
        if (item.googsInfo.vip_activity_tag.isNullOrEmpty()) {
            binding.top.visibility = View.GONE
        } else {
            binding.top.visibility = View.VISIBLE
            binding.top.text = item.googsInfo.vip_activity_tag
        }

        binding.time.text = item.googsInfo.title
        binding.text1.text = "$" + item.googsInfo.vip_ori_price
        binding.text2.text = "$" + item.googsInfo.price
        binding.text1.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        binding.bottom.text = item.googsInfo.vip_activity_desc

        binding.top.shaderMode = SuperTextView.ShaderMode.LEFT_TO_RIGHT
        binding.top.isShaderEnable = true
    }
}