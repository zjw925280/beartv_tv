package com.gys.play.util.dialog

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.DialogButtonListener
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.gys.play.MyApplication
import com.gys.play.R

class RemoveAccountDialog(context: Context, listener: DialogButtonListener?) :
    JustDialog(context) {
    var countDownTimer: CountDownTimer? = null
    private var listener: DialogButtonListener? = null
    var int: Int = 3

    init {
        this.listener = listener
        addView(R.layout.dialog_remove_account).setWidth(0.83f).cancelable(false)
            .cancelOnTouchOutside(false)
        getContentView()
    }

    fun getContentView(): View {
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            if (listener != null) {

                listener!!.onCancel()
            }
            dismiss()
        }

        var tv_remove_account = view.findViewById<TextView>(R.id.tv_remove_account)
        var resource = MyApplication.getInstance().getActivityResources()
        tv_remove_account.setOnClickListener {
            if (listener != null) {
                if (tv_remove_account.text.equals(resource.getString(R.string.sure_delete_account)))
                    listener!!.onSure()
            }
            if (tv_remove_account.text.equals(resource.getString(R.string.sure_delete_account)))
                dismiss()
        }
        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.findViewById<TextView>(R.id.tv_remove_account).text =
                    resource.getString(R.string.sure_delete_account) + "(" + int + "s)"
                int--
            }

            override fun onFinish() {
                view.findViewById<TextView>(R.id.tv_remove_account).text =
                    resource.getString(R.string.sure_delete_account)
                view.findViewById<TextView>(R.id.tv_remove_account)
                    .setTextColor(Color.parseColor("#666666"))
            }
        }.start()
        return view
    }

    companion object {
        fun show(context: Context, listener: DialogButtonListener?): RemoveAccountDialog {
            var dialog = RemoveAccountDialog(context, listener)
            dialog.show()
            return dialog
        }
    }
}