package com.gys.play.ui.video

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import com.mybase.libb.ext.getColor
import com.mybase.libb.ext.setNoDouble
import com.gys.play.R
import com.gys.play.databinding.PopVideoSpeedBinding
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig


class SpeedPop(
    context: Context,
    private val speed: Float,
    private val onSpeedChange: (Float) -> Unit
) :
    BasePopupWindow(context) {

    private var normalSpeed = floatToSpeed(speed)

    private lateinit var db: PopVideoSpeedBinding


    init {
        popupGravity = Gravity.RIGHT

        contentView = createPopupById(R.layout.pop_video_speed)
    }

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)

        db = PopVideoSpeedBinding.bind(contentView)

        db.tv3.setNoDouble {
            setSpeed(SpeedVideo.NO3)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv2.setNoDouble {
            setSpeed(SpeedVideo.NO2)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv17.setNoDouble {
            setSpeed(SpeedVideo.NO175)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv15.setNoDouble {
            setSpeed(SpeedVideo.NO15)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv125.setNoDouble {
            setSpeed(SpeedVideo.NO125)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv1.setNoDouble {
            setSpeed(SpeedVideo.NO1)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        db.tv05.setNoDouble {
            setSpeed(SpeedVideo.NO05)
            onSpeedChange.invoke(normalSpeed.speed)
            dismiss()
        }

        setSpeed(normalSpeed)
    }

    private fun setSpeed(speed: SpeedVideo) {
        setTvColor(normalSpeed, false)
        normalSpeed = speed
        setTvColor(speed, show = true)
    }

    private fun setTvColor(speed: SpeedVideo, show: Boolean) {

        val tv = when (speed) {
            SpeedVideo.NO3 -> db.tv3
            SpeedVideo.NO2 -> db.tv2
            SpeedVideo.NO1 -> db.tv1
            SpeedVideo.NO175 -> db.tv17
            SpeedVideo.NO15 -> db.tv15
            SpeedVideo.NO125 -> db.tv125
            SpeedVideo.NO05 -> db.tv05
        }
        tv.setTextColor(if (show) R.color.C_00ACFF.getColor() else R.color.C_52576B.getColor())
    }

    private fun floatToSpeed(float: Float): SpeedVideo {
        return when (float) {
            3f -> SpeedVideo.NO3
            2f -> SpeedVideo.NO2
            1.75f -> SpeedVideo.NO175
            1.5f -> SpeedVideo.NO15
            1.25f -> SpeedVideo.NO125
            0.5f -> SpeedVideo.NO05
            else -> SpeedVideo.NO1
        }
    }


    override fun onCreateShowAnimation(): Animation? {
        //这里完成展示动画
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.FROM_RIGHT)
            .toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        //这里完成消失动画
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.TO_RIGHT)
            .toShow()
    }


}

enum class SpeedVideo(val speed: Float) {

    NO3(3f),
    NO2(2f),
    NO175(1.75f),
    NO15(1.5f),
    NO125(1.25f),
    NO1(1f),
    NO05(0.5f)
}