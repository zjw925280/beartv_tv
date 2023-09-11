package com.gys.play.fragment.book_city.style

import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle3NewBinding
import com.gys.play.entity.BtVideoInfo

object BtVideoStyle3 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_3
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_3_new,
            Config.HOME_NOVEL_STYLE_3,
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                holder?.let {
                    val binding: ItemNovelStyle3NewBinding = it.getViewDataBinding()
                    binding.title.text = item?.title
                    binding.rvGuess.divider {
                        setDrawable(R.drawable.divider_size_8)
                        orientation = DividerOrientation.GRID
                    }.models = item?.list
                }
            }
        }
    }
}