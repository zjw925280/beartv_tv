package com.gys.play.entity

import com.gys.play.MyApplication
import com.gys.play.R

//{
//    "id": 6,
//    "title": "Week",
//    "price": 1.01,
//    "google_product_id": "a11",
//    "vip_ori_price": 2,
//    "vip_activity_tag": "周体验",
//    "vip_activity_desc": "一周体验会员",
//    "vip_type": 1,
//    "vip_day": 7
//}
data class GoogsInfo(
    val id: Int,
    val title: String,
    val price: String,
    val google_product_id: String,
    val vip_ori_price: String,
    //充值
    val coins: Int,
    val give_coins: Int,
    val is_first: Int,
    val is_most: Int,
    //vip
    val vip_activity_tag: String,
    val vip_activity_desc: String,
    val vip_type: Int,
    val vip_day: Int
) {
    fun isShowTop(): Boolean {
        return is_first == 1 || is_most == 1
    }

    fun getTopText(): String {
        val resources = MyApplication.getInstance().getActivityResources()
        if (is_first == 1) {
            return resources.getString(R.string.discount_for_new_user)
        }
        if (false&&is_most == 1) {
            return resources.getString(R.string.most_people_choose)
        }
        return ""
    }

    fun isFirst() = is_first == 1
}
