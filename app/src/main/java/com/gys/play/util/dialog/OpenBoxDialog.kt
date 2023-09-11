package com.gys.play.util.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.liba.context.AppContext
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.Config
import com.gys.play.R

class OpenBoxDialog(context: Context?, type: Int, coins: Int, listener: OnBoxRewardlListener) :
    BaseCenterDialog(context) {
    var listener: OnBoxRewardlListener? = null
    var type = 0
    var coins = 0

    init {
        this.listener = listener
        this.type = type
        this.coins = coins
    }

//    var animationBox: AnimationDrawable? = null
    override fun getContentView(): View {
        var view = layoutInflater.inflate(R.layout.dialog_open_box, null)
//        var ivBgAnimation = view.findViewById<ImageView>(R.id.ivOpenBox)
//        animationBox = ivBgAnimation.getBackground() as AnimationDrawable?
//        animationBox?.isOneShot = true
//        animationBox!!.start()
        AppContext.getMMKV().encode(Config.FIRST_GIFT, false)
        when (type) {
            0 -> { //未领取
                view.findViewById<TextView>(R.id.tv_left).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.tv_right).visibility = View.GONE
            }
            1 -> { //已领取
                view.findViewById<TextView>(R.id.tv_left).visibility = View.GONE
                view.findViewById<TextView>(R.id.tv_right).visibility = View.VISIBLE
            }
        }
        view.findViewById<TextView>(R.id.coins_num).text = coins.toString()
        view.findViewById<TextView>(R.id.tv_left).setOnClickListener {
            if (listener != null) {
                listener?.onSure(type)
            }
            AppContext.showToast(context.getString(R.string.great_toast))
            dismiss()
        }
        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            dismiss()
        }
        return view
    }

    companion object {
        fun show(
            context: Context?,
            type: Int,
            coins: Int,
            listener: OnBoxRewardlListener
        ): OpenBoxDialog {
            var dialog = OpenBoxDialog(context, type, coins, listener)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }
}