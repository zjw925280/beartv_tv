package com.gys.play.util

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV

object FreeCountDownUtil {

    private const val RECORD_TIME_KEY = "record_time_key"

    val countDownTimer = MutableLiveData<Long>()

    private var runing = false

    private var mCountDownTimer: CountDownTimer? = null

    private const val ONE_DAY_SECOND = 86400 * 1000

    private val kv = MMKV.defaultMMKV()

    private var temporary = 0

    private fun countDownTime(
        time: Int,
    ) = object : CountDownTimer((time.times(1000)).toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            temporary++
            countDownTimer.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            countDownTimer.value = -1
            runing = false
            start(ONE_DAY_SECOND)
        }

    }

    /**
     * 取消倒计时
     */
    fun cancel(isStart: Boolean = false) {
        if (!isStart) {
            kv?.encode(RECORD_TIME_KEY, 0L)
            runing = false
        }
        temporary = 0
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    fun checkRestart(): Boolean {
        val currentTime = kv?.decodeLong(RECORD_TIME_KEY, 0L)
        if (currentTime != null && currentTime > 0L) {
            val a = System.currentTimeMillis() / 1000
            val temporary1 = (a - currentTime)
            return temporary1 - temporary !in -10..10

        }
        return false
    }


    fun start(totalTime: Int) {
        if (runing) return

        cancel(true)
        runing = true
        kv?.encode(RECORD_TIME_KEY, System.currentTimeMillis() / 1000)

        mCountDownTimer = countDownTime(totalTime)
        mCountDownTimer?.start()
    }

}