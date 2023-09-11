package com.gys.play.ui.comment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.FragmentUtils
import com.gys.play.R
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ui.dialog.BaseBottomDialogFragment

class CommentWowDialog : BaseBottomDialogFragment() {


    companion object {
        //order 0 默认 1热度
        fun newInstance(type: Int, resource_id: String, comment_id: String, order: String = "0") =
            CommentWowDialog().setBundle {
                it["type"] = type
                it["resource_id"] = resource_id
                it["comment_id"] = comment_id
                it["order"] = order
            }
    }

    private val resource_id by argument("")
    private val comment_id by argument("")
    private val order by argument("0")
    private val type by argument(1)
    override val layoutId: Int
        get() = R.layout.dialog_comment_wow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransBottomSheetDialogStyle)

    }


    override fun initView(savedInstanceState: Bundle?) {

        (mView.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        findViewById<View>(R.id.ivClose).setNoDouble {
            dismiss()
        }

        FragmentUtils.replace(
            childFragmentManager,
            CommentListFragment.newInstance(type, resource_id, comment_id, order).also {
                it.onTopListener = {
                    behavior?.setHideable(it)
                }
            },
            R.id.fl
        )
    }

}