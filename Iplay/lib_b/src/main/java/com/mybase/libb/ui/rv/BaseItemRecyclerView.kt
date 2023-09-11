package com.mybase.libb.ui.rv

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.mybase.libb.R

open class BaseItemRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    lateinit var mAdapter: BindingAdapter

    init {
        var layoutId = -1

        var gridSize = 0
        var vertical = true
        var scroll = true
        var lazy = false

        if (attrs != null) {
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.BaseItemRecyclerView)
            try {
                //布局xml ID
                layoutId =
                    attributes.getResourceId(R.styleable.BaseItemRecyclerView_layout_item, -1)
                //grid数量  小于1是grid   大于1是 linear
                gridSize =
                    attributes.getInt(R.styleable.BaseItemRecyclerView_layout_grid_num, 0)

                //水平 or 垂直
                vertical = attributes.getBoolean(
                    R.styleable.BaseItemRecyclerView_layout_vertical,
                    vertical
                )

                lazy = attributes.getBoolean(
                    R.styleable.BaseItemRecyclerView_layout_lazy,
                    lazy
                )

                // 是否可以滑动
                scroll = attributes.getBoolean(
                    R.styleable.BaseItemRecyclerView_layout_scroll_enable,
                    scroll
                )

            } finally {
                attributes.recycle()
            }
        }

        if (!lazy)
            init(layoutId, gridSize, vertical, scroll)

    }


    fun init(layoutId: Int, gridSize: Int, vertical: Boolean, scroll: Boolean) {

//        isNestedScrollingEnabled = scroll

        mAdapter = (if (gridSize > 0) grid(
            gridSize,
            orientation = if (vertical) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL,
            scrollEnabled = scroll
        ) else linear(
            orientation = if (vertical) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL,
            scrollEnabled = scroll
        )).setup {
            if (layoutId > 0)
                addType<IBaseItem>(layoutId)

            setup(this, it)
        }
    }

    fun setup(adapter: BindingAdapter, rv: RecyclerView) {

    }

    var models: List<Any?>? = null
        get() = mAdapter._data
        set(value) {
            field = value
            mAdapter.models = value
        }


}