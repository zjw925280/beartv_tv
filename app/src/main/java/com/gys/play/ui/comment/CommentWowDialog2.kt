package com.gys.play.ui.comment

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.gys.play.R
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ui.dialog.BaseBottomDialog

class CommentWowDialog2(
    private val mActivity: FragmentActivity,
    private val type: Int,
    private val resource_id: String,
    private val comment_id: String,
    private val order: String,
) :
    BaseBottomDialog(mActivity, R.style.TransBottomSheetDialogStyle) {
    override val layoutId: Int
        get() = R.layout.dialog_comment_wow

    companion object {
        //order 0 默认 1热度
        fun newInstance(
            activity: FragmentActivity,
            type: Int,
            resource_id: String,
            comment_id: String,
            order: String = "0"
        ) = CommentWowDialog2(activity, type, resource_id, comment_id, order)
    }

    init {
        mView.findViewById<View>(R.id.ivClose).setNoDouble {
            dismiss()
        }

       /* mView.findViewById<ViewPager2>(R.id.vp).let {
            it.adapter = object : FragmentStateAdapter(mActivity) {
                override fun getItemCount() = 1
                override fun createFragment(position: Int) =
                    CommentListFragment.newInstance(type, resource_id, comment_id, order).also {
                        it.onTopListener = {
                            behavior.setHideable(it)
                        }
                    }
            }
            it.isUserInputEnabled = false
        }
*/
        /*     FragmentUtils.replace(
                 mActivity.supportFragmentManager,
                 CommentListFragment.newInstance(type, resource_id, comment_id, order).also {
                     it.onTopListener = {
                         behavior.setHideable(it)
                     }
                 },
                 R.id.fl
             )*/


    }

    override fun initView(view: View) {

    }
}