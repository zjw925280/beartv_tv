package com.gys.play.entity

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gys.play.R

class TextTimeItem(
    val listener: TextTimeItemClickListener,
    val position: Int,
    val isSelect: Boolean = false,
)

interface TextTimeItemClickListener {
    fun click(item: TextTimeItem)
}

@BindingAdapter("textTimeInit")
fun textTimeInit(view: TextView, item: TextTimeItem) {
    view.text = " ${item.position + 1}月"
    if (item.isSelect) {
        view.setTextColor(Color.parseColor("#FFFFFF"))
        view.setBackgroundResource(R.drawable.shape_rect_00acff_3)
    } else {
        view.setTextColor(Color.parseColor("#52576B"))
        view.setBackgroundColor(Color.parseColor("#ffffff"))
    }
}

