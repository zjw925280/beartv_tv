package com.gys.play.adapter

import androidx.databinding.ViewDataBinding
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemMyCommentBinding
import com.gys.play.entity.CommentInfo

class MyCommentAdapter(list: MutableList<CommentInfo> = mutableListOf()) :
    Adapter<CommentInfo>(BR.item, R.layout.item_my_comment, list) {

    override fun convert(binding: ViewDataBinding, item: CommentInfo, position: Int) {
        (binding as ItemMyCommentBinding).apply {
            rlMore.setOnClickListener {
                deleteListener?.delete(item)
            }
        }
    }

    var deleteListener: DeleteListener? = null

    interface DeleteListener {
        fun delete(item: CommentInfo)
    }
}