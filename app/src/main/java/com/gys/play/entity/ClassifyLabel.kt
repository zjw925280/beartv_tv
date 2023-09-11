package com.gys.play.entity

import android.view.View

data class ClassifyLabel(
    val id: Int,
    val title: String,
    var mix: Int = 0,
    var isSelect: Boolean = false,
    var listener: SelectListener? = null
) {
    fun click(view: View) {
        if (isSelect || title.isNullOrEmpty()) {
            return
        }
        listener?.let {
            isSelect = true
            view.isSelected = isSelect
            listener?.selectListener(this)
        }
    }
}

