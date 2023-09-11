package com.gys.play.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * type 支付状态
 *
 */
@Entity(tableName = "order")
data class Order(
    val orderId: String, //谷歌支付好了之后获取的订单号 需要传给服务端
    val productId: String,//谷歌的商品ID
    @PrimaryKey @ColumnInfo val purchaseToken: String,//谷歌支付好了之后获取的订单 token 需要传给服务端
    /**
     *@param status
     * 1 用户支付成功
     * 2 结算成功
     * 3 服务器发货
     * 4 失败
     */
    var status: Int,//订单状态
    var quantity: Int,//购买数量
    val userID: String,//用户id
    val orderNo: String,//服务端返回的订单编号 需要传给服务端
) {
    override fun toString(): String {
        return "Order(googOrderId='$orderId', productId='$productId', purchaseToken='$purchaseToken', status=$status, quantity=$quantity, userID=$userID, orderNo=$orderNo)"
    }
}

