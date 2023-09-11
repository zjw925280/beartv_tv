package com.gys.play.ui.comment

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.FragmentUtils
import com.gys.play.R
import com.gys.play.databinding.ActivityCommentBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.video.CommentVideoFragment
import com.jccppp.start.launchAc
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ext.statusBar
import com.mybase.libb.ui.vb.BaseVbActivity

class CommentActivity : BaseVbActivity<ActivityCommentBinding>() {

    private val viewModel by viewModels<EditCommentViewModel>()

    companion object {
        fun start(context: FragmentActivity) {
            if (!SpUserInfo.isLogin()) {
                context.launchAc<LoginActivity>()
            } else {
                context.launchAc<CommentActivity>()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        statusBar(hasImmersive = false, darkFont = true)

        FragmentUtils.replace(
            supportFragmentManager,
            CommentVideoFragment.newMyInstance(),
            R.id.fl
        )

        mBind.itemTopRight.setOnClickListener {
            val value = viewModel.editSwitch.value ?: false
            viewModel.editSwitch.value = !value
        }
        mBind.all.setNoDouble {
            viewModel.allSelected.postValue(true)
        }

        mBind.del.setNoDouble {
            viewModel.startDelete.postValue(true)
        }

    }

    override fun observeUi(owner: LifecycleOwner) {
        super.observeUi(owner)
        viewModel.editTxt.observe(owner) {
            mBind.itemTopRight.text = it

            if (it == "取消") {
                mBind.bottomLayout.visibility = View.VISIBLE
            } else {
                mBind.bottomLayout.visibility = View.GONE
            }
        }

    }

}
