package com.gys.play.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.liba.network.protocol.BaseListInfo
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.models
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.ActivityHistorylistBinding
import com.gys.play.databinding.ItemHistoryBinding
import com.gys.play.entity.HistryInfo
import com.gys.play.entity.SimpleReturn
import com.gys.play.ui.video.VideoListActivity
import com.gys.play.util.onClick
import com.gys.play.viewmodel.NovelBasePresent
import com.jccppp.start.argument
import com.mybase.libb.ext.notEmpty
import post

class HistoryListActivity :
    NovelBaseActivity<NovelBasePresent<HistoryListActivity>, ActivityHistorylistBinding>(R.layout.activity_historylist) {
    private var editor = false

    private val isHistory by argument(true)

    companion object {
        fun start(context: Context?, isHistory: Boolean = true) {
            context?.run {
                val intent = Intent(context, HistoryListActivity::class.java)
                intent.putExtra("isHistory", isHistory)
                startActivity(intent)
            }
        }
    }

    private var loadType = ""

    private var weekPage = 1

    private val list get() = binding.prv.rv.models

    private fun goActivity(histryInfo: HistryInfo) {
        if (isHistory) {
            VideoListActivity.launch(mActivity,histryInfo.tv_id,histryInfo.id)
        }
    }


    private suspend fun delHttpData(ids: String): SimpleReturn {
        if (isHistory) {
            return Quest.api.delPlayLog(Quest.getHead(Config.APP_SHORTTV_DELPLAYLOG) {
                add("tv_ids", ids)
            })
        }
        return Quest.api.delWowSubscribe(Quest.getHead(Config.APP_WOW_DELSUBSCRIBE) {
            add("sub_ids", ids)
        })
    }

    private suspend fun getHttpData(type: String, page: Int): BaseListInfo<HistryInfo> {
        if (isHistory) {
            return Quest.api.getShorTvLog(Quest.getHead(Config.APP_SHORTTV_TVPLAYLOG) {
                add("type", type)
                add("page", "$page")
                add("limit", "20")
            })
        }
        return Quest.api.getWowSubscribeList(Quest.getHead(Config.APP_WOW_SUBSCRIBELIST) {
            add("type", type)
            add("page", "$page")
            add("limit", "20")
        })

    }


    override fun initView() {
        binding.itemTopBack.onClick = { finish() }
        binding.itemTopTitle.text = if (isHistory) "觀看歷史" else "我的訂閱"

        binding.prv.config {
            if (it == 1) {
                weekPage = 1
                loadType = "week"
            }
            val list = arrayListOf<Any>()
            val week = getHttpData(loadType, if (loadType == "earlier") weekPage else it)
            if (it == 1) {
                if (week.items.notEmpty()) {
                    list.add("一週內")
                }
            }

            if (week.items.notEmpty())
                list.addAll(week.items)

            if (loadType == "week" && week.items.size < 20) {
                loadType = "earlier"
                val httpData = getHttpData(loadType, weekPage)
                if (httpData.items.notEmpty()) {
                    list.add("更早")
                    list.addAll(httpData.items)
                }
            }

            if (loadType == "earlier") {
                weekPage++
            }

            list
        }

        (binding.prv.rv.adapter as? BindingAdapter)?.let {
            it.addType<String>(R.layout.item_history_title)
            it.onBind {
                if (itemViewType == R.layout.item_history_title) {
                    findView<TextView>(R.id.tvTitle).text = getModel()
                } else {
                    val histryInfo = getModel<HistryInfo>()
                    val binding = getBinding<ItemHistoryBinding>()
//                binding.item = histryInfo
                    binding.select.run {
                        visibility = if (editor) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        if (histryInfo.isSelect) {
                            setImageResource(R.mipmap.icon_select_s)
                        } else {
                            setImageResource(R.mipmap.icon_select_n)
                        }
                    }
                    binding.root.setOnClickListener { v ->
                        if (editor) {
                            histryInfo.isSelect = !histryInfo.isSelect
                            it.notifyItemChanged(modelPosition)
                        } else {
                            //跳转对应的页面
                            goActivity(histryInfo)
                        }
                    }
                }
            }
        }

        binding.prv.onRestart()
    }

    override fun initData() {
    }


    override fun loadData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
        binding.itemTopRight.setOnClickListener {
            changeEdit(!editor)
        }
        binding.all.setOnClickListener {
            if (list.isNullOrEmpty()) {
                return@setOnClickListener
            }
            for (item in list!!) {
                if (item is HistryInfo)
                    item.isSelect = true
            }
            binding.prv.rv.adapter?.notifyDataSetChanged()

        }
        binding.del.setOnClickListener {
            if (list.isNullOrEmpty()) {
                return@setOnClickListener
            }
            var ids = ""
            val delList = mutableListOf<HistryInfo>()
            for (item in list!!) {
                if (item is HistryInfo && item.isSelect) {
                    if (isHistory) {
                        ids += "${item.tv_id},"
                    } else {
                        ids += "${item.id},"
                    }

                    delList.add(item)
                }
            }
            if (ids.isEmpty()) {
                return@setOnClickListener
            }
            post {
                val data = delHttpData(ids)
                changeEdit(false)
                binding.prv.onRestart()
            }
        }
    }

    private fun changeEdit(edit: Boolean) {
        if (list.isNullOrEmpty()) {
            editor = false
            binding.itemTopRight.text = "編輯"
            binding.bottomLayout.visibility = View.GONE
            return
        }

        editor = edit
        if (editor) {
            binding.itemTopRight.text = "取消"
            binding.bottomLayout.visibility = View.VISIBLE
        } else {
            binding.itemTopRight.text = "編輯"
            binding.bottomLayout.visibility = View.GONE
        }
        binding.prv.rv.adapter?.notifyDataSetChanged()

        binding.prv.setEnableRefresh(!edit)
        binding.prv.setEnableLoadMore(!edit)
    }
}
