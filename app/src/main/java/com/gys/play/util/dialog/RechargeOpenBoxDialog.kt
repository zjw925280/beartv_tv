package com.gys.play.util.dialog

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.R

class RechargeOpenBoxDialog(
    context: Context?,
    coin: String
) :
    BaseCenterDialog(context) {

    var coin = ""

    init {
        this.coin = coin
    }

    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_recharge_open_box, null)
        var imgOpenBox = view.findViewById<ImageView>(R.id.img_open_box)
        var tvCoin = view.findViewById<TextView>(R.id.tv_coin)
        tvCoin.text =
            context.getString(R.string.congratulations_give_coins) + " " + coin + " " + context.getString(
                R.string.give_coins
            )
        var ani = imgOpenBox.background as AnimationDrawable
        ani.start()
        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            ani.stop()
            dismiss()
        }
        return view
    }

    companion object {
        fun show(
            context: Context?,
            coin: String
        ): RechargeOpenBoxDialog {
            var dialog = RechargeOpenBoxDialog(context, coin)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }
}