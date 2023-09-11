package com.gys.play.fragment.home

import androidx.recyclerview.widget.GridLayoutManager
import com.android.liba.ui.base.BindingFragment
import com.gys.play.R
import com.gys.play.adapter.HomeRankListAdapter
import com.gys.play.databinding.FragmentHomeRankListBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.viewmodel.HomeRankListModel

class HomeRankListFragment :
    BindingFragment<HomeRankListModel, FragmentHomeRankListBinding>() {

    var type = 0
    var list = ArrayList<BtVideoInfo>()
    var sort_type = 1
    var adapter: HomeRankListAdapter? = null

    companion object {
        fun newInstance(type: Int): HomeRankListFragment {
            var fragment = HomeRankListFragment()
            fragment.type = type
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_home_rank_list

    override fun initView() {
        var gridLayoutManager = GridLayoutManager(activity, 2)
        binding.rvHomeRank.layoutManager = gridLayoutManager
        when (type) {
            0 -> sort_type = 1
            1 -> sort_type = 5
            2 -> sort_type = 6
        }
    }

    override fun initData() {
        adapter = HomeRankListAdapter(list)
        binding.rvHomeRank.adapter = adapter
//        presenter.getBookRank(
//            sort_type,
//            0
//        ) { activity: HomeRankListFragment, data: BaseListInfo<NovelInfo> ->
//          adapter?.setList(data.items)
//        }
    }
}