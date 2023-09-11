package com.gys.play.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemCommentBinding
import com.gys.play.entity.CommentInfo
import com.gys.play.util.onClick

class CommentAdapter(list: MutableList<CommentInfo> = mutableListOf()) :
    Adapter<CommentInfo>(BR.item, R.layout.item_comment, list) {
    var isDetailActivity = false
    override fun convert(binding: ViewDataBinding, item: CommentInfo, position: Int) {
        if (binding is ItemCommentBinding) {
            binding.bottomLine.visibility =
                if (position == itemCount - 1) View.GONE else View.VISIBLE



            binding.agree.isSelected = item.agree
            binding.agree.onClick = {
                item.is_agree = if(item.agree) 0 else 1
                it.isSelected = item.agree
                if (it.isSelected) {
                    item.agree_count++
                } else {
                    item.agree_count--
                }
                binding.agree.text = "${item.agree_count}"
                commentListener?.agree(item)
            }

            if (isDetailActivity) {
                return
            }
            binding.moreView.visibility = View.VISIBLE

            binding.contxt.onClick = {
                commentListener?.edit(item)
            }

        }
    }

    var commentListener: CommentListener? = null
    var reportListener: ReportListener? = null

    interface CommentListener {
        fun edit(item: CommentInfo)
        fun goDetail(item: CommentInfo)
        fun agree(item: CommentInfo)
        fun more(item: CommentInfo)
    }

    interface ReportListener {
        fun report(item: CommentInfo)
    }
}