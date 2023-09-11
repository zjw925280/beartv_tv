package com.gys.play.entity

import com.gys.play.MyApplication
import com.gys.play.R

class RewardListInfo(
    var id: Int = 0,
    var title_tw: String = "",
    var title_en: String = "",
    var icon: String = "",
    var coins: Int = 0
) {
    fun setGiftName(): String {
        var name = ""
        var lang = MyApplication.getInstance().getActivityResources().getString(R.string.lang_type)
        if (lang.equals("2")) {
            name = title_en
        } else {
            name = title_tw
        }
        return name
    }
}