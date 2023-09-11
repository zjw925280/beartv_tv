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
import androidx.recyclerview.widget.GridLayoutManager
import com.android.liba.context.AppContext
import com.android.liba.ui.base.listgroup.holder.RepeatClickListener
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.gys.play.adapter.RechargeAdapter
import com.gys.play.databinding.ActivityRechargeBinding
import com.gys.play.entity.CommodityInfo
import com.gys.play.entity.UserInfo
import com.gys.play.ui.helpfeedback.HelpAndFeedbackActivity
import com.gys.play.util.dialog.RechargeOpenBoxDialog
import com.gys.play.viewmodel.GoogsViewModel

class RechargeActivity :
    PayBaseActivity<GoogsViewModel<RechargeActivity>, ActivityRechargeBinding>(R.layout.activity_recharge) {

    var isSel = true
    val firstPayList = mutableListOf<CommodityInfo>() //首充数据
    val payList = mutableListOf<CommodityInfo>()
    var firstPayAdapter = RechargeAdapter(firstPayList, 1)
    var notFirstAdapter = RechargeAdapter(payList, 2)
    var isSelect = true

    companion object {
        fun start(context: Context?, bookId: Int = 0, chapterId: Int = 0) {
            context?.run {
                val intent = Intent(context, RechargeActivity::class.java)
                intent.putExtra(Config.NOVEL_ID, bookId)
                intent.putExtra(Config.CHAPTER_ID, chapterId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    override fun updateUserInfo(userInfo: UserInfo) {
        firstPayList.clear()
        payList.clear()
        binding.balance.text = userInfo.coins
        presenter.getAppGoodsIndex { _, data ->
            val list = mutableListOf<CommodityInfo>()
            var position = 0
            for (item in data.items) {
                list.add(CommodityInfo(null, isSelect, position++, item))
            }
            queryProductList(list)
            for (pos in list.indices) {
                if (!isLogin()) {
                    firstUserData(list, pos)
                } else {
                    if (userInfo.isFirstRecharge()) { //首充用户
                        firstUserData(list, pos)
                    } else { //非首充用户
                        notFirstUserData(list, pos)
                    }
                }
            }
            if (!isLogin()) {
                binding.rvFirstPay.visibility = View.VISIBLE
                firstPayAdapter.setList(firstPayList)
            } else {
                if (userInfo.isFirstRecharge()) { //首充用户
                    binding.rvFirstPay.visibility = View.VISIBLE
                    firstPayAdapter.setList(firstPayList)
                } else {
                    binding.rvFirstPay.visibility = View.GONE
                }
            }
            notFirstAdapter.setList(payList)
        }
        var isPaySuccess = AppContext.getMMKV().decodeBool(Config.GOOGLE_PAY_SUCCESS, false)
        if (isPaySuccess) {
            AppContext.getMMKV().encode(Config.GOOGLE_PAY_SUCCESS, false)
            RechargeOpenBoxDialog.show(
                this, userInfo.coins
            )
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
        setClickListener()
        setTopRightClick {
            MyApplication.getAnalytics().setBookCoin("明细点击量")
            if (!isLogin()) {
                goLogin()
                return@setTopRightClick
            }
            startActivity(Intent(this, GoldOpenRecordActivity::class.java))
        }
        binding.rvFirstPay.layoutManager = GridLayoutManager(this, 3)
        binding.rvFirstPay.adapter = firstPayAdapter

        binding.rv.layoutManager = GridLayoutManager(this, 3)
        binding.rv.adapter = notFirstAdapter

        binding.pay.isEnabled = true
        binding.pay.isSelected = true

        val builder = SpannableStringBuilder(binding.text.text)
        setTextSpan(builder, binding.text.text.toString(), "【幫助反饋】", "#00ACFF") {
            startActivity(Intent(activity, HelpAndFeedbackActivity::class.java))
        }
        binding.text.setMovementMethod(LinkMovementMethod.getInstance())
        binding.text.setText(builder)
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

    private fun setClickListener() {
        binding.imgSelPay.setOnClickListener {
            if (isSel) {
                binding.imgSelPay.setImageResource(R.mipmap.icon_gou)
                binding.pay.isEnabled = false
                binding.pay.isSelected = false
                isSel = false
            } else {
                binding.pay.isEnabled = true
                binding.pay.isSelected = true
                binding.imgSelPay.setImageResource(R.mipmap.icon_gou_n)
                isSel = true
            }
        }
    }

    override fun initData() {
        binding.customer.setOnClickListener {
            MyApplication.getAnalytics().setBookCoin("充值遇到问题点击量")
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

    override fun loadData(savedInstanceState: Bundle?) {
        firstPayAdapter.listener = listener
        notFirstAdapter.listener = listener
    }

    var listener = object : RechargeAdapter.OnSetSelClickListener {
        override fun onSelClickListener(position: Int, type: Int) {
            var newSelect: CommodityInfo
            if (type == 1) {
                newSelect = firstPayList.get(position)
            } else {
                newSelect = payList.get(position)
            }

            getPaymentAmountText().text = "$" + newSelect.googsInfo.price
            if (select != null && select == newSelect) {
                return
            }
            select = newSelect
            upSelectUi(firstPayAdapter)
            upSelectUi(notFirstAdapter)
        }
    }

    private fun upSelectUi(
        adapter: RechargeAdapter,
    ) {
        val list = adapter.list
        for (pos in list.indices) {
            list.get(pos).run {
                if (googsInfo.id == select?.googsInfo?.id) {
                    this.isSelect = true
                    adapter?.notifyItemChanged(pos)
                } else {
                    if (this.isSelect) {
                        this.isSelect = false
                        adapter?.notifyItemChanged(pos)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isSelect = true
    }

    private fun firstUserData(list: MutableList<CommodityInfo>, pos: Int) {
        if (list.get(pos).googsInfo.is_first == 1) {
            list.get(pos).isSelect = isSelect
            if (isSelect) {
                select = list.get(pos)
                getPaymentAmountText().text = "$" + list.get(pos).googsInfo.price
                isSelect = false
            }
            firstPayList.add(list.get(pos))
        } else {
            list.get(pos).isSelect = false
            payList.add(list.get(pos))
        }
    }

    private fun notFirstUserData(list: MutableList<CommodityInfo>, pos: Int) {
        if (list.get(pos).googsInfo.is_first != 1) {
            list.get(pos).isSelect = isSelect
            if (isSelect) {
                select = list.get(pos)
                getPaymentAmountText().text = "$" + list.get(pos).googsInfo.price
                isSelect = false
            }
            payList.add(list.get(pos))
        }
    }

    override fun getPayAdapter() = RechargeAdapter()

    override fun getPayView() = binding.pay

    override fun getPaymentAmountText() = binding.paymentAmount

    override fun getAppPayCreateOrder(commodityInfo: CommodityInfo?) {
        commodityInfo?.data?.let {
            if (isSel) {
                presenter.getAppPayCreateOrder(
                    select!!.googsInfo.id,
                    bookId,
                    chapterId
                ) { _, data ->
                    pay(data, commodityInfo)
                }
            }
        }
    }
}
