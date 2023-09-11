package com.gys.play.util.view.flowlayout

import android.view.View
import com.gys.play.entity.ClassifyLabel

class NovelTagAdapter(list: MutableList<ClassifyLabel>) : TagAdapter<ClassifyLabel>(list) {
    override fun getView(parent: FlowLayout?, position: Int, t: ClassifyLabel?): View? {
        return parent
    }
}