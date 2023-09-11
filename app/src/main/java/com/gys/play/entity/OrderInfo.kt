package com.gys.play.entity

//{
//    "user_id": 20001,
//    "goods_id": 1,
//    "goods_name": "150 Coins",
//    "amount": 299,
//    "coins": 150,
//    "order_no": "2112241057500623345109",
//    "trade_no": "",
//    "ip": "127.0.0.1",
//    "pay_type": "Google",
//    "type": 1,
//    "os_type": "android",
//    "order_time": 1640314670,
//    "status": 0,
//    "id": 3
//}
data class OrderInfo(
    val id: Int,
    val user_id: String,
    val goods_id: String,
    val goods_name: String,
    val order_no: String,
)
