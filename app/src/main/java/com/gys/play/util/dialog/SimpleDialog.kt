package com.gys.play.util.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.android.liba.jk.OnListener
import com.android.liba.ui.dialog.BaseDialog
import com.gys.play.R

open class SimpleDialog(
    context: Context,
    val title: String? = null,
    val content: String? = null,
    val leftText: String? = null,
    val rightText: String? = null,
    val leftClickListener: View.OnClickListener? = null,
    val rightClickListener: View.OnClickListener? = null
) : BaseDialog(context) {
    companion object {

        fun addBookshelf1(context: Context, onListener: OnListener<Boolean>): SimpleDialog {
            context.run {
                val dialog = SimpleDialog(
                    context,
                    getString(R.string.reminder),
                    getString(R.string.whether_add_bookshelf),
                    getString(R.string.oh_wait),
                    getString(R.string.add_to_bookshelf),
                    leftClickListener = { onListener.onListen(false) },
                    rightClickListener = { onListener.onListen(true) }
                )
                dialog.show()
                return dialog
            }
        }

        fun addBookshelf(context: Context, rightClickListener: View.OnClickListener): SimpleDialog {
            context.run {
                val dialog = SimpleDialog(
                    context,
                    getString(R.string.reminder),
                    getString(R.string.whether_add_bookshelf),
                    getString(R.string.oh_wait),
                    getString(R.string.add_to_bookshelf),
                    rightClickListener = rightClickListener
                )
                dialog.show()
                return dialog
            }
        }

        fun delbookshelf(context: Context, rightClickListener: View.OnClickListener): SimpleDialog {
            context.run {
                val dialog = SimpleDialog(
                    context,
                    getString(R.string.whether_remove_bookshelf),
                    getString(R.string.think_twice_please),
                    getString(R.string.yes_please),
                    getString(R.string.no_thanks),
                    rightClickListener
                )
                dialog.show()
                return dialog
            }
        }

        fun delComment(context: Context, rightClickListener: View.OnClickListener): SimpleDialog {
            context.run {
                val dialog = SimpleDialog(
                    context,
                    getString(R.string.comment_del_title),
                    getString(R.string.comment_del_content),
                    getString(R.string.confirm_del),
                    getString(R.string.cancel_del),
                    rightClickListener
                )
                dialog.show()
                return dialog
            }
        }
    }

    open fun getLayoutId(): Int = R.layout.dialog_layout

    override fun initContentView(layoutInflater: LayoutInflater): View {
        val view = layoutInflater.inflate(getLayoutId(), null)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvTitle?.run {
            if (title.isNullOrBlank()) {
                visibility = View.GONE
            } else {
                text = title
            }
        }
        if (!content.isNullOrBlank()) {
            val tvContent = view.findViewById<TextView>(R.id.tvContent)
            tvContent?.text = content
        }
        val tvLeft = view.findViewById<TextView>(R.id.tv_left)
        if (leftText.isNullOrEmpty()) {
            tvLeft?.visibility = View.GONE
        }
        leftText?.let { tvLeft?.text = it }
        tvLeft?.run {
            setOnClickListener {
                dismiss()
                leftClickListener?.let { it.onClick(tvLeft) }
            }
        }
        val tvRight = view.findViewById<TextView>(R.id.tv_right)
        rightText?.let { tvRight?.text = it }
        if (rightText.isNullOrEmpty()) {
            tvRight?.visibility = View.GONE
        }
        tvRight?.run {
            setOnClickListener {
                dismiss()
                rightClickListener?.let { it.onClick(tvLeft) }
            }
        }
        return view
    }
}