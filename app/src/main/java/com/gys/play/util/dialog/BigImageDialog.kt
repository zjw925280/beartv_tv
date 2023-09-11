package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class BigImageDialog(context: Context?,url:String) :
    BaseCenterDialog(context) {
    var url:String = url

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_img, null)
        val rbStar = view.findViewById<ImageView>(R.id.iv_big_image)
        rbStar.setImageURI(url.toUri())
        rbStar.setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(context: Context?,url: String): BigImageDialog {
            var dialog = BigImageDialog(context,url)
            dialog.show()
            return dialog
        }
    }
}