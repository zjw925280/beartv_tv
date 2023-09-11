package com.gys.play.ui.comment

import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.android.liba.ui.base.BaseFragment
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.DialogSendCommentBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.ui.LoginActivity
import com.jccppp.start.launchAc
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ext.showToast
import post
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig

class SendCommentDialog(val context: Fragment, val type: Int, val onReply: () -> Unit) :
    BasePopupWindow(context) {

    init {
        contentView = createPopupById(R.layout.dialog_send_comment)
//        setBackgroundColor(Color.TRANSPARENT)
    }

    private lateinit var mBinding: DialogSendCommentBinding

    private val keyboardFlag = FLAG_KEYBOARD_ALIGN_TO_ROOT or FLAG_KEYBOARD_ANIMATE_ALIGN


    override fun onViewCreated(contentView: View) {
        mBinding = DialogSendCommentBinding.bind(contentView)
    }

    fun show(id1: String, id2: String, hint: String) {

        if (!SpUserInfo.isLogin()) {
            context.launchAc<LoginActivity>()
            return
        }

        mBinding.etComment.hint = hint
        setKeyboardAdaptive(true)
            .setAutoShowKeyboard(mBinding.etComment, true)
            .setKeyboardGravity(Gravity.BOTTOM)
            .setKeyboardAdaptionMode(mBinding.etComment, keyboardFlag)
            .showPopupWindow()

        mBinding.send.setNoDouble {
            val text = mBinding.etComment.text.toString()

            when {
                text.trim().length < 3 -> {
                    showToast("評論至少要3個字")
                }
                text.trim().length >= 256 -> {
                    BaseFragment.showToast("評論超過256個字")
                }
                else -> {
                    context.post(showLoading = true) {
                        Quest.api.addComment(Quest.getHead(Config.ADD_COMMENT) {
                            add("type", "$type")
                            add("resource_id", id1).add("comment_id", id2).add("content", text)
                        })
                        mBinding.etComment.setText("")
                        onReply.invoke()
                        dismiss()
                    }
                }
            }

        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.FROM_BOTTOM)
            .toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.TO_BOTTOM)
            .toDismiss()
    }
}