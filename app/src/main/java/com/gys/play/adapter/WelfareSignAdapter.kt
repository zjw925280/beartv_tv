package com.gys.play.adapter

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.android.liba.jk.OnListener
import com.gys.play.BR
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.ItemWelfareSignBinding
import com.gys.play.entity.SignInfo
import com.gys.play.entity.SignListData

class WelfareSignAdapter(
    val activity: FragmentActivity,
    var item: MutableList<SignInfo>,
    private var onDoSignListener: OnListener<Int>?//-1:已签到已翻倍，0:未签到，1：已签到未翻倍
) :
    Adapter<SignInfo>(BR.item, R.layout.item_welfare_sign, item) {

    private var isTodayDoubleEnable = false
    private var is_sign_today = 0
    private var sign_days = 0
    var itemWidth = 0
    var itemHeight = 0

    fun setSignData(signListData: SignListData?) {
        isTodayDoubleEnable = signListData?.isTodayDoubleEnable() ?: false
        is_sign_today = signListData?.is_sign_today ?: 0
        sign_days = signListData?.sign_days ?: 0
    }

    override fun convert(binding: ViewDataBinding, item: SignInfo, position: Int) {
        super.convert(binding, item, position)
        (binding as ItemWelfareSignBinding).apply {
            if (itemWidth > 0 && itemHeight > 0) {
                val lp = root.layoutParams
                lp.width = itemWidth
                lp.height = itemHeight
                root.layoutParams = lp
            }

            item.sign_days = sign_days
            setItem(item)
            layout.setOnClickListener {
                //position = sign_days即今天可签到
                if (is_sign_today == 1) {
                    //今天已签到
                    if (position != sign_days - 1) {
                        //非可签到的那天点击
                        return@setOnClickListener
                    }
                    if (isTodayDoubleEnable) {
                        //可双倍
                        onDoSignListener?.onListen(1)//已签到未翻倍
                    } else {
                        onDoSignListener?.onListen(-1)//已签到已翻倍
                        return@setOnClickListener
                    }
                } else {
                    //未签到
                    if (position != sign_days) {
                        //非可签到的那天点击
                        return@setOnClickListener
                    }
                    onDoSignListener?.onListen(0)//未签到
                }
            }
        }
    }
}