package com.gys.play.adapter

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.BR
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemBatchPayBinding
import com.gys.play.entity.BuyPercent

class BatchPayAdapter(context: Context?, list: MutableList<BuyPercent> = mutableListOf()) :
    Adapter<BuyPercent>(BR.item, R.layout.item_batch_pay, list) {

    var context: Context? = null
    var index = -1
    var childClickListener: ((chapter: Int, coins: String, position: Int) -> Unit)? = null

    init {
        this.context = context
    }

    override fun convert(binding: ViewDataBinding, item: BuyPercent, position: Int) {
        super.convert(binding, item, position)
        binding as ItemBatchPayBinding

        binding.tvConis.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        if (item.chapter_num > item.can_buy_num) {
            binding.rlPay.visibility = View.GONE
        } else {
            binding.rlPay.visibility = View.VISIBLE
        }
        binding.tvChapterNum.text =
            context?.getString(R.string.next_chapter_num, item.chapter_num.toString())

        if (index == position) {
            binding.imgSel.setImageResource(R.mipmap.icon_gou_n)
        } else {
            binding.imgSel.setImageResource(R.mipmap.icon_gou)
        }
        binding.root.setOnClickListener {
            index = position
            childClickListener?.invoke(
                item.chapter_num,
                binding.tvSaleConis.text.toString(),
                position
            )
            notifyDataSetChanged()
        }
    }
}