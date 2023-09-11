package com.gys.play.adapter.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(var list: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent?.context), viewType, parent, false
            )
        )
    }


    fun replaceData(newData: List<T>) {
        list.clear()
        list.addAll(newData)
    }

    fun clear() {
        if (list == null) {
            list = mutableListOf()
        }
        notifyItemRangeRemoved(0, list.size)
        list.clear()
    }


    fun addDatas(newData: MutableList<T>, itemSize: Int) {
        if (list == null) {
            list = mutableListOf()
        }
        list!!.addAll(newData)
        notifyItemRangeChanged(0, itemSize)
    }

    fun addDatas(newData: MutableList<T>) {
        if (list == null) {
            list = mutableListOf()
        }
        val startNumber = itemCount
        list!!.addAll(newData)
        notifyItemRangeChanged(startNumber, newData.size)
    }

    open fun setList(list: Collection<T>?) {
//        if (list !== this.list) {
//            notifyItemRangeRemoved(0,this.list.size)
//            this.list.clear()
//            if (!list.isNullOrEmpty()) {
//                notifyItemRangeInserted(0,list.size)
//                this.list.addAll(list)
//            }
//        } else {
//            if (!list.isNullOrEmpty()) {
//                val newList = ArrayList(list)
//                notifyItemRangeRemoved(0,this.list.size)
//                this.list.clear()
//                notifyItemRangeInserted(0,newList.size)
//                this.list.addAll(newList)
//            } else {
//                notifyItemRangeRemoved(0,this.list.size)
//                this.list.clear()
//            }
//        }
        if (list !== this.list) {
            this.list.clear()
            if (!list.isNullOrEmpty()) {
                this.list.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.list.clear()
                this.list.addAll(newList)
            } else {
                this.list.clear()
            }
        }
        notifyDataSetChanged()
    }
}

