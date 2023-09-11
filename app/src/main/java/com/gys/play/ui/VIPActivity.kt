package com.gys.play.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.android.liba.context.AppContext
import com.android.liba.ui.base.listgroup.holder.RepeatClickListener
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.gys.play.adapter.CommodityAdapter
import com.gys.play.databinding.ActivityVipBinding
import com.gys.play.entity.CommodityInfo
import com.gys.play.entity.GoogsInfo
import com.gys.play.entity.UserInfo
import com.gys.play.ui.helpfeedback.HelpAndFeedbackActivity
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.LiveDataUtil
import com.gys.play.viewmodel.GoogsViewModel

class VIPActivity :
    PayBaseActivity<GoogsViewModel<VIPActivity>, ActivityVipBinding>(R.layout.activity_vip) {
    override fun getPayAdapter() = CommodityAdapter()

    companion object {
        fun start(context: Context?, bookId: Int = 0, chapterId: Int = 0) {
            context?.run {
                val intent = Intent(context, VIPActivity::class.java)
                intent.putExtra(Config.NOVEL_ID, bookId)
                intent.putExtra(Config.CHAPTER_ID, chapterId)
                startActivity(intent)
            }
        }
    }

    var isSel = true
    override fun setListener() {
        super.setListener()
        if (isSel) {
            binding.pay.isEnabled = true
            binding.pay.isSelected = true
        } else {
            binding.pay.isEnabled = false
            binding.pay.isSelected = false
        }
        binding.imgSelPay.setOnClickListener {
            if (isSel) {
                binding.imgSelPay.setImageResource(R.mipmap.icon_gou)
                binding.pay.isEnabled = false
                binding.pay.isSelected = false
            } else {
                binding.pay.isEnabled = true
                binding.pay.isSelected = true
                binding.imgSelPay.setImageResource(R.mipmap.icon_gou_n)
            }
            isSel = !isSel
        }
    }

    override fun isStatusBarTextDark(): Boolean {
        return false
    }

    var bookId = 0
    var chapterId = 0
    override fun initView() {
        bookId = intent.getIntExtra(Config.NOVEL_ID, 0)
        chapterId = intent.getIntExtra(Config.CHAPTER_ID, 0)
        MyApplication.getAnalytics().setVip("vip页面曝光量")
        setTitleText(getString(R.string.vip_membership), R.color.white)
        findViewById<TextView>(R.id.itemTopRight)?.run {
            background = getDrawable(R.mipmap.icon_kf)
            setOnClickListener {
                val serviceEmail = SpServiceConfig.getServiceEmail()
                if (serviceEmail.isNullOrEmpty()) {
                    return@setOnClickListener
                }
                startActivity(
                    Intent(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:$serviceEmail"))
                )
                MyApplication.getAnalytics().setBookCoin("拉起邮箱返回值${serviceEmail}")
            }
        }
        setTopBackImage(R.mipmap.icon_back_white)
        binding.openHistory.setOnClickListener {
            MyApplication.getAnalytics().setVip("开通记录点击量")
            if (!isLogin()) {
                goLogin()
                return@setOnClickListener
            }
            startActivity(Intent(this, VipOpenRecordActivity::class.java))
        }
        binding.rv.layoutManager = GridLayoutManager(this, 3)
        binding.rv.adapter = adapter
        LiveDataUtil.userInfo.observe(this) {
            updateUserInfo(it)
        }
        val text = binding.vipExplain.text.toString()
        val vip1 = getString(R.string.vip1)
        val vip2 = getString(R.string.vip2)
        val vip3 = getString(R.string.vip3)
        val vip4 = getString(R.string.vip4)
        val builder = SpannableStringBuilder(text)
        setTextSpan(builder, text, vip1, "#00ACFF") {
            Config.toUserRulePage(this)
        }
        setTextSpan(builder, text, vip2, "#00ACFF") {
            Config.toPrivacyPage(this)
        }
        setTextSpan(builder, text, vip3, "#00ACFF") {
            Config.toVipRulePage(this)
        }
        setTextSpan(builder, text, vip4, "#00ACFF") {
            startActivity(Intent(activity, HelpAndFeedbackActivity::class.java))
        }
        binding.vipExplain.setMovementMethod(LinkMovementMethod.getInstance())
        binding.vipExplain.setText(builder)
    }

    private fun setTextSpan(
        builder: SpannableStringBuilder,
        text: String,
        vip1: String,
        colorString: String = "#ff0000",
        listener: View.OnClickListener
    ) {
        val start = text.indexOf(vip1)
        Log.i(TAG, "setTextSpan: $start  $text    $vip1")
        if (start < 0) {
            return
        }
        builder.setSpan(object : RepeatClickListener() {
            override fun notRepeatClick(v: View) {
                listener.onClick(v)
            }

            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }, start, start + vip1.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        builder.setSpan(
            ForegroundColorSpan(Color.parseColor(colorString)),
            start, start + vip1.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }

    override fun updateUserInfo(userInfo: UserInfo) {
        userInfo.run {
            //默认没登录
            var userName = getString(R.string.not_log_in)
            var vipTime = getString(R.string.vip_is_not_available_yet)
//            var textColor = resources.getColor(R.color.gray_9f9f9f)
            var userBack = resources.getDrawable(R.mipmap.img_vip_bg_n)
            //登录变更
            if (isLogin()) {
//                textColor = resources.getColor(R.color.black_4b2600)
                ImageLoaderUtils.loadCircle(binding.userImage, avatar)
                userName = user_name
            } else {
                binding.userImage.setImageResource(R.mipmap.icon_tx_m)
            }
            //vip变更
            if (isVip()) {
                vipTime = getString(R.string.validity, getVipTime())
                userBack = resources.getDrawable(R.mipmap.img_vip_bg)
            }
            binding.userName.text = userName
            binding.vipTime.text = vipTime
//            binding.vipTime.setTextColor(textColor)
//            binding.userName.setTextColor(textColor)
            binding.userBack.background = userBack
        }
        AppContext.getMMKV().encode(Config.GOOGLE_PAY_SUCCESS, false)
        getVipList(userInfo)
    }

    override fun initData() {
    }

    private fun getVipList(userInfo: UserInfo) {
        presenter.getAppVIPGoodsIndex { _, data ->
            if (!userInfo.isFirstRecharge()) {
                val list = mutableListOf<GoogsInfo>()
                for (d in data.items) {
                    if (d.isFirst()) {
                        list.add(d)
                    }
                }
                for (d in list) {
                    data.items.remove(d)
                }
            }
            initGoodsList(data)
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {

    }

    override fun getPayView() = binding.pay

    override fun getPaymentAmountText() = binding.paymentAmount

    override fun getAppPayCreateOrder(commodityInfo: CommodityInfo?) {
        presenter.getAppPayCreateOrder(commodityInfo!!.googsInfo.id, bookId, chapterId) { _, data ->
            pay(data, commodityInfo)
        }
    }

}
