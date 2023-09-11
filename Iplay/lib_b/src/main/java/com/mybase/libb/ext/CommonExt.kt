package com.mybase.libb.ext

import androidx.annotation.ColorInt
import com.blankj.utilcode.util.SizeUtils


@ColorInt
fun Int.getColor() = getAppGlobal().resources.getColor(this)


fun Int.getDrawable() = getAppGlobal().resources.getDrawable(this)

fun Any?.notEmpty(): Boolean {
    if (this is CharSequence) {
        return !isNullOrEmpty()
    } else if (this is Collection<*>) {
        return !isNullOrEmpty()
    }
    return this != null
}

fun Float.dp2px() = SizeUtils.dp2px(this)

fun Float.px2dp() = SizeUtils.px2dp(this)

fun String?.getStr(default: String = ""): String {
    if (this.notEmpty()) return this!!
    return default
}

fun String?.getSafeInt(default: Int = 0): Int {
    try {
        return this.getStr("$default").toInt()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return 0
}


