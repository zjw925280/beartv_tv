package com.gys.play.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemCommentItemDetailBinding
import com.gys.play.entity.CommentInfo

class CommentItemAdapter(list: MutableList<CommentInfo> = mutableListOf()) :
    Adapter<CommentInfo>(BR.item, R.layout.item_comment_item_detail, list) {

    override fun convert(binding: ViewDataBinding, item: CommentInfo, position: Int) {
        val binding = binding as ItemCommentItemDetailBinding
        binding.more = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                commentListener?.more(item)
            }
        }
    }

    var commentListener: CommentListener? = null

    interface CommentListener {
        fun edit(item: CommentInfo)
        fun goDetail(item: CommentInfo)
        fun agree(item: CommentInfo)
        fun more(item: CommentInfo)
    }
}