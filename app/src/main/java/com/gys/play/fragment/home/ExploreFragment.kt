package com.gys.play.fragment.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.ui.base.BindingFragment
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.gys.play.BR
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentExploreBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.entity.ClassifyLabel
import com.gys.play.ui.search.SearchActivity
import com.gys.play.util.LiveDataUtil
import com.gys.play.viewmodel.FrequencyDivisionModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import post

class ExploreFragment : BindingFragment<FrequencyDivisionModel, FragmentExploreBinding>() {

    var channel_type = 2
    var page = 1
    var list = ArrayList<BtVideoInfo>()
    var categoryListAdapter = Adapter(BR.item, R.layout.item_item3, list)

    override fun getLayoutId() = R.layout.fragment_explore


    override fun initView() {
        getClassLabels()
        binding.rvCategoryList.layoutManager = GridLayoutManager(activity, 3)
        binding.rvCategoryList.adapter = categoryListAdapter
        observeUi(this)
    }

    override fun initData() {
        setClickListener()
        getClassLabels()
        binding.sRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                getClassifytList(false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                getClassifytList(true)
            }
        })
    }

    private fun getClassLabels() {
        post {
            val data = Quest.api.getCategoryIndex(Quest.getHead(Config.APP_BOOK_CATEGORY_INDEX) {
                add("channel_type", "$channel_type")
            })

            if (data.category.isNotEmpty()) {
                data.category.add(0, ClassifyLabel(0, getString(R.string.type_all)))
            }
            if (data.area.isNotEmpty()) {
                data.area.add(0, ClassifyLabel(0, getString(R.string.type_all)))
            }
            if (data.year.isNotEmpty()) {
                data.year.add(0, ClassifyLabel(0, getString(R.string.type_all)))
            }
            if (data.pay_type.isNotEmpty()) {
                data.pay_type.add(0, ClassifyLabel(0, getString(R.string.type_all)))
            }
            initItemClick(binding.rv1, data.category)
            initItemClick(binding.rv2, data.area)
            initItemClick(binding.rv3, data.year)
            initItemClick(binding.rv4, data.pay_type)

            getClassifytList(false)
        }
    }

    private fun initItemClick(rv: RecyclerView, dataList: MutableList<ClassifyLabel>) {
        tagMap[rv] ?: let {
            tagMap.put(rv, Pair(0, dataList[0]))
        }

        rv.linear(RecyclerView.HORIZONTAL).setup {
            addType<ClassifyLabel>(R.layout.item_classify_label)
            onBind {
                getModel<ClassifyLabel>().let { data ->
                    findView<TextView>(R.id.text).run {
                        val selectData = tagMap[rv]
                        text = data.title
                        if (selectData?.second == data) {
                            setBackgroundResource(R.drawable.shape_rect_fff6f9fb_5_12)
                        } else {
                            setBackgroundResource(R.color.transparent)

                        }
                        setOnClickListener {
                            tagMap[rv]?.let {
                                notifyItemChanged(it.first)
                                if (it.second == data) {
                                    return@setOnClickListener
                                }
                            }
                            tagMap.put(rv, Pair(modelPosition, data))
                            notifyItemChanged(modelPosition)
                            getClassifytList(false)
                        }
                    }
                }
            }
        }.models = dataList
    }

    private val tagMap = hashMapOf<View, Pair<Int, ClassifyLabel>>()

    private fun getClassifytList(isLoadMore: Boolean) {
        if (isLoadMore) {
            page++
        } else {
            page = 1
        }
        binding.sRefreshLayout.setNoMoreData(false)
        post {
            val data = Quest.api.getCategoryList(Quest.getHead(Config.APP_BOOK_CATEGORY_LIST) {
//                add("channel_type", "$channel_type")
                add("cid", "${tagMap.get(binding.rv1)?.second?.id ?: 0}")
                add("area", "${tagMap.get(binding.rv2)?.second?.id ?: 0}")
                add("year", "${tagMap.get(binding.rv3)?.second?.id ?: 0}")
                add("pay_type", "${tagMap.get(binding.rv4)?.second?.id ?: 0}")
//                add("sort_type", "0")
                add("page", "$page")
                add("limit", "10")
            })
            if (isLoadMore) {
                if (data.items.size <= 0) {
                    binding.sRefreshLayout.finishLoadMoreWithNoMoreData()
                }
                categoryListAdapter.addDatas(data.items, data.items.size)
            } else {
                categoryListAdapter.setList(data.items)
            }
            binding.sRefreshLayout.finishRefresh()
            binding.sRefreshLayout.finishLoadMore()
        }
    }

    private fun setClickListener() {
        binding.searchLayout.setOnClickListener {
            MyApplication.getAnalytics().setClassify("搜索点击量")
            activity?.let {
                startActivity(Intent(it, SearchActivity::class.java))
            }
        }
    }

    private fun observeUi(owner: LifecycleOwner) {
        LiveDataUtil.mainHomeGoTopClick.observe(owner) { binding.rvCategoryList.scrollToPosition(0) }
        binding.rvCategoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                LiveDataUtil.mainHomeGoTop.value = recyclerView.canScrollVertically(-1)
            }
        })
    }
}