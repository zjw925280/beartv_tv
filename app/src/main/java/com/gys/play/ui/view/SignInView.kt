package com.gys.play.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.liba.network.protocol.BaseListInfo
import com.gys.play.R
import com.gys.play.entity.SignListInfo
import com.gys.play.getString
import com.gys.play.util.KvUserInfo
import com.lihang.ShadowLayout
import com.mybase.libb.ext.dp2px
import com.mybase.libb.ext.getColor
import com.mybase.libb.ext.showGONE
import com.mybase.libb.ext.showVISIBLE


class SignInView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val SIGN_DAY = 7

    private val sls = mutableListOf<ShadowLayout>()
    private val tvSign = mutableListOf<TextView>()
    private val tvDay = mutableListOf<TextView>()
    private val ivSign = mutableListOf<ImageView>()
    private val lines = mutableListOf<View>()

    init {

        for (index in 0 until SIGN_DAY) {
            val inflate = LayoutInflater.from(context).inflate(R.layout.inflate_sign_in, null)

            val sl = inflate.findViewById<ShadowLayout>(R.id.sl)
            val sign = inflate.findViewById<TextView>(R.id.tvSign)
            val day = inflate.findViewById<TextView>(R.id.tvDay)
            val iv = inflate.findViewById<ImageView>(R.id.ivSign)

            sls.add(sl)
            tvSign.add(sign)
            tvDay.add(day)
            ivSign.add(iv)

            addView(inflate, LayoutParams(0, -2, 1f))

            if (index != SIGN_DAY - 1) {
                val layoutParams = LayoutParams(8f.dp2px(), 4f.dp2px())
                layoutParams.gravity = Gravity.TOP
                layoutParams.topMargin = 18f.dp2px()
                val line = View(context)
                line.setBackgroundColor(R.color.C_f2f2f2.getColor())
                lines.add(line)
                addView(line, layoutParams)
            }

        }

    }

    fun setData(data: BaseListInfo<SignListInfo>) {

        for (index in 0 until SIGN_DAY) {

            tvDay[index].text =
                "${index.plus(1)} ${if (index == 0 && KvUserInfo.isEnglish) R.string.day.getString() else R.string.days.getString()}"

            if (index in 0 until data.sign_days) {
                sls[index].let {
                    it.setLayoutBackground(R.color.C_FF8040.getColor())
                    it.setStrokeColor(R.color.C_FF8040.getColor())
                }
                ivSign[index].setImageResource(R.mipmap.ic_welfare_yqd)
                tvDay[index].setTextColor(R.color.C_FF8040.getColor())

                if (index in 0 until data.sign_days - 1) {
                    lines[index].setBackgroundColor(R.color.C_FF8040.getColor())
                }

                tvSign[index].showGONE()

            } else {
                if (data.is_sign_today == 0 && data.sign_days == index) {
                    sls[index].let {
                        it.setLayoutBackground(R.color.C_FFFAEB.getColor())
                        it.setStrokeColor(R.color.C_FF8040.getColor())
                    }
                    tvDay[index].setTextColor(R.color.C_FF8040.getColor())
                    tvSign[index].setTextColor(R.color.C_FF8040.getColor())
                } else {
                    sls[index].let {
                        it.setLayoutBackground(R.color.C_f2f2f2.getColor())
                        it.setStrokeColor(R.color.C_f2f2f2.getColor())
                    }
                    tvSign[index].setTextColor(R.color.C_828282.getColor())
                    tvDay[index].setTextColor(R.color.C_E0E0E0.getColor())
                }
                tvSign[index].text = "*${data.items[index].coins}"
                tvSign[index].showVISIBLE()
                ivSign[index].setImageResource(R.mipmap.icon_welfare_shubi)

            }
        }


    }
}