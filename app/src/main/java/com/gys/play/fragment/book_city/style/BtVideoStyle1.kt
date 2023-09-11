package com.gys.play.fragment.book_city.style

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle1Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.fragment.book_city.NovelStyleMoreActivity
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.onClick

object BtVideoStyle1 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_1
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_1,
            Config.HOME_NOVEL_STYLE_1,
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                holder?.let {
                    val binding: ItemNovelStyle1Binding = it.getViewDataBinding()
                    binding.change.visibility = View.VISIBLE
                    binding.title.text = item?.title
                    if (item == null) {
                        binding.btnViewAll.visibility = View.GONE
                    } else {
                        if (item.icon.isEmpty()) {
                            binding.icon.visibility = View.GONE
                        } else {
                            binding.icon.visibility = View.VISIBLE
                            ImageLoaderUtils.loadImage(binding.icon, item.icon)
                        }

                        if (item.list!!.size > 3) {
                            item.list = item.list!!.subList(0, 3)
                        }
                        binding.listRv.models = item.list
                        binding.btnViewAll.visibility =
                            if (item.is_more == 1) View.VISIBLE else View.GONE
                        binding.btnViewAll.onClick = {
                            NovelStyleMoreActivity.launch(activity, item.id, item.title)
                        }
                    }
                    binding.change.setOnClickListener {
                        item?.moreListener?.more(item, position, 0)
                    }
                }
            }
        }
    }
}