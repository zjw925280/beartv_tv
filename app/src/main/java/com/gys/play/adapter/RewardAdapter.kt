package com.gys.play.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.BR
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemRewardBinding
import com.gys.play.entity.RewardListInfo

class RewardAdapter(context: Context, list: MutableList<RewardListInfo> = mutableListOf()) :
    Adapter<RewardListInfo>(BR.item, R.layout.item_reward, list) {

    var context: Context? = null
    var index = 0
    var listener: ((gift_id: Int, coins: Int, position: Int) -> Unit)? = null

    init {
        this.context = context
    }

    override fun convert(binding: ViewDataBinding, item: RewardListInfo, position: Int) {
        super.convert(binding, item, position)
        (binding as ItemRewardBinding).apply {
            if (position == index) {
                listener?.invoke(item.id, item.coins, position)
                rlBg.setBackgroundResource(R.drawable.shape_rect_f6f8fe_stroke_ffca20_5f)
            } else {
                rlBg.setBackgroundResource(R.drawable.shape_rect_f6f8fe_5)
            }
            rlBg.setOnClickListener {
                index = position
                listener?.invoke(item.id, item.coins, position)
                notifyDataSetChanged()
            }
        }
    }
}