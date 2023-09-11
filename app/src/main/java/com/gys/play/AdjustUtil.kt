package com.gys.play

import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent


object AdjustUtil {

    const val  appToken = "ni5xboyjcv7k"

    fun register() {
        Adjust.trackEvent(AdjustEvent("vhx0dm"))
    }

    fun pay(money: Double) {
        val adjustEvent = AdjustEvent("gksrfe")
        adjustEvent.setRevenue(money, "USD")
        Adjust.trackEvent(adjustEvent)
    }
}