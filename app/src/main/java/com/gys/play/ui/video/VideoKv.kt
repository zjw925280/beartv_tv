package com.gys.play.ui.video

import com.gys.play.Config
import com.gys.play.coroutines.Quest
import com.gys.play.entity.SpUserInfo
import com.gys.play.util.MmKvPref
import com.lzx.pref.KvPrefModel
import com.lzx.pref.dynamicKeyPref
import com.lzx.pref.property.DynamicKeyPref
import com.mybase.libb.ext.notEmpty
import com.tencent.mmkv.MMKV
import postGlobal

object VideoKv : KvPrefModel("video", MmKvPref()) {

    private var lastId = ""

    private var lastposition = 0  //上一集

    private var lastProgress = 0

    private var lastTime = 0L

    //章节记录时长
    private var videoLocalForId: DynamicKeyPref<Long> by dynamicKeyPref(0L)

    //本地播放第几集
    private var videoLocalIndex: DynamicKeyPref<Int> by dynamicKeyPref(0)

    fun recordHistory(id: String, chapterId: String, position: Int, progress: Int, time: Long) {
        if (!SpUserInfo.isLogin()) {
            return
        }
        if (position != lastposition) {
            lastposition = position
            videoLocalIndex.set("__$id", position)
        }
        if (chapterId == lastId && progress == lastProgress) {
            return
        }

        if (lastId.notEmpty() && chapterId != lastId) {
            //有记录要上传
            recordNet(lastId, lastProgress)
        } else {
            if (progress % 5 == 0) {
                recordNet(chapterId, progress)
            }
        }
        if (time - 1500 > lastTime) {
            recordLocal(chapterId, time)
        }

        lastId = chapterId
        lastProgress = progress
        lastTime = time


    }

    private fun recordNet(id: String, progress: Int) {
        if (lastId.notEmpty() && progress in 1..100)
            postGlobal(showToast = false) {
                Quest.api.addReadLog(Quest.getHead(Config.APP_READ_LOG) {
                    add("chapter_id", id).add("progress", "$progress")
                })
            }
    }

    private fun recordLocal(id: String, time: Long) {
        videoLocalForId.set("_$id", time)
    }

    fun getLocalRecord(id: String): Long {
        return videoLocalForId.get("_$id")
    }

    fun getLocalIndex(videoId: String): Int {
        return videoLocalIndex.get("__$videoId")
    }

    fun onDestroy() {
        recordNet(lastId, lastProgress)
        lastId = ""
        lastProgress = 0
        lastTime = 0L
    }

    fun removeLocal() {
        MMKV.mmkvWithID("video")?.clearAll()
    }
}