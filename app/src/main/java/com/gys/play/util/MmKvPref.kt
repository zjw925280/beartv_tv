package com.gys.play.util

import android.content.Context
import android.content.SharedPreferences
import com.lzx.pref.KvPrefProvider
import com.tencent.mmkv.MMKV


open class MmKvPref : KvPrefProvider {

    var kv: MMKV? = null

    override fun get(context: Context, name: String, mode: Int): SharedPreferences {
        if (MMKV.getRootDir().isNullOrEmpty()) {
            MMKV.initialize(context)
        }
        kv = MMKV.mmkvWithID(name, MMKV.SINGLE_PROCESS_MODE)
        return kv as SharedPreferences
    }
}