package com.gys.play.fragment.book_city

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gys.play.Config
import com.gys.play.adapter.CategoryAdapter
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentListBinding
import com.gys.play.entity.BtVideoInfo
import com.mybase.libb.ui.vb.BaseVbFragment
import post

class SearchHotFragment :
    BaseVbFragment<FragmentListBinding>() {

    private var adapterDataList = ArrayList<BtVideoInfo>()
    private var categoryAdapter: CategoryAdapter? = null

    private fun initData() {
        adapterDataList.clear()

        post {
            val data =
                Quest.api.appHomeChannelConfig(Quest.getHead(Config.APP_HOME_CHANNEL_CONFIG) {
                    add("channel_id", "5")//频道ID：1-新人必读 2-男生 3-女生 4-Home 5-搜索 6-Search, 7-劲爆系列
                })
            val items = data.items
            if (items != null && items.size > 0) {
                for (item in items) {
                    adapterDataList.add(item.getNovelInfo())
                }
            }
            categoryAdapter?.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBind.listRv.layoutManager = linearLayoutManager
        categoryAdapter = CategoryAdapter(adapterDataList, requireActivity())
        mBind.listRv.adapter = categoryAdapter

        initData()
    }

}