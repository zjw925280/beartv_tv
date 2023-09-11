package com.gys.play.util.dialog

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.liba.context.AppContext
import com.android.liba.jk.OnSimpleListener
import com.android.liba.ui.base.listgroup.holder.RepeatClickListener
import com.android.liba.ui.dialog.AgreementPrivacyListener
import com.android.liba.ui.dialog.BaseCenterDialog
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R

class AgreementDialog(val activity: FragmentActivity, listener: AgreementPrivacyListener?) :
    BaseCenterDialog(activity) {

    private var listener: AgreementPrivacyListener? = null
    private val keyColor = MyApplication.getInstance().resources.getColor(R.color.key_color)

    init {
        this.listener = listener
    }

    companion object {
        fun show(activity: FragmentActivity, listener: AgreementPrivacyListener?): AgreementDialog {
            val dialog = AgreementDialog(activity, listener)
            dialog.setCancelable(false)
            dialog.show()
            return dialog
        }
    }

    override fun getContentView(): View {
        val view = layoutInflater.inflate(R.layout.dialog_agreement, null)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        handleText(tvContent)

        val checkBox = view.findViewById<CheckBox>(R.id.check)
        val checkText = view.findViewById<TextView>(R.id.checkText)
        handleCheckText(checkText)

        view.findViewById<TextView>(R.id.tvSure).setOnClickListener {
            if (!checkBox.isChecked) {
                AppContext.showToast(
                    MyApplication.getInstance().getActivityResources().getString(R.string.agree_tip)
                )
            } else {
                if (listener != null) {
                    listener!!.onSure()
                    dismiss()
                }
            }
        }
        view.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            if (listener != null) {
                listener!!.onCancel()
            }
        }
        return view
    }

    private fun handleText(tvContent: TextView) {
        val welcome = context.getString(R.string.agreement_welcome)
        val one = context.getString(R.string.agreement_content_one)
        val userRule = context.getString(R.string.agreement_content_two)
        val ji = context.getString(R.string.agreement_content_three)
        val dun = context.getString(R.string.agreement_content_dun)
        val activityCommonRule = context.getString(R.string.activity_common_rule_)
        val accountServiceRule = context.getString(R.string.account_service_rule_)
        val ugcRule = context.getString(R.string.ugc_rule_)
        val softRule = context.getString(R.string.soft_rule_)
        val privacy = context.getString(R.string.agreement_content_four)
        val five = context.getString(R.string.agreement_content_five)
        val agreement =
            StringBuffer(welcome).append(one).append(userRule).append(dun).append(privacy)
                .append(dun).append(activityCommonRule)
                .append(dun).append(accountServiceRule)
                .append(dun).append(ugcRule)
                .append(ji).append(softRule)
                .append(five)
                .toString()
        val builder = SpannableStringBuilder(agreement)

        addClickKeySpan(builder, userRule) { Config.toUserRulePage(activity) }
        addClickKeySpan(builder, privacy) { Config.toPrivacyPage(activity) }
        addClickKeySpan(builder, activityCommonRule) { Config.toActivityCommonPage(activity) }
        addClickKeySpan(builder, accountServiceRule) { Config.toAccountServiceRulePage(activity) }
        addClickKeySpan(builder, ugcRule) { Config.toUGCRulePage(activity) }
        addClickKeySpan(builder, softRule) { Config.toSoftRule(activity) }

        tvContent.setMovementMethod(LinkMovementMethod.getInstance())
        tvContent.setText(builder)
    }

    private fun handleCheckText(checkText: TextView) {
        val one = context.getString(R.string.check_agree_one)
        val userRule = context.getString(R.string.agreement_content_two)
        val ji = context.getString(R.string.check_agree_three)
        val dun = context.getString(R.string.agreement_content_dun)
        val privacy = context.getString(R.string.agreement_content_four)
        val activityRule = context.getString(R.string.activity_common_rule_)
        val accountRule = context.getString(R.string.account_service_rule_)
        val ugcRule = context.getString(R.string.ugc_rule_)
        val softRule = context.getString(R.string.soft_rule_)
        val agreement = StringBuffer(one).append(userRule)
            .append(dun).append(privacy)
            .append(dun).append(activityRule)
            .append(dun).append(accountRule)
            .append(dun).append(ugcRule)
            .append(ji).append(softRule)
            .toString()
        val builder = SpannableStringBuilder(agreement)


        addClickKeySpan(builder, userRule) { Config.toUserRulePage(activity) }
        addClickKeySpan(builder, privacy) { Config.toPrivacyPage(activity) }
        addClickKeySpan(builder, activityRule) { Config.toActivityCommonPage(activity) }
        addClickKeySpan(builder, accountRule) { Config.toAccountServiceRulePage(activity) }
        addClickKeySpan(builder, ugcRule) { Config.toUGCRulePage(activity) }
        addClickKeySpan(builder, softRule) { Config.toSoftRule(activity) }

        checkText.setMovementMethod(LinkMovementMethod.getInstance())
        checkText.setText(builder)
    }

    private fun addClickKeySpan(
        builder: SpannableStringBuilder,
        key: String,
        onClickListener: OnSimpleListener
    ) {
        val start = builder.indexOf(key)
        val end = start + key.length
        builder.setSpan(object : RepeatClickListener() {
            override fun notRepeatClick(v: View) {
                onClickListener.onListen()
            }
        }, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        builder.setSpan(
            ForegroundColorSpan(keyColor),
            start,
            end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
}