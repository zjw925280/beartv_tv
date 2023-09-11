package com.gys.play.ui.video

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.gys.play.R
import com.gys.play.ui.VIPActivity
import com.mybase.libb.ext.setNoDouble
import razerdp.basepopup.BasePopupWindow

class VipHintPop(context: Context) : BasePopupWindow(context) {

    init {
        popupGravity = Gravity.CENTER

        contentView = createPopupById(R.layout.pop_video_vip_hint)

        setOutSideDismiss(false)

        setBackPressEnable(false)

        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)

        contentView.findViewById<View?>(R.id.tvExit)?.setOnClickListener {
            context.finish()
        }

        contentView.findViewById<View>(R.id.goVip).setNoDouble{
            VIPActivity.start(context)
        }

    }


}