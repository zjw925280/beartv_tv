package com.gys.play.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gys.play.R
import com.gys.play.BR
import com.gys.play.MyApplication
import com.gys.play.adapter.base.Adapter
import com.gys.play.entity.ClassifyLabel
import com.gys.play.entity.SelectListener

class LabelAdapter(

    list: MutableList<ClassifyLabel>
) :
    Adapter<ClassifyLabel>(BR.item, R.layout.item_table, list) {

    fun getSelectId(): Int {
        if (selectData == null) {
            return 0
        }
        return selectData!!.id
    }

    var selectData: ClassifyLabel? = null

    var tableListener: SelectListener? = null

    var listener: View.OnClickListener? = null

    init {
        initData()
    }

    @SuppressLint("ResourceAsColor")
    override fun convert(binding: ViewDataBinding, item: ClassifyLabel, position: Int) {
        super.convert(binding, item, position)
//        val binding = binding as ItemTableBinding
//        if (item.isSelect) {
//            binding.image.visibility = View.VISIBLE
//        } else {
//            binding.image.visibility = View.INVISIBLE
//        }
    }

    private fun initData() {
        if (list.isNullOrEmpty()) {
            return
        }
        tableListener ?: let {
            tableListener = object : SelectListener {
                override fun selectListener(select: ClassifyLabel) {
                    MyApplication.getAnalytics().setClassify("${select.title}")
                    selectData?.isSelect = false
                    val id = list.indexOf(selectData)
                    selectData = select
                    notifyItemChanged(id!!)
                    notifyItemChanged(list.indexOf(select))
                    listener?.onClick(null)
                }
            }
        }
        selectData = list[0]
        selectData?.isSelect = true
        for (item in list) {
            item.listener = tableListener
        }
    }

    fun updateData(newData: MutableList<ClassifyLabel>) {
        list = newData
        //添加空白数据
        list?.let {
            var nullItem = 1
            while (nullItem * 5 < it.size) {
                list!!.add(nullItem * 5, ClassifyLabel(0, ""))
                nullItem++
            }
        }
        initData()
        notifyDataSetChanged()
    }

    fun updateData1(newData: MutableList<ClassifyLabel>) {
        list = newData
        //添加空白数据
        list?.let {
            var nullItem = 1
            while (nullItem * 4 < it.size) {
                list!!.add(nullItem * 4, ClassifyLabel(0, ""))
                nullItem++
            }
        }
        initData()
        notifyDataSetChanged()
    }
}