package com.gys.play.entity

data class GiftInfo(
    var is_get: Int = 0, //1-已领取 0-未领取
    var is_show: Int = 0,//1-显示 0-不显示
    var coins: Int = 0, //数量
    var flag: Int = 0 //1-已领取 0-未领取
)