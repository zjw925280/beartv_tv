package com.gys.play.fragment.book_city

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gys.play.Config
import com.gys.play.MainActivity
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.coroutines.Quest
import post
import com.gys.play.databinding.FragmentHomeItem2Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.util.LiveDataUtil
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.setSize
import com.mybase.libb.ui.vb.BaseVbFragment

class HomeNew2Fragment :
    BaseVbFragment<FragmentHomeItem2Binding>() {

    //    var bannerDataList = ArrayList<HomeBannerInfo>()
    private val headerDataList = mutableListOf<BtVideoInfo>()
    private val headerBottomDataList = mutableListOf<BtVideoInfo>()
    var adapterDataList = ArrayList<BtVideoInfo>()
    var novelStyleAdapter: NovelStyleAdapter? = null
    var page = 1
    val type by argument(1)

    companion object {
        fun newInStane(type: Int) = HomeNew2Fragment().setBundle {
            it["type"] = type
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBind.goTop.setOnClickListener {
            it.visibility = View.GONE
            mBind.rvRecommend.scrollToPosition(0)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBind.rvRecommend.layoutManager = linearLayoutManager

        novelStyleAdapter = NovelStyleAdapter(adapterDataList, requireActivity())
        mBind.rvRecommend.adapter = novelStyleAdapter
//        novelStyleAdapter?.bannerListener = bannerListener
        getRecommendData()
        mBind.sRefreshLayout.setOnRefreshListener {
            headerDataList.clear()
            adapterDataList.clear()
            page = 1
            getRecommendData()
        }
        mBind.sRefreshLayout.setOnLoadMoreListener {
            page++
            loadMore()
        }
        mBind.rvRecommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                LiveDataUtil.mainHomeGoTop.value = recyclerView.canScrollVertically(-1)
            }
        })
        mBind.rvRecommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.getLayoutManager()
                    if (layoutManager is LinearLayoutManager) {
                        val mFirstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                        if (mFirstVisiblePosition > 3) {
                            mBind.goTop.visibility = View.VISIBLE
                        } else {
                            mBind.goTop.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    val listener = object : BtVideoInfo.Listener {
        override fun select(type: Int) {
            if (shortTvType == type) {
                return
            }
            (mActivity as? MainActivity)?.showLoading()
            shortTvType = type
            page = 1
            loadMore()
        }
    }

    private fun loadMore() {
        post {
            val data =
                Quest.api.getAppShortTvListBy(Quest.getPageHead(Config.APP_SHORTTV_LISTBY, page) {
                    add("sort_type", "$shortTvType")//排序类型：4-播放量 5-评分
                    add("page", "$page")
                    add("limit", "10")
                })
            if (data.items != null && data.items.size > 0) {
                if (page == 1) {
                    (activity as? MainActivity)?.closeLoading()
                    adapterDataSetChanged()
                } else {
                    mBind.sRefreshLayout.finishLoadMore()
                }
                novelStyleAdapter?.addData(data.items)
            } else {
                mBind.sRefreshLayout.finishRefreshWithNoMoreData()
            }
        }
    }

    private fun getRecommendData() {
        post {
            val data =
                Quest.api.appHomeChannelConfig(Quest.getHead(Config.APP_HOME_CHANNEL_CONFIG) {
                    add("channel_id", "$type")
                })
            val items = data.items
            if (items != null && items.size > 0) {
                for (item in items) {
                    headerDataList.add(item.getNovelInfo())
                }
                //加个播放量评分标题
                val novelInfo = BtVideoInfo(datatype = Config.HOME_SHORT_TV_TYPE)
                novelInfo.listener = listener
                novelInfo.shortTvType = shortTvType
                headerDataList.add(novelInfo)
                adapterDataSetChanged()
                loadMore()
            }
        }
    }

    var shortTvType = 4

    private fun adapterDataSetChanged() {
        adapterDataList.clear()
        adapterDataList.addAll(headerDataList)
        novelStyleAdapter?.notifyDataSetChanged()
        mBind.sRefreshLayout.finishRefresh()
    }

    override fun observeUi(owner: LifecycleOwner) {
        LiveDataUtil.categoryHeadHeight.observe(owner) {
            mBind.head.setSize(height = it)
        }

        LiveDataUtil.mainHomeGoTopClick.observe(owner) { mBind.rvRecommend.scrollToPosition(0) }
    }

}