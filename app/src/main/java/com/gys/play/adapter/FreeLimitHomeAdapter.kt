package com.gys.play.adapter

import android.app.Activity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemFreeLimitBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.BR

class FreeLimitHomeAdapter(list: ArrayList<BtVideoInfo>, var ctx: Activity) :
    Adapter<BtVideoInfo>(BR.item, R.layout.item_free_limit, list) {

    override fun convert(binding: ViewDataBinding, item: BtVideoInfo, position: Int) {
        super.convert(binding, item, position)
        if (binding is ItemFreeLimitBinding){
            if (item.status_txt.equals("")) {
                binding.ivTip.visibility = View.GONE
            } else {
                binding.ivTip.visibility = View.VISIBLE
            }
        }
    }
}