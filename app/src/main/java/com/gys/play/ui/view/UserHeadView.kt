package com.gys.play.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.UserInfo
import com.gys.play.getString
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.LiveDataUtil


class UserHeadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    val tvName: TextView
    val tvVip: TextView
    val userImage: ImageView

    init {

        LayoutInflater.from(context).inflate(R.layout.include_user_head, this)

        userImage = findViewById(R.id.userImage)
        tvName = findViewById(R.id.tvName)
        tvVip = findViewById(R.id.tvVip)


        if (context is LifecycleOwner) {
            LiveDataUtil.userInfo.observe(context) {
                updateUserInfo(it)
            }
        }
        updateUserInfo(SpUserInfo.getUserInfo())
    }

    fun updateUserInfo(userInfo: UserInfo) {
        userInfo.run {
            //默认没登录
            var userName = R.string.not_log_in.getString()
            var vipTime = R.string.vip_is_not_available_yet.getString()
            //登录变更
            if (isLogin()) {
                ImageLoaderUtils.loadCircle(userImage, avatar)
                userName = user_name
            } else {
                userImage.setImageResource(R.mipmap.icon_tx_m)
            }
            //vip变更
            if (isVip()) {
                vipTime = String.format(R.string.validity.getString(), getVipTime())
                tvVip.setTextColor(Color.parseColor("#F5E2BC"))
            } else {
                tvVip.setTextColor(Color.parseColor("#99F5E2BC"))
            }
            tvName.text = userName
            tvVip.text = vipTime
        }
    }
}