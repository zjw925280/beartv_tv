package com.gys.play

import com.android.liba.context.AppContext
import com.android.liba.util.UIHelper
import com.android.billingclient.api.*
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.gys.play.db.entity.Order
import com.gys.play.entity.SpUserInfo
import com.gys.play.viewmodel.MainViewModel
import com.tencent.mmkv.MMKV


private const val TAG = "GooglePayUtils"
private const val ORDER_NO_LIST = "orderNoList"

/**
 * 支付工具类
 */
class GooglePayUtils(val application: MyApplication) {
    var orderNoList: MutableSet<String> = mutableSetOf()

    lateinit var billingClient: BillingClient
    var connectionError = 0 //连接错误次数，如果超过 MAX_CONNECTION 次就不连接，部分 google 账号是不能连接的
    val MAX_CONNECTION = 3 //最大连接错误次数

    var http: MainViewModel<MainActivity>? = null

    //先这样写，后面用接口回调
    fun setPresenter(presenter: MainViewModel<MainActivity>) {
        http = presenter
    }

    fun appPayVerifyOrder(
        order: Order,
    ) {
        if (SpUserInfo.isLogin()) {
            UIHelper.showLog(TAG, "appPayVerifyOrder orderNo   : $order")
            http?.appPayVerifyOrder(order.orderNo, order.productId, order.purchaseToken) {
                UIHelper.showLog(TAG, "appPayVerifyOrder str   : $it")
                application.dataViewModel.orderRepository.orderEnd(order.purchaseToken)
                updateUserInfo()
                remoteOrderNo(order.orderNo)
//                注释appsflyer内购埋点，直接使用facebook内的数据
                val eventValues = HashMap<String, Any>()
                var revenue = AppContext.getMMKV()?.getString(order.orderNo, "0")
                eventValues.put(AFInAppEventParameterName.CONTENT_ID, order.productId)
                eventValues.put(AFInAppEventParameterName.CONTENT_TYPE, order.orderId)
                eventValues.put(AFInAppEventParameterName.REVENUE, revenue!!)


                try {
                    AdjustUtil.pay(revenue.toDouble())
                } catch (t: Throwable) {
                    t.printStackTrace()
                }

                AppsFlyerLib.getInstance().logEvent(
                    application,
                    AFInAppEventType.PURCHASE, eventValues, object : AppsFlyerRequestListener {
                        override fun onSuccess() {
                            UIHelper.showLog(TAG, "onSuccess: ")
                        }

                        override fun onError(p0: Int, p1: String) {
                            UIHelper.showLog(TAG, "onError: " + p0 + " " + p1)
                        }
                    }
                )
            }
        }
    }

    fun updateUserInfo() {
        http?.getUserInfo {
            SpUserInfo.saveUserInfo(it)
        }
    }

    /**
     * 初始化
     */
    fun init() {
        MMKV.defaultMMKV()?.getStringSet(ORDER_NO_LIST, orderNoList)?.let {
            orderNoList = it
        }

        billingClient = BillingClient.newBuilder(application)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        startConnection()
    }

    /**
     * 连接谷歌支付服务
     */
    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                UIHelper.showLog(TAG, " onBillingSetupFinished:${billingResult.responseCode} ")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryPurchases()
                    connectionError = 0
                }
            }

            override fun onBillingServiceDisconnected() {
                UIHelper.showLog(TAG, " onBillingServiceDisconnected  ")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                if (++connectionError < MAX_CONNECTION) {
                    startConnection()
                }
            }
        })
    }

    /**
     * 订单状态监听 用户支付成功之后需要APP启动消费
     */
    val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            UIHelper.showLog(
                TAG,
                " onPurchasesUpdated: billingResult $billingResult purchases $purchases"
            )
            UIHelper.showLog(TAG, "  responseCode ${billingResult.responseCode} ")
            UIHelper.showLog(TAG, "  debugMessage ${billingResult.debugMessage} ")
            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    //用户支付成功
                    handlePurchase(purchase)
                }
            } else {
//                if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//                    // Handle an error caused by a user cancelling the purchase flow.
//                }

                // Handle any other error codes.
                var orderNo = ""
                for (str in orderNoList) {
                    orderNo = str
                    break
                }
                http?.payError(orderNo, billingResult.responseCode, billingResult.debugMessage)

                remoteOrderNo(orderNo)
            }

        }

    /**
     * APP消费订单
     * 调用入口，
     *      1.用户支付后通过google回调直接消费
     *      2.用户支付后没有通过google回调，或者没有消费成功，app查询之后重新发起订单消费
     * 文件存储
     */
    private fun handlePurchase(purchase: Purchase) {
        //用户支付成功之后要存储到数据库里面，避免这个时候APP闪退没执行到下一步
        val order = application.dataViewModel.orderRepository.userPayOrder(purchase)
        UIHelper.showLog(TAG, " handlePurchase productId: $order")
        billingClient.consumeAsync(
            ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
        ) { billingResult, str ->
            UIHelper.showLog(TAG, " billingResult.responseCode: ${billingResult.responseCode}")
            UIHelper.showLog(TAG, " billingResult.debugMessage: ${billingResult.debugMessage}")
            UIHelper.showLog(TAG, " str: ${str}")
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                UIHelper.showLog(
                    TAG,
                    " Consumption successful. Delivering entitlement. $str"
                )
//                消费成功通知服务端发货
                application.dataViewModel.orderRepository.consumptionOrder(purchase.purchaseToken)
                AppContext.getMMKV().encode(Config.GOOGLE_PAY_SUCCESS, true)
                AppContext.showToast("success")
                appPayVerifyOrder(order)

            } else {
                application.dataViewModel.orderRepository.orderError(purchase.purchaseToken)
//                消费失败
                UIHelper.showLog(TAG, " Error while consuming: " + billingResult.debugMessage)
            }
        }
    }

    /**
     *  调用的是缓存，就随便调用了
     *  查询订单信息，用户支付但是未消费的订单
     *  https://developer.android.com/reference/com/android/billingclient/api/BillingClient#queryPurchasesAsync(java.lang.String,%20com.android.billingclient.api.PurchasesResponseListener)
     *  只有活动订阅和未消费的一次性订阅才会返回。此方法使用谷歌Play Store应用程序的缓存，而不发起网络请求。
     */
    fun queryPurchases() {
        val mPurchasesResponseListener =
            PurchasesResponseListener { billingResult, purchasesResult ->
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK || purchasesResult == null) return@PurchasesResponseListener
                for (purchase in purchasesResult) {
                    if (purchase == null || purchase.purchaseState != Purchase.PurchaseState.PURCHASED) continue
                    //用户支付成功 消费订单
                    handlePurchase(purchase)
                }
            }
        billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, mPurchasesResponseListener)

        //查询已支付但是未发货的订单
        val list = application.dataViewModel.orderRepository.getUnfilledOrders()
        list?.let {
            for (order in list) {
                appPayVerifyOrder(order)
            }
        }
    }

    fun addOrderNo(str: String) {
        orderNoList.add(str)
        MMKV.defaultMMKV()?.putStringSet(ORDER_NO_LIST, orderNoList)
    }

    fun remoteOrderNo(str: String) {
        orderNoList.remove(str)
        MMKV.defaultMMKV()?.putStringSet(ORDER_NO_LIST, orderNoList)
    }


}