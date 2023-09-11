package com.gys.play.entity

/**
 * Created by Koi.
 * Date: 2021/12/30
 * explain: 接收操作型返回对象
 */
data class StatusInfo(
    var status: String,
    var desc: String
) {
    constructor() : this(
        "",""
    )

    val isSuccess get() = status=="success"
}
