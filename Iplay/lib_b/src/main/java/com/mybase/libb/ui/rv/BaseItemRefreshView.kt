package com.mybase.libb.ui.rv

import android.content.Context
import android.util.AttributeSet
import com.mybase.libb.R


abstract class BaseItemRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseRefreshView(context, attrs, defStyleAttr) {


    override fun getLayoutId() = R.layout.include_lib_brv_refresh_pull_item

    init {

        var layoutId = -1

        var gridSize = 0
        var vertical = true
        var scroll = true

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

                // 是否可以滑动
                scroll = attributes.getBoolean(
                    R.styleable.BaseItemRecyclerView_layout_scroll_enable,
                    scroll
                )
            } finally {
                attributes.recycle()
            }
        }

        if (rv is BaseItemRecyclerView) {
            (rv as BaseItemRecyclerView).init(layoutId, gridSize, vertical, scroll)
        }


    }


}