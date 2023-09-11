package com.gys.play.util.dialog

import androidx.fragment.app.FragmentActivity
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.gys.play.R

class WaitingDialog(
    val activity: FragmentActivity, private val cancelAble: Boolean = true
) :
    JustDialog(activity) {

    init {
        addView(R.layout.dialog_waiting)
        setCancelableFor(cancelAble)
        setOnDismissListener {
            onDismiss()
        }
    }

    fun setCancelableFor(cancelAble: Boolean) {
        cancelable(cancelAble)
        cancelOnTouchOutside(cancelAble)
    }


    private fun onDismiss() {
        remove(activity)
    }

    companion object {
        private val map = mutableMapOf<FragmentActivity, WaitingDialog>()

        fun show(activity: FragmentActivity, cancelAble: Boolean = true) {
            val dialog: WaitingDialog
            if (map.containsKey(activity)) {
                dialog = map[activity]!!
                dialog.setCancelableFor(cancelAble)
            } else {
                dialog = WaitingDialog(activity, cancelAble)
            }
            dialog.show()
            map[activity] = dialog
        }

        fun hide(activity: FragmentActivity) {
            if (!map.containsKey(activity)) {
                return
            }
            val waitingDialog = map[activity] ?: return
            waitingDialog.dismiss()
            remove(activity)
        }

        fun remove(activity: FragmentActivity) {
            map.remove(activity)
        }
    }
}