package com.gys.play.util.dialog

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.android.liba.context.AppContext
import com.android.liba.network.protocol.BaseListInfo
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.jccppp.dialog.setClickDismiss
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.SignListInfo
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.view.SignInView
import java.util.*

class DaySignDialog(
    context: Context,
    val data: BaseListInfo<SignListInfo>,
    listener: DaySignListener
) :
    JustDialog(context) {

    var listener: DaySignListener? = null

    init {

        addView(R.layout.dialog_day_sign).setWidth(0.93f).setClickDismiss(R.id.img_close)
        getContentView()
        this.listener = listener
    }


    fun getContentView() {

        view.findViewById<TextView>(R.id.tv_day).text = data.sign_days.toString()
        var signView = view.findViewById<SignInView>(R.id.signView)

        signView.setData(data)

        view.findViewById<TextView>(R.id.tv_check_it).setOnClickListener {
            if (SpUserInfo.isLogin()) {
                listener?.let {
                    it.checkIt(this)
                }
            } else {
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }

        view.findViewById<View>(R.id.img_rule).setOnClickListener {
            SignRuleDialog.show(context)
        }

    }


    companion object {
        fun show(
            context: Context,
            datas: BaseListInfo<SignListInfo>,
            listener: DaySignListener
        ): DaySignDialog {
            var dialog = DaySignDialog(context, datas, listener)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }

        fun isNotToday(): Boolean {
            var t = Calendar.getInstance()
            t.timeZone = TimeZone.getTimeZone("GMT+8:00")
            var year = t.get(Calendar.YEAR)
            var month = t.get(Calendar.MONTH) + 1
            var date = t.get(Calendar.DAY_OF_MONTH)
            var oldyear = AppContext.getMMKV().getInt("year", -1)
            var oldmonth = AppContext.getMMKV().getInt("month", -1)
            var olddate = AppContext.getMMKV().getInt("date", -1)
            saveMsg()
            if (oldyear == -1 || oldmonth == -1 || olddate == -1) {
                return true
            }
            if (oldyear < year) {
                return true
            } else if (oldmonth < month) {
                return true
            } else if (olddate < date) {
                return true
            } else {
                return false
            }
        }

        fun saveMsg() {
            var t = Calendar.getInstance()
            t.timeZone = TimeZone.getTimeZone("GMT+8:00")
            var year = t.get(Calendar.YEAR)
            var month = t.get(Calendar.MONTH) + 1
            var date = t.get(Calendar.DAY_OF_MONTH)
            AppContext.getMMKV().putInt("year", year)
            AppContext.getMMKV().putInt("month", month)
            AppContext.getMMKV().putInt("date", date)
        }
    }
}