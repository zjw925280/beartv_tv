package com.gys.play.adapter.base

import android.view.View
import androidx.databinding.ViewDataBinding
import com.android.liba.util.UIHelper

open class Adapter<T>(
    val br: Int,
    val layoutId: Int,
    list: MutableList<T> = mutableListOf()
) : BaseAdapter<T>(list) {

    var loadMoreItem: LoadMoreItem? = null

    var itemClickListener: OnItemClickListener<T>? = null
    var itemLongClickListener: OnItemLongClickListener<T>? = null

    override fun getItemViewType(position: Int): Int {
        if (list.size > position) {
            return getItemLayout(position)
        }
        loadMoreItem?.let {
            return it.layout
        }
        UIHelper.showLog(
            "Adapter",
            "getItemViewType: error position $position > list.size ${list.size} "
        )
        return layoutId
    }

    open fun getItemLayout(position: Int): Int {
        return layoutId
    }


    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            loadMoreItem?.let {
                return list.size + 1
            }
        }
        return list.size
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        var binding: ViewDataBinding = holder.dataBinding
        if (position >= list.size) {
            loadMoreItem?.let {
                it.binding = binding
                it.loadMore(position)
                binding.setVariable(br, it.type)
            }
            return
        }
//        binding.setVariable(BR.position, position)
        val item = list?.get(position)
        binding.setVariable(br, item)
        convert(binding, item, position)

        //这边的点击监听会和binding里面的顶层view点击监听冲突
        itemClickListener?.let {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, item, position)
            }
        }
        itemLongClickListener?.let {
            binding.root.setOnLongClickListener {
                itemLongClickListener?.onItemLongClick(it, item, position)
                return@setOnLongClickListener true
            }
        }

    }

    fun loadEnd() {
        loadMoreItem?.let {
            it.type = LOAD_END
            it?.binding?.setVariable(br, it.type)
        }
    }


    open fun convert(binding: ViewDataBinding, item: T, position: Int) {
    }


    interface OnItemClickListener<T> {
        fun onItemClick(view: View, data: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(view: View, data: T, position: Int)
    }


}