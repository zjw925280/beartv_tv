package com.gys.play.ui.video

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class NoTouchImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        setOnClickListener {}

        setOnTouchListener { _, _ -> true }
    }

}