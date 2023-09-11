package com.gys.play.ui

import android.view.View
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.util.UIHelper
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetailsParams
import com.gys.play.MyApplication
import com.gys.play.adapter.base.Adapter
import com.gys.play.entity.CommodityInfo
import com.gys.play.entity.GoogsInfo
import com.gys.play.entity.OrderInfo
import com.gys.play.entity.UserInfo
import com.gys.play.util.LiveDataUtil
import com.gys.play.viewmodel.NovelBasePresent

abstract class PayBaseActivity<P : NovelBasePresent<*>, T : ViewBinding>(layout: Int) :
    NovelBaseActivity<P, T>(layout) {
    abstract fun getPayAdapter(): Adapter<CommodityInfo>
    val adapter = getPayAdapter()
    var select: CommodityInfo? = null

    fun initGoodsList(data: BaseListInfo<GoogsInfo>) {
        val list = mutableListOf<CommodityInfo>()
        var isSelect = true
        var position = 0
        for (item in data.items) {
            val data = CommodityInfo(null, isSelect, position++, item)
            if (isSelect) {
                select = data
                getPaymentAmountText().text = "$" + data.googsInfo?.price
            }
            list.add(data)
            isSelect = false
        }
        adapter.setList(list)
        queryProductList(list)
    }

    /**
     * 查询服务端返回的商品列表信息
     */
    fun queryProductList(data: MutableList<CommodityInfo>) {
        val skuList: MutableList<String> = ArrayList()

        for (item in data) {
            skuList.add(item.googsInfo.google_product_id)
        }

        val params = SkuDetailsParams.newBuilder()
        UIHelper.showLog(TAG, "googlePlay skuList: $skuList ")

        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        MyApplication.getPay().billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            UIHelper.showLog(TAG, "googlePlay billingResult: $billingResult ")
            UIHelper.showLog(TAG, "googlePlay skuDetailsList: $skuDetailsList")
            val list = mutableListOf<CommodityInfo>()
            if (!skuDetailsList.isNullOrEmpty()) {
                for (detail in skuDetailsList) {
                    UIHelper.showLog(TAG, "queryProductList detail: $detail")
                    for (i in 0 until data.size) {
                        val item = data[i]
                        if (item.googsInfo.google_product_id.equals(detail.sku)) {
                            item.data = detail
                        }
                        UIHelper.showLog(TAG, "queryProductList: $item")
                    }
                }
                UIHelper.showLog(TAG, "queryProductList size: ${list.size}")
            }
        }
    }

    abstract fun getPayView(): View

    fun pay(orderInfo: OrderInfo, commodityInfo: CommodityInfo) {
        if (!isLogin()) {
            goLogin()
            return
        }
        commodityInfo.data?.let {
            getMMKV()?.putString(orderInfo.order_no, select?.googsInfo?.price)
            //因为同一个商品未付款的话不能重复订阅，所以直接设置覆盖值
            UIHelper.showLog(TAG, "appPayVerifyOrder orderNo  : ${orderInfo.order_no}")
//            getMMKV()?.putString(it.sku, orderInfo.order_no)
            val flowParams = BillingFlowParams.newBuilder()
                .setObfuscatedAccountId(orderInfo.user_id)
                .setObfuscatedProfileId(orderInfo.order_no)
                .setSkuDetails(it)
                .build()
            val code: Int =
                MyApplication.getPay().billingClient.launchBillingFlow(
                    activity,
                    flowParams
                ).getResponseCode()
            UIHelper.showLog(TAG, "googlePlay code: $code")
            MyApplication.getPay().addOrderNo(orderInfo.order_no)
        }
    }

    var payClickTime: Long = 0

    override fun setListener() {
        LiveDataUtil.userInfo.observe(this) {
            updateUserInfo(it)
        }
        getPayView().setOnClickListener {
            if (this is VIPActivity) {
                MyApplication.getAnalytics().setVip("立即开通点击量")
            } else {
                MyApplication.getAnalytics().setBookCoin("立即支付点击量")
            }
            UIHelper.showLog(TAG, "googlePlay details: ${select?.data} ")
            select?.data?.let {
                if (!isLogin()) {
                    goLogin()
                    return@let
                }
                val commodityInfo = select
                val time = System.currentTimeMillis()
                if (payClickTime + 2000 < time) {
                    payClickTime = time
                    getAppPayCreateOrder(commodityInfo)
                }
            }
        }

        adapter.itemClickListener = object : Adapter.OnItemClickListener<CommodityInfo> {
            override fun onItemClick(view: View, data: CommodityInfo, position: Int) {
                select?.let {
                    it.isSelect = false
                    adapter.notifyItemChanged(it.position)
                }
                data.isSelect = true
                adapter.notifyItemChanged(data.position)
                select = data
                getPaymentAmountText().text = "$" + data.googsInfo?.price
            }
        }
    }

    abstract fun updateUserInfo(userInfo: UserInfo)

    abstract fun getPaymentAmountText(): TextView

    abstract fun getAppPayCreateOrder(commodityInfo: CommodityInfo?)
}