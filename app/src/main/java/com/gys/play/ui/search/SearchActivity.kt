package com.gys.play.ui.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.util.AppUtil
import com.android.liba.util.UIHelper
import com.dm.cartoon.sql.Search
import com.gys.play.BR
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ActivitySearchBinding
import com.gys.play.db.SqlSearchFactory
import com.gys.play.db.SqlSearchViewModel
import com.gys.play.entity.BtVideoInfo
import com.gys.play.fragment.book_city.SearchHotFragment
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.viewmodel.SearchModel

class SearchActivity :
    NovelBaseActivity<SearchModel, ActivitySearchBinding>(R.layout.activity_search) {

    private var otherSearchList = ArrayList<BtVideoInfo>()
    private var searchList = ArrayList<BtVideoInfo>()
    private var page = 1
    private val searchSqlModel: SqlSearchViewModel by viewModels { SqlSearchFactory((application as MyApplication).searchRepository) }
    private var adapter = Adapter(BR.item, R.layout.item_powerful_list, otherSearchList)
    private var searchAdapter = Adapter(BR.item, R.layout.item_powerful_list, searchList)

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvOtherSearch.layoutManager = linearLayoutManager

        val linearLayoutManager1 = LinearLayoutManager(this)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearchContent.layoutManager = linearLayoutManager1

        UIHelper.setEditTextAction(mActivity, binding.etSearch) {
            search()
        }
    }

    private fun setHistory(historyData: MutableList<Search>) {
        binding.historyList.removeAllViews()
        binding.historyList.setChipSpacing(UIHelper.dip2px(activity, 15f))
        for (d in historyData) {
            binding.historyList.addView(createHistorySearchItem(d))
        }
    }

    override fun initData() {
        binding.rvOtherSearch.adapter = adapter
        binding.rvSearchContent.adapter = searchAdapter

        supportFragmentManager.beginTransaction().replace(R.id.searchHotFrame, SearchHotFragment())
            .commitNowAllowingStateLoss()

        binding.sRefreshLayout.setOnRefreshListener {
            page = 1
            binding.sRefreshLayout.setNoMoreData(false)
            refreashData()
        }
        binding.sRefreshLayout.setOnLoadMoreListener {
            page++
            loadMore()
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
        showSoftInput()
        historySearch()
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
                c?.let {
                    if (it.length > 0) {
                        binding.imgSearchDelete.visibility = View.VISIBLE
                    } else {
                        binding.imgSearchDelete.visibility = View.GONE
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun createHistorySearchItem(d: Search): View {
        val view = layoutInflater.inflate(R.layout.item_hot_search_new, null, false)
        val text = view.findViewById<TextView>(R.id.tvSearch)

        if (TextUtils.isEmpty(d.name)) {
            text.text = ""
        } else {
            text.text = d.name
        }
        view.setOnClickListener {
            binding.etSearch.setText(d.name)
            binding.etSearch.setSelection(d.name.length)
            search(d.name)
        }
        return view
    }

    private fun showSoftInput() {
        binding.etSearch.postDelayed({
            binding.etSearch.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
            binding.etSearch.setSelection(binding.etSearch.text.toString().length)
        }, 500)
    }

    private fun historySearch() {
        binding.llHistorySearch.visibility = View.GONE
        searchSqlModel.allData.observe(this) {
            val historyData = mutableListOf<Search>()

            for (index in 0..9) {
                if (it.size > index) historyData.add(it[index])
            }
            setHistory(historyData)

            binding.llHistorySearch.visibility =
                if (historyData.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (binding.llHot.visibility != View.VISIBLE) {
            binding.llHot.visibility = View.VISIBLE
            binding.rlHasContent.visibility = View.GONE
            binding.llOther.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }

    override fun setListener() {
        binding.rlBack.setOnClickListener {
            if (binding.llHot.visibility != View.VISIBLE) {
                binding.llHot.visibility = View.VISIBLE
                binding.rlHasContent.visibility = View.GONE
                binding.llOther.visibility = View.GONE
                return@setOnClickListener
            }
            finish()
        }
        binding.btnSearch.setOnClickListener {
            search()
        }
        binding.imgSearchDelete.setOnClickListener {
            binding.etSearch.setText("")
        }
        binding.imgClear.setOnClickListener {
            searchSqlModel.deletall()
        }
        binding.imgSearchDelete.setOnClickListener {
            binding.etSearch.setText("")
        }
    }

    private fun search(content: String) {
        AppUtil.hideSoftInput(binding.etSearch)
        searchSqlModel.insert(Search(content))
        presenter.getBookSearch(
            content,
            page
        ) { activity: SearchActivity, data: BaseListInfo<BtVideoInfo> ->
            val items = data.items
            if (items != null && items.size > 0) {
                hasSearchContent()
                searchAdapter.setList(data.items)
            } else {
                binding.llHot.visibility = View.GONE
                binding.rlHasContent.visibility = View.GONE
                binding.llOther.visibility = View.VISIBLE
                presenter.getBookRank { activity: SearchActivity, data: BaseListInfo<BtVideoInfo> ->
                    adapter.setList(data.items)
                }
            }
        }
    }

    private fun search() {
        val content: String = binding.etSearch.text.toString()
        if (TextUtils.isEmpty(content)) {
            showToast(getString(R.string.search_content))
        } else {
            search(content)
        }
    }

    private fun refreashData() {
        presenter.getBookSearch(
            binding.etSearch.text.toString(),
            page
        ) { activity: SearchActivity, data: BaseListInfo<BtVideoInfo> ->
            hasSearchContent()
            searchAdapter.setList(data.items)
            binding.sRefreshLayout.finishRefresh()
        }
    }

    private fun loadMore() {
        presenter.getBookSearch(
            binding.etSearch.text.toString(),
            page
        ) { activity: SearchActivity, data: BaseListInfo<BtVideoInfo> ->
            hasSearchContent()
            if (data.items.size <= 0) {
                binding.sRefreshLayout.finishLoadMoreWithNoMoreData()
            }
            searchAdapter.addDatas(data.items, data.items.size)
            binding.sRefreshLayout.finishLoadMore()
        }
    }

    private fun hasSearchContent() {
        binding.rlHasContent.visibility = View.VISIBLE
        binding.llHot.visibility = View.GONE
        binding.llOther.visibility = View.GONE
    }

}