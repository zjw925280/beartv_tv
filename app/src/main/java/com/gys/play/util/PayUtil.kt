package com.gys.play.util

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.android.liba.context.AppContext
import com.android.liba.jk.OnListener
import com.android.liba.util.UIHelper
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.entity.CommodityInfo
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.LoginActivity

object PayUtil {
    private const val TAG = "GPayUtil"

    data class GooglePayInfo(
        val google_product_id: String,
        val order_no: String,
        val price: String,
        val user_id: String
    )

    fun doGooglePay(
        activity: FragmentActivity,
        googleProduceId: String,
        price: String,
        orderNo: String,
        userId: String
    ) {
        if (!SpUserInfo.isLogin()) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            return
        }
        queryProductList(googleProduceId) {
            if (it == null) {
                AppContext.showToast(R.string.unsupport_google_pay)
            } else {
                AppContext.getMMKV()?.putString(orderNo, price)
                //因为同一个商品未付款的话不能重复订阅，所以直接设置覆盖值
                UIHelper.showLog(TAG, "appPayVerifyOrder orderNo  : $orderNo")
//            getMMKV()?.putString(it.sku, orderInfo.order_no)
                val flowParams = BillingFlowParams.newBuilder()
                    .setObfuscatedAccountId(userId)
                    .setObfuscatedProfileId(orderNo)
                    .setSkuDetails(it)
                    .build()
                val code: Int =
                    MyApplication.getPay().billingClient.launchBillingFlow(
                        activity,
                        flowParams
                    ).responseCode
                UIHelper.showLog(TAG, "googlePlay code: $code")
                MyApplication.getPay().addOrderNo(orderNo)
            }
        }
    }

    /**
     * 查询服务端返回的商品列表信息
     */
    private fun queryProductList(googleProduceId: String, onListener: OnListener<SkuDetails?>) {
        val skuList: MutableList<String> = ArrayList()

        skuList.add(googleProduceId)

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
                    if (googleProduceId == detail.sku) {
                        onListener.onListen(detail)
                        return@querySkuDetailsAsync
                    }
                }
                UIHelper.showLog(TAG, "queryProductList size: ${list.size}")
            }
            onListener.onListen(null)
        }
    }
}