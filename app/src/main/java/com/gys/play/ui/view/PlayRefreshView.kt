package com.gys.play.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import com.mybase.libb.ext.getLifeActivity
import com.mybase.libb.ext.notEmpty
import post
import kotlinx.coroutines.CoroutineScope

class PlayRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : com.mybase.libb.ui.rv.BaseItemRefreshView(context, attrs, defStyleAttr) {

    private var await: (suspend CoroutineScope.(page: Int) -> List<Any?>)? = null

    private var lifecycleOwner: LifecycleOwner? = null

    fun config(
        await: (suspend CoroutineScope.(page: Int) -> List<Any?>),
        lifecycleOwner: LifecycleOwner?,
    ) = apply {
        this.await = await
        this.lifecycleOwner = lifecycleOwner

    }

    fun config(
        await: (suspend CoroutineScope.(page: Int) -> List<Any?>)
    ) = apply {
        config(await, null)
    }

    private var reStartTag = ""

    override fun load() {
        if (await == null) throw RuntimeException("请先调用  config")
        (lifecycleOwner ?: context?.getLifeActivity())?.post(onError = {
            if (reStartTag.notEmpty()) {
                prl.showError()
            } else {
                if (!prl.loaded) prl.showError() else {
                    prl.finish(false)
                }
            }

        }) {
            val list = await!!.invoke(this, page)
            reStartTag = ""
            setData(list, list.size < limit)

        }
    }


    fun onRestart() {
        reStartTag = "restart"
        start()
    }
}