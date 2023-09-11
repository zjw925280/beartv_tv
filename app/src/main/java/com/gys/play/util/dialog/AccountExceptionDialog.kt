package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class AccountExceptionDialog(context: Context?) :
    BaseCenterDialog(context) {


    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_account_exception, null)
        view.findViewById<TextView>(R.id.tv_account_cancel).setOnClickListener {
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_customs).setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(context: Context?): AccountExceptionDialog {
            var dialog = AccountExceptionDialog(context)
            dialog.show()
            return dialog
        }
    }
}