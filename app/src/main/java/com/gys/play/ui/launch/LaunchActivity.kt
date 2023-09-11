package com.gys.play.ui.launch

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.context.AppContext
import com.android.liba.ui.base.BaseActivity
import com.android.liba.ui.dialog.AgreementPrivacyListener
import com.android.liba.ui.dialog.DialogButtonListener
import com.android.liba.util.UIHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gys.play.*
import com.gys.play.coroutines.Quest
import post
import com.gys.play.ui.LoginActivity
import com.gys.play.util.MulLanguageUtil
import com.gys.play.util.PushUtil
import com.gys.play.util.dialog.AgainAgreementDialog
import com.gys.play.util.dialog.AgreementDialog
import com.gys.play.viewmodel.LaunchViewModel

class LaunchActivity : BaseActivity<LaunchViewModel<LaunchActivity>>() {
    override fun getLayoutId() = R.layout.activity_launch

    override fun initView(savedInstanceState: Bundle?, fragmentView: View?) {
        UIHelper.showLog("启动", "LaunchActivity 1")
        val isFirst = AppContext.getMMKV().decodeBool(Config.IS_FIRST, true)
        if (isFirst) {
            UIHelper.showLog("启动", "LaunchActivity 2")
            AgreementDialog.show(this, object : AgreementPrivacyListener {
                override fun onSure() {
                    UIHelper.showLog("启动", "LaunchActivity 3")
                    AppContext.getMMKV().encode(Config.IS_FIRST, false)
                    getAppConfigAndNextToNextPage(true)
                }

                override fun onCancel() {
                    AgainAgreementDialog.show(this@LaunchActivity,
                        object : DialogButtonListener {
                            override fun onSure() {
                            }

                            override fun onCancel() {
                                moveTaskToBack(true)
                            }
                        })
                }

                override fun onContent(p0: RecyclerView?) {
                }

                override fun onAgreement() {
                    Config.toUserRulePage(this@LaunchActivity)
                }

                override fun onPrivacy() {
                    Config.toPrivacyPage(this@LaunchActivity)
                }
            })
        } else {
            getAppConfigAndNextToNextPage(false)
        }
    }

    private fun getAppConfigAndNextToNextPage(isFirst: Boolean) {

        if (SpServiceConfig.isConfigPrepared()) {

            toNextPage(isFirst)

            post {
                val data =
                    Quest.api.getAppStartConfigSSS(Quest.getHead(Config.APP_START_CONFIG))
                SpServiceConfig.save(data)
            }
        } else {
            toNextPage(isFirst)
            post {
                val data =
                    Quest.api.getAppStartConfigSSS(Quest.getHead(Config.APP_START_CONFIG))
                SpServiceConfig.save(data)

            }
        }
    }

    private fun toNextPage(isFirst: Boolean) {
        MulLanguageUtil.checkSetLanguage(mActivity)

        if (isFirst) {
            MyApplication.getAnalytics().setFirst("首次启动app登录页面曝光量")
            startActivity(
                Intent(this@LaunchActivity, LoginActivity::class.java)
                    .putExtra(Config.IS_LAUNCH_GO, true)
            )
        } else {
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
            PushUtil.checkPushIntent(mActivity, intent)
        }

        finish()
    }

    override fun loadData(savedInstanceState: Bundle?) {
    }

    override fun setListener() {
    }

    //检查是否已经登录
    fun isLogin(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val locale = newConfig.locale
        Log.d(
            "mulLanguage",
            "LaunchActivity onConfigurationChanged = " + locale.language + "," + locale.country
        )
    }
}