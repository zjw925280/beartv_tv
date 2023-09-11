package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.android.liba.ui.dialog.DialogButtonListener
import com.gys.play.R

class AgainAgreementDialog(context: Context?, listener: DialogButtonListener?) :
    BaseCenterDialog(context) {

    private var listener: DialogButtonListener? = null

    init {
        this.listener = listener
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_again_agreement, null)
        view.findViewById<TextView>(R.id.tv_sure_again).setOnClickListener {
            if (listener != null) {
                listener!!.onSure()
            }
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            if (listener != null) {
                listener!!.onCancel()
            }
            dismiss()
        }
        return view
    }

    companion object {
        fun show(context: Context?, listener: DialogButtonListener?): AgainAgreementDialog {
            var dialog = AgainAgreementDialog(context, listener)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }
}