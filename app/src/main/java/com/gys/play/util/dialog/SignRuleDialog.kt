package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class SignRuleDialog(
    context: Context?
) :
    BaseCenterDialog(context) {

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_sign_rule, null)
        view.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(
            context: Context?,
        ): SignRuleDialog {
            var dialog = SignRuleDialog(context)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }
}