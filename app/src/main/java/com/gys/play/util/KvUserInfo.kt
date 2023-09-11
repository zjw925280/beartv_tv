package com.gys.play.util

import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.android.liba.util.MyLiveData
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.getString
import com.lzx.pref.KvPrefModel
import com.lzx.pref.intPref
import com.mybase.libb.ext.getAppGlobal



object KvUserInfo : KvPrefModel("userInfo", MmKvPref()) {

    fun isLogin() = SpUserInfo.isLogin()

    private var _lookType by intPref(0)

    val lookType get() = _lookType

    val lookBoy get() = _lookType != 2 //当前是否是男频

    val lookTypeLd = MutableLiveData(lookBoy) //当前是否是男频
    val lookTypeLiveData = MyLiveData(lookType)
//
    fun setChangeLook(type: Int) {
        if (_lookType != type) {
            _lookType = type
            lookTypeLd.value = lookBoy
            lookTypeLiveData.value = type
        }
    }

    val isEnglish get() = R.string.lang_type.getString() == "2"

}


fun <T> T.boy(girl: T): T {
    if (KvUserInfo.lookBoy) return this
    return girl
}

fun getBoyColor(): Int = ContextCompat.getColor(
    getAppGlobal(),
    R.color.C_2997FD.boy(R.color.C_FD7BA9)
)