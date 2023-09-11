package com.mybase.libb.ui.rv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.PageRefreshLayout
import com.mybase.libb.R


abstract class BaseRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val _page: PageRefreshLayout
    private val _rv: RecyclerView

    companion object {
        var LIMIT_SIZE = 20
    }

    open fun getLayoutId() = R.layout.include_lib_brv_refresh_pull

    init {

        LayoutInflater.from(context).inflate(getLayoutId(), this)
        _page = findViewById(R.id.brvPage)
        _rv = findViewById(R.id.brvRV)
        _page.setRetryIds(R.id.retry_state)

        _page.onRefresh {
            load()
        }
    }

    val page get() = _page.index

    val prl: PageRefreshLayout get() = _page

    val rv: RecyclerView get() = _rv

    val limit get() = if (getLimitSize() == 0) LIMIT_SIZE else getLimitSize()

    private var limitSize = 0

    open fun getLimitSize() = limitSize

    open fun setLimitSize(size: Int) {
        limitSize = size
    }

    fun initRv(rv: (RecyclerView) -> Unit) = apply {
        rv(this.rv)
    }

    fun onRefresh(block: PageRefreshLayout.() -> Unit) = apply {
        prl.onRefresh(block)
    }

    fun setData(data: List<Any?>, loadEnd: Boolean) {

        prl.addData(data) {
            !loadEnd  // 判断是否有更多页
        }
    }


    fun showError() {
        prl.showError()
    }

    fun start() {
        prl.showLoading()
    }

    fun setEnableRefresh(enabled: Boolean) = apply {
        prl.setEnableRefresh(enabled)
    }

    fun setEnableLoadMore(enabled: Boolean) = apply {
        prl.setEnableLoadMore(enabled)
    }

    abstract fun load()

}