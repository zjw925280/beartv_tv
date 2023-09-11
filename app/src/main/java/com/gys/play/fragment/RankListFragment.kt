package com.gys.play.fragment

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.ui.base.BindingFragment
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.RankListAdapter
import com.gys.play.coroutines.Quest
import post
import com.gys.play.databinding.FragmentRecommendBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.viewmodel.MoreRankListModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

class RankListFragment :
    BindingFragment<MoreRankListModel, FragmentRecommendBinding>() {

    var sort_type = 1
    var list = ArrayList<BtVideoInfo>()
    var adapter: RankListAdapter? = null

    companion object {
        fun newInstance(type: Int): RankListFragment {
            val fragment = RankListFragment()
            fragment.sort_type = type
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_recommend

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvRecommend.layoutManager = linearLayoutManager
        adapter = RankListAdapter(list)
        adapter?.type = sort_type
        binding.rvRecommend.adapter = adapter




        binding.sRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                loadMore()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page++
                loadMore()
            }
        })
    }

    override fun initData() {
        loadMore()
    }


    var page = 1

    private fun loadMore() {
        Log.e(TAG, "loadMore: sort_type $sort_type page $page")
        post {
            val data =
                Quest.api.getRank(Quest.getHead(Config.APP_RANK) {
                    add("sort_type", "$sort_type")
                    add("page", "$page")
                    add("limit", "20")
                })
            if (page == 1) {
                adapter?.clear()
            } else if (data.items.isNullOrEmpty()) {
                binding.sRefreshLayout.finishRefreshWithNoMoreData()
            }
            adapter?.addDatas(data.items)
            binding.sRefreshLayout.finishLoadMore()
            binding.sRefreshLayout.finishRefresh()
        }
    }
}