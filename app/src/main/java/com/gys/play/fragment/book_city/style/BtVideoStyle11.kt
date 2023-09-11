package com.gys.play.fragment.book_city.style

import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle11Binding
import com.gys.play.entity.BtVideoInfo

object BtVideoStyle11 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_11
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_11,
            getType(),
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                holder?.let {
                    val binding: ItemNovelStyle11Binding = it.getViewDataBinding()

                    val list = item!!.list
                    if (list!!.size > 0) {
                        val marqueeViewListOf = mutableListOf<String>()
                        for (d in list) {
                            if (!TextUtils.isEmpty(d.title)) {
                                marqueeViewListOf.add(d.title)
                            }
                        }

                        binding.fbsNoticeLayout.visibility = View.VISIBLE
                        binding.fbsMarqueeView.startWithList(marqueeViewListOf)
                        binding.fbsMarqueeView.setOnItemClickListener { position, textView ->
                            list[position].jump(activity)
                        }
                    } else {
                        binding.fbsNoticeLayout.visibility = View.GONE
                    }
                }
            }
        }
    }
}