package com.gys.play.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.util.list.ListUtil
import com.gys.play.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener

class PagingListView : FrameLayout, OnRefreshListener,
    OnLoadMoreListener {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr, 0)

    constructor(
        context: Context, attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    val sRefreshLayout: SmartRefreshLayout
    val listRv: RecyclerView
    var onRefreshListener: OnRefreshListener? = null
    var onLoadMoreListener: OnLoadMoreListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_footer_list, this, true)
        sRefreshLayout = findViewById(R.id.sRefreshLayout)
        listRv = findViewById(R.id.listRv)

        sRefreshLayout.setOnRefreshListener(this)
        sRefreshLayout.setOnLoadMoreListener(this)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        onRefreshListener?.onRefresh(refreshLayout)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        onLoadMoreListener?.onLoadMore(refreshLayout)
    }

    fun onGetDataEnd(isMore: Boolean) {
        if (isMore) {
            sRefreshLayout.finishLoadMore()
        } else {
            sRefreshLayout.finishRefresh()
        }
    }

    fun setListV() {
        ListUtil.initRecyclerView(context, listRv, 0, 0)
    }

    fun setListVG(gridCount: Int) {
        ListUtil.initRecyclerView(context, listRv, 1, gridCount)
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        listRv.adapter = adapter
    }

    fun setListPaddingTop(top: Int) {
        listRv.setPadding(0, top, 0, 0)
    }
}