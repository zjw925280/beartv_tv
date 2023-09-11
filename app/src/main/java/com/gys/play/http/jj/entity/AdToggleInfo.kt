package com.gys.play.http.jj.entity

import java.io.Serializable

data class AdToggleInfo(val ret: Int, val data: AdToggleInfoDataBean?, val msg: String) :
    Serializable {
    companion object {
        private const val serialVersionUID = -10016L
    }

    fun isShowAd(): Boolean {
        if (data == null) return false
        return data.show == 1
    }

    fun isCompliance(): Boolean {
        if (data == null) return false
        return data.compliance == 1
    }
}

data class AdToggleInfoDataBean(
    val source: String,
    val time: Long,
    val show: Int,
    val compliance: Int,
    val data: List<AdToggleInfoData1>?
) : Serializable {
    companion object {
        private const val serialVersionUID = -10017L
    }
}

data class AdToggleInfoData1(
    var id: Int,
    val title: String,
    val subhead: String,
    val link: String,
    val img: String,
    val bgimg: String,
    val adid: Int,
    val appid: Int,
    val position: Int,
    val sort: Int,
    val status: Int,
    val addtime: Int,
    val extra: String,
    val wxfrom: String,
    val wxxct: Int,
    val wxid: String,
    val click: Int,
    val show: Int,
    val xinxiliu: String,
    val sleeptime: Int,
    val isshow: Int?
) : Serializable {
    companion object {
        private const val serialVersionUID = -10018L
    }
}
