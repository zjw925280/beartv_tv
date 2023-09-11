package com.gys.play.adapter

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.BR
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemReportBinding

class ReportAdapter(list: MutableList<String>, type: Int) :
    Adapter<String>(BR.item, R.layout.item_report, list), Parcelable {

    var index = -1
    var type = -1
    var listener: OnSetSelClickListener? = null

    constructor(parcel: Parcel) : this(
        TODO("list"),
        TODO("type")
    ) {
        index = parcel.readInt()
        type = parcel.readInt()
    }

    init {
        this.type = type
    }

    override fun convert(binding: ViewDataBinding, item: String, position: Int) {
        super.convert(binding, item, position)
        val binding = binding as ItemReportBinding
        if (position == index) {
            binding.imgSel.setImageResource(R.mipmap.ic_jb_sel)
        } else {
            binding.imgSel.setImageResource(R.mipmap.ic_jb_nor)
        }
        binding.root.setOnClickListener {
            index = position
            notifyItemRangeChanged(0,itemCount,0)
            listener?.let {
                it.onSelClickListener(type, item)
            }
        }
    }

    fun refreash() {
        index = -1
        notifyItemRangeChanged(0,itemCount,0)
    }

    interface OnSetSelClickListener {
        fun onSelClickListener(type: Int, content: String)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReportAdapter> {
        override fun createFromParcel(parcel: Parcel): ReportAdapter {
            return ReportAdapter(parcel)
        }

        override fun newArray(size: Int): Array<ReportAdapter?> {
            return arrayOfNulls(size)
        }
    }
}