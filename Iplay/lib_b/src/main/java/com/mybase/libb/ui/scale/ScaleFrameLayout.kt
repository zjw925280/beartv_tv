package com.mybase.libb.ui.scale

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.blankj.utilcode.util.ScreenUtils
import com.mybase.libb.R


class ScaleFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var heightScale = 0f
    private var widthScale = 0f

    init {

        attrs?.let {
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.ScaleView)

            heightScale = attributes.getFloat(R.styleable.ScaleView_height_scale, 0f)
            widthScale = attributes.getFloat(R.styleable.ScaleView_width_scale, 0f)
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (heightScale <= 0f && widthScale <= 0f) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            var widthMeasureSpec = widthMeasureSpec
            var heightMeasureSpec = heightMeasureSpec
            if (heightScale > 0f) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (ScreenUtils.getAppScreenHeight() * heightScale).toInt(),
                    MeasureSpec.getMode(heightMeasureSpec)
                )
            }
            if (widthScale > 0f) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    (ScreenUtils.getAppScreenWidth() * widthScale).toInt(),
                    MeasureSpec.getMode(widthMeasureSpec)
                )
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        }

    }

}