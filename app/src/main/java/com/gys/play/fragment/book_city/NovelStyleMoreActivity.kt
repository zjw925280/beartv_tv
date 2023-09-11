package com.gys.play.fragment.book_city

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gys.play.BR
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.ActivityNovelStyleMoreBinding
import com.gys.play.entity.BtVideoInfo
import com.mybase.libb.ui.vb.BaseVbActivity
import post
import kotlin.properties.Delegates

class NovelStyleMoreActivity : BaseVbActivity<ActivityNovelStyleMoreBinding>() {

    private var id by Delegates.notNull<Int>()
    private var page = 1
    private val limit = 10
    var dataList = ArrayList<BtVideoInfo>()
    var adapter = Adapter(BR.item, R.layout.item_home_for_your_list, dataList)

    companion object {
        fun launch(activity: FragmentActivity, id: Int, name: String = "") {
            val intent = Intent(activity, NovelStyleMoreActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            activity.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        id = intent.getIntExtra("id", 0)
        val name = intent.getStringExtra("name")
        mBind.btnBack.setOnClickListener { finish() }
        mBind.title.text = name

        val linearLayoutManager = LinearLayoutManager(mActivity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mBind.listRv.layoutManager = linearLayoutManager

        mBind.listRv.adapter = adapter
        mBind.sRefreshLayout.setOnRefreshListener {
            getData(1)
        }
        mBind.sRefreshLayout.setOnLoadMoreListener {
            getData(2)
        }
        getData(0)
    }

    private fun getData(type: Int) {
        if (type == 2) {
            page++
        } else {
            page = 1
        }
        post {
            val data = Quest.api.getNovelMoreListSSS(Quest.getHead(Config.APP_HOME_RECOMMEND_MORE) {
                add("recommend_id", "$id")
                add("page", "$page")
                add("limit", "$limit")
            })
            val items = data.items
            if (type != 2) {
                adapter.clear()
            }
            if (items.isNotEmpty()) {
                adapter.addDatas(items)
            }
            mBind.sRefreshLayout.finishRefresh()
            mBind.sRefreshLayout.finishLoadMore()
        }
    }
}