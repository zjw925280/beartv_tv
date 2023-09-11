package com.gys.play.fragment.book_city.style

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle5Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.ui.rank.RankActivity
import com.gys.play.util.onClick

object BtVideoStyle5 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_5
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_5,
            getType(),
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                holder?.let {
                    val binding: ItemNovelStyle5Binding = it.getViewDataBinding()
                    if (item == null) {
                        return@let
                    }
                    binding.title.text = item.title

                    val click = View.OnClickListener {
                        binding.text1.isSelected = false
                        binding.text2.isSelected = false
                        binding.text3.isSelected = false
                        when (it) {
                            binding.text1 -> {
                                binding.listRv.models = item.rank_list?.must_read
                                binding.text1.isSelected = true
                                item.page = 1
                            }
                            binding.text2 -> {
                                binding.listRv.models = item.rank_list?.over
                                binding.text2.isSelected = true
                                item.page = 2
                            }
                            binding.text3 -> {
                                binding.listRv.models = item.rank_list?.newest
                                binding.text3.isSelected = true
                                item.page = 3
                            }
                        }
                    }
                    binding.text1.setOnClickListener(click)
                    binding.text2.setOnClickListener(click)
                    binding.text3.setOnClickListener(click)

                    binding.listRv.models = when (item.page) {
                        1 -> item.rank_list?.newest
                        2 -> item.rank_list?.over
                        else -> item.rank_list?.newest
                    }

//                    binding.btnViewAll.visibility =
//                        if (item.is_more == 1) View.VISIBLE else View.GONE
                    binding.btnViewAll.onClick = {
                        RankActivity.start(activity)
                    }
                }
            }
        }
    }
}