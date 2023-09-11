package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class AutoPayExplainDialog(context: Context?) :
    BaseCenterDialog(context) {

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_auto_pay_explain, null)
        var tvRight = view.findViewById<TextView>(R.id.tv_right)
        tvRight.setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(context: Context?): AutoPayExplainDialog {
            var dialog = AutoPayExplainDialog(context)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }
}