package com.gys.play.ui.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gys.play.entity.VideoDetailBean

class VideoViewModel : ViewModel() {

    val playPosition = MutableLiveData<Int>()

    val videoDetailBean = MutableLiveData<VideoDetailBean>()

    val showChoosePop = MutableLiveData<Boolean>()

    val commentNum = MutableLiveData<String>()

}