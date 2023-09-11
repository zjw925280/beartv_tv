package com.android.liba.util.js


interface BaseJsCallBack {

    fun call(name: String, vararg o: Any?):Boolean
}