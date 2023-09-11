package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.android.liba.ui.dialog.BaseBottomDialog
import com.gys.play.R

class MyCommentDialog(
    context: Context?,
    listener: MyCommentListener?
) :
    BaseBottomDialog(context) {

    var listener: MyCommentListener? = null

    init {
        this.listener = listener
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_my_comment, null)
        view.findViewById<LinearLayout>(R.id.ll_del).setOnClickListener {
            listener?.let {
                it.onDelete()
                dismiss()
            }
        }
        view.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(
            context: Context?,
            listener: MyCommentListener
        ): MyCommentDialog {
            var dialog = MyCommentDialog(context, listener)
            dialog.show()
            return dialog
        }
    }
}