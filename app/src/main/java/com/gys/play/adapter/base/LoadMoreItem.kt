package com.gys.play.adapter.base

import androidx.databinding.ViewDataBinding
import com.gys.play.R

const val LOAD_LOADING = 0 //默认状态，加载更多中
const val LOAD_END = 1 //加载完成

class LoadMoreItem(val layout: Int = R.layout.item_load_more, val listener: LoadMoreListener) {

    var mark = -1 //标记 通过这个标记来避免多次回调加载更多
    var type = LOAD_LOADING

    var binding: ViewDataBinding? = null

    fun loadMore(position: Int) {
        if (position == mark) {
            return
        }
        mark = position
        if (type == LOAD_END) {
            return
        }
        listener?.loadMore()
    }

}