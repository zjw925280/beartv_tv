package com.gys.play.entity

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gys.play.R

class TextItem(
    var text: String,
    val listener: TextItemClickListener,
    val tag: String = "",//扩展的参数，可以随便设置未自定义信息
    var color: Int = R.color.black_353535,
    val value: String = ""//编辑用户信息使用
)

@BindingAdapter("textInit")
fun textInit(view: TextView, item: TextItem) {
    view.setTextColor(view.resources.getColor(item.color))
}