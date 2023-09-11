package com.gys.play.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.gys.play.R
import com.gys.play.entity.BtVideoInfo
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup


class NovelItemRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    val mAdapter: BindingAdapter


    init {

        var layoutId = -1

        var gridSize = 0
        var vertical = true
        var scroll = false

        if (attrs != null) {
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.NovelItemRecyclerView)
            try {
                layoutId =
                    attributes.getResourceId(
                        R.styleable.NovelItemRecyclerView_layout_item,
                        layoutId
                    )
                gridSize =
                    attributes.getInt(R.styleable.NovelItemRecyclerView_layout_grid_num, gridSize)
                vertical = attributes.getBoolean(
                    R.styleable.NovelItemRecyclerView_layout_vertical,
                    vertical
                )
                scroll = attributes.getBoolean(
                    R.styleable.NovelItemRecyclerView_layout_scroll_enable,
                    scroll
                )
            } finally {
                attributes.recycle()
            }
        }

        if (layoutId < 0) throw RuntimeException("请设置  layout_item  ID")

        isNestedScrollingEnabled = scroll

        mAdapter = (if (gridSize > 0) grid(
            gridSize,
            orientation = if (vertical) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL,
            scrollEnabled = scroll
        ) else linear(
            orientation = if (vertical) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL,
            scrollEnabled = scroll
        )).setup {
            addType<BtVideoInfo>(layoutId)

        }
        mAdapter.models
    }

    var models: List<Any?>? = null
        get() = mAdapter._data
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            mAdapter.models = value
        }

}