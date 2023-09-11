package com.gys.play.wow

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gys.play.R
import com.gys.play.entity.WowDetailBean

class WowAdapter(private val context: Context) :
    RecyclerView.Adapter<WowItemHolder>() {

    private val data: MutableList<WowDetailBean> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WowItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_wow_video, parent, false)
        return WowItemHolder(context, v)
    }

    override fun onBindViewHolder(holder: WowItemHolder, position: Int) {
        holder.onBind(position, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getData() = data

    fun newData(newData: List<WowDetailBean>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(moreData: List<WowDetailBean>) {
        data.addAll(moreData)
        notifyItemRangeInserted(data.size - moreData.size, data.size)
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }
}