package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class DownloadBookDialog(context: Context?, listener: OnDownloadBookListener) :
    BaseCenterDialog(context) {

    var listener: OnDownloadBookListener? = null

    init {
        this.listener = listener
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_download_book, null)
        view.findViewById<TextView>(R.id.tv_left).setOnClickListener {
            listener?.let {
                it.onContinueDownload()
                dismiss()
            }
        }
        view.findViewById<TextView>(R.id.tv_right).setOnClickListener {
            listener?.let {
                it.onGoPay()
                dismiss()
            }
        }
        return view
    }

    companion object {
        fun show(context: Context?, listener: OnDownloadBookListener): DownloadBookDialog {
            var dialog = DownloadBookDialog(context, listener)
            dialog.show()
            return dialog
        }
    }
}