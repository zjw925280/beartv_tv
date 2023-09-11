package com.gys.play.entity

import com.android.billingclient.api.SkuDetails

class CommodityInfo(
    var data: SkuDetails?,
    var isSelect: Boolean,
    val position: Int,
    val googsInfo: GoogsInfo,
)