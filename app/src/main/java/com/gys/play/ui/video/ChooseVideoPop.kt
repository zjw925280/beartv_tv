package com.gys.play.ui.video

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.linear
import com.gys.play.R
import razerdp.basepopup.BasePopupWindow

class ChooseVideoPop(context: Context) : BasePopupWindow(context) {
    init {
        popupGravity = Gravity.RIGHT

        contentView = createPopupById(R.layout.pop_video_choose)
    }

    private lateinit var rv: RecyclerView

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)

        rv =
            findViewById<RecyclerView>(R.id.rvAnthology).linear(orientation = RecyclerView.VERTICAL)

    }

    fun show(adapter: ChooseVideoAdapter) {
        rv.adapter = adapter

        if (adapter.playIndex >= 0)
            rv.postDelayed({
                rv.scrollToPosition(adapter.playIndex)
            }, 200)

        if (!isShowing)
            showPopupWindow()
    }
}