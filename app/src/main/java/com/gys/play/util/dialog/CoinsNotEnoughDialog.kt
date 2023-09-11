package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class CoinsNotEnoughDialog(context: Context?, listener: OnCoinsNotEnoughListener) :
    BaseCenterDialog(context) {

    var listener: OnCoinsNotEnoughListener? = null

    init {
        this.listener = listener
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_coins_not_enough, null)
        view.findViewById<TextView>(R.id.tv_account_cancel).setOnClickListener {
            listener?.let {
                it.onCancel()
            }
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_pay).setOnClickListener {
            listener?.let {
                it.onPay()
            }
        }
        return view
    }

    companion object {
        var dialog: CoinsNotEnoughDialog? = null
        fun show(context: Context?, listener: OnCoinsNotEnoughListener): CoinsNotEnoughDialog {
            dialog = CoinsNotEnoughDialog(context, listener)
            dialog!!.setCancelable(false)
            dialog!!.show()
            return dialog!!
        }

        fun dismissDialog() {
            dialog?.let {
                it.dismiss()
            }
        }
    }
}