package com.gys.play.ui.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditCommentViewModel : ViewModel() {

    val changeOrderType = MutableLiveData<Int>()

    val editTxt = MutableLiveData<String>()

    val editSwitch = MutableLiveData<Boolean>()

    val allSelected = MutableLiveData<Boolean>()

    val startDelete =  MutableLiveData<Boolean>()

}