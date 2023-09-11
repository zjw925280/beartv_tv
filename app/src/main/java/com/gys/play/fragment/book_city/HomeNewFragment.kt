package com.gys.play.fragment.book_city

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gys.play.Config
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentHomeNewBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.entity.MoreListener
import com.gys.play.util.LiveDataUtil
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.setSize
import com.mybase.libb.ui.vb.BaseVbFragment
import post

class HomeNewFragment : BaseVbFragment<FragmentHomeNewBinding>() {

    //    var bannerDataList = ArrayList<HomeBannerInfo>()
    private val headerDataList = mutableListOf<BtVideoInfo>()
    private val headerBottomDataList = mutableListOf<BtVideoInfo>()
    var adapterDataList = ArrayList<BtVideoInfo>()
    var novelStyleAdapter: NovelStyleAdapter? = null
    var page = 1
    val type by argument(1)

    companion object {
        fun newInStane(type: Int) = HomeNewFragment().setBundle {
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

    private fun loadMore() {
        post {
            val data =
                Quest.api.getAppShortTvListBy(Quest.getPageHead(Config.APP_SHORTTV_LISTBY, page) {
                    add("sort_type", "4")//排序类型：4-播放量 5-评分
                    add("page", "$page")
                    add("limit", "10")
                })
            if (data.items != null && data.items.size > 0) {
                if (page == 1) {
                    novelStyleAdapter?.addData(data.items)
                    headerBottomDataList.clear()
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
            headerDataList.clear()
            adapterDataList.clear()
            val items = data.items
            if (items != null && items.size > 0) {
                for (item in items) {
                    val newItem = item.getNovelInfo()
                    if (newItem.datatype == 14) {
                        newItem.datatype = 5
                    }
                    if (newItem.datatype == 5) {
                        newItem.rank_list?.rank()
                    }

                    newItem.moreListener = moreListener
                    headerDataList.add(newItem)
                }
                adapterDataSetChanged()
            }
        }
    }

    private val moreListener = object : MoreListener {
        override fun more(data: BtVideoInfo, position: Int, type: Int) {
            ++data.page
            post {
                val newData =
                    Quest.api.getNovelMoreListSSS(Quest.getHead(Config.APP_HOME_RECOMMEND_MORE) {
                        add("recommend_id", "${data.id}")
                        add("page", "${data.page}")
                        add("limit", "3")
                    })
                if (newData.items.isEmpty()) {
                    if (data.page == 1) {
                        //加个判断排除死循环
                        return@post
                    }
                    data.page = 0
                    more(data, position, type)
                } else {
                    data.list = newData.items
                    novelStyleAdapter?.notifyItemChanged(position)
                }
            }
        }
    }

    private fun adapterDataSetChanged() {
        adapterDataList.clear()
        adapterDataList.addAll(headerDataList)
        //加个标题..
//        adapterDataList.add(NovelInfo(datatype = Config.HOME_SAVE_SHORTAGE_BOOK))
//        adapterDataList.addAll(headerBottomDataList)
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