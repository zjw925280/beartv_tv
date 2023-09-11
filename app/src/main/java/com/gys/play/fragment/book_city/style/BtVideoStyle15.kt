package com.gys.play.fragment.book_city.style

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.HomeCityRankListAdapter
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle15Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.ui.rank.RankActivity
import com.gys.play.util.ImageLoaderUtils

object BtVideoStyle15 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_15
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_15,
            getType(),
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo, position: Int) {
                holder?.let { h ->
                    val binding: ItemNovelStyle15Binding = h.getViewDataBinding()

                    binding.title.text = item.title
                    if (item.icon.isEmpty()) {
                        binding.icon.visibility = View.GONE
                    } else {
                        binding.icon.visibility = View.VISIBLE
                        ImageLoaderUtils.loadImage(binding.icon, item.icon)
                    }
                    binding.listRv.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

                    val rankList = item.rank_list
                    if (rankList != null) {
                        val list = mutableListOf(
                            rankList.must_read,
                            rankList.over,
                            rankList.newest
                        )

                        binding.listRv.adapter =
                            HomeCityRankListAdapter(list, item.channel_id)
                        binding.listRv.addOnScrollListener(
                            object : RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(
                                    recyclerView: RecyclerView,
                                    newState: Int
                                ) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                                        && isSlideToBottom(recyclerView)
                                    ) {
                                        goRankActivity(activity, recyclerView, item.channel_id)
                                    } else {
                                        startRank = false
                                    }
                                }
                            })
                    }
                }
            }
        }
    }

    private fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        recyclerView?.run {
            return (recyclerView.computeHorizontalScrollExtent() + recyclerView.computeHorizontalScrollOffset() >= recyclerView.computeHorizontalScrollRange())
        }
        return false
    }

    private var startRank = false
    private fun goRankActivity(
        ctx: Activity,
        recyclerView: RecyclerView,
        channelId: Int
    ) {
        if (!startRank) {
            RankActivity.start(ctx)
            recyclerView.smoothScrollBy(-50, 0)
            startRank = true
        }
    }

}