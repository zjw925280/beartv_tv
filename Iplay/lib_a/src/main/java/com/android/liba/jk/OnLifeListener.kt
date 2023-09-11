package com.android.liba.jk

import android.app.Activity

interface OnLifeListener {
    fun onCreate(activity:Activity)
    fun onResume(activity:Activity)
    fun onPause(activity:Activity)
    fun onStop(activity:Activity)
    fun onDestroy(activity:Activity)
}