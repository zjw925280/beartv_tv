package com.gys.play.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.R
import com.gys.play.databinding.IncludeBrvRefreshPullBinding
import com.mybase.libb.ext.getLifeActivity
import com.mybase.libb.ext.setNoDouble
import post


class BaseRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val mBind: IncludeBrvRefreshPullBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.include_brv_refresh_pull,
        this,
        true
    )

    val page get() = mBind.brvPage.index

    var first = true

    val prl get() = mBind.brvPage
    val rv get() = mBind.brvRV

    init {

        mBind.brvPage.onError {
            this.findViewById<View>(R.id.goError)?.setNoDouble {
                autoRefresh()
            }
        }
    }

    fun <T> init(
        request: suspend () -> BaseListInfo<T>,
        rv: (RecyclerView) -> Unit
    ) {
        init(10, request, rv)

    }

    fun <T> init(
        limit: Int,
        request: suspend () -> BaseListInfo<T>,
        rv: (RecyclerView) -> Unit
    ) {
        rv(mBind.brvRV)

        mBind.brvPage.onRefresh {
            context.getLifeActivity()?.let {
                it.post(state = {
                    when (it) {
                        LoadState.ERROR -> {
                            if (first)
                                mBind.brvPage.showError()
                            else {
                                if (page == 1) {
                                    mBind.brvPage.finishRefresh()
                                } else {
                                    mBind.brvPage.finishLoadMore(false)
                                }
                            }
                        }
                        LoadState.SUCCESS -> {
                            if (first)
                                first = false
                        }
                    }
                }) {
                    val data = request()
                    addData(data.items) {
                        data.items.size == limit  // 判断是否有更多页
                    }
                }
            }
        }

    }

    fun autoRefresh() = mBind.brvPage.autoRefresh()


    fun refresh() = mBind.brvPage.refresh()
}