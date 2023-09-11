package com.gys.play.fragment.book_city.style

import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.MixTypeAdapter
import com.gys.play.entity.BtVideoInfo

object BtVideoStyle {
    fun addAllStyle(activity: FragmentActivity, adapter: MixTypeAdapter<BtVideoInfo>) {
        adapter.apply {
            addItemViewDelegate(BtVideoStyle1.getType(), BtVideoStyle1.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle2.getType(), BtVideoStyle2.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle3.getType(), BtVideoStyle3.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle4.getType(), BtVideoStyle4.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle5.getType(), BtVideoStyle5.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle6.getType(), BtVideoStyle6.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle7.getType(), BtVideoStyle7.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle8.getType(), BtVideoStyle8.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle9.getType(), BtVideoStyle9.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle10.getType(), BtVideoStyle10.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle11.getType(), BtVideoStyle11.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle12.getType(), BtVideoStyle12.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle13.getType(), BtVideoStyle13.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle14.getType(), BtVideoStyle14.getItemViewDelegate(activity))
            addItemViewDelegate(BtVideoStyle15.getType(), BtVideoStyle15.getItemViewDelegate(activity))
        }
    }
}