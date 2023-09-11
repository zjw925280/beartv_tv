package com.gys.play.db

import androidx.annotation.WorkerThread
import com.android.billingclient.api.Purchase
import com.gys.play.Config
import com.gys.play.db.dao.OrderDao
import com.gys.play.db.entity.Order

class OrderRepository(val dao: OrderDao) {

    val all = dao.getAll()

    fun getUnfilledOrders() = dao.getAllForStatus(Config.GOOGLE_ORDER_CONSUMPTION)


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(order: Order) {
        dao.insert(order)
    }


    /**
     *用户支付的订单
     */
    fun userPayOrder(purchase: Purchase): Order {
        var productId = ""
        if (purchase.skus.isNotEmpty()) {
            productId = purchase.skus[0]
        }
        val orderid = purchase.accountIdentifiers
        val accountId = orderid?.obfuscatedAccountId ?: "userNull"
        val profileId = orderid?.obfuscatedProfileId ?: "profileIdNull${System.currentTimeMillis()}"
        val order = Order(
            purchase.orderId,
            productId,
            purchase.purchaseToken,
            purchase.quantity,
            Config.GOOGLE_ORDER_USER_PAY,
            accountId,
            profileId
        )
        dao.insert(order)
        return order
    }

    /**
     *用户支付 APP 已经消费 未发货
     */
    fun consumptionOrder(purchaseToken: String) {
        dao.updateOrderStatus(Config.GOOGLE_ORDER_CONSUMPTION, purchaseToken)
    }

    /**
     *用户支付 APP 已经消费  服务端已经发货
     */
    fun orderEnd(purchaseToken: String) {
        dao.updateOrderStatus(Config.GOOGLE_ORDER_END, purchaseToken)
    }

    /**
     *用户支付 APP 消费错误
     */
    fun orderError(purchaseToken: String) {
        dao.updateOrderStatus(Config.GOOGLE_ORDER_CONSUMPTION_ERROR, purchaseToken)
    }
}