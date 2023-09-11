package com.gys.play.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.android.liba.ui.dialog.DialogButtonListener
import com.android.liba.util.AppUtil
import com.android.liba.util.UIHelper
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.SpServiceConfig
import com.gys.play.databinding.ActivitySettingBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.TextItem
import com.gys.play.entity.TextItemClickListener
import com.gys.play.ui.launch.LaunchActivity
import com.gys.play.util.GlideUtils
import com.gys.play.util.KeyTool
import com.gys.play.util.MulLanguageUtil
import com.gys.play.util.dialog.BottomDialog
import com.gys.play.util.dialog.RemoveAccountDialog
import com.gys.play.viewmodel.UserViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingActivity :
    NovelBaseActivity<UserViewModel<SettingActivity>, ActivitySettingBinding>(R.layout.activity_setting) {
    override fun initView() {
        setTitleText(getString(R.string.settings))
        if (SpServiceConfig.getServiceConfig().isCloseLanguageSwitch()) {
            binding.language.visibility = View.GONE
        }
        binding.version.text = AppUtil.getVersionName(this)
        findViewById<View>(R.id.itemTopTitle)?.setOnLongClickListener {
            KeyTool.logAll(this)
            true
        }
    }

    override fun initData() {
        val userInfo = SpUserInfo.getUserInfo()
        if (userInfo.id.isEmpty()) {
//            binding.llLoginOut.visibility = View.GONE
            binding.userInfo.visibility = View.GONE
            binding.accountSafe.visibility = View.GONE
//            binding.viewUserinfo.visibility = View.GONE
//            binding.viewAccountSafe.visibility = View.GONE
        } else {
//            binding.llLoginOut.visibility = View.VISIBLE
            binding.userInfo.visibility = View.VISIBLE
            binding.accountSafe.visibility = View.VISIBLE
//            binding.viewUserinfo.visibility = View.VISIBLE
//            binding.viewAccountSafe.visibility = View.VISIBLE
        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
    }

    private fun initCacheText() {
        binding.cacheSize.text = GlideUtils.getGlidCacheSize(activity)
    }

    override fun onResume() {
        super.onResume()
        initCacheText()
    }

    override fun setListener() {
        binding.signOut.setOnClickListener {
            try {
                binding.signOut.visibility = View.GONE
                binding.delAccount.visibility = View.GONE
                MyApplication.getInstance().dataViewModel.isRefreashBookShelf.value = true
                SpUserInfo.clear()
                AuthUI.getInstance().signOut(activity)
                finish()
            } catch (e: Throwable) {
                UIHelper.showLog("Http", "403 signOut Throwable $e")
            }
        }
        binding.delAccount.setOnClickListener {
            RemoveAccountDialog.show(this, object : DialogButtonListener {
                override fun onSure() {
                    AuthUI.getInstance().delete(this@SettingActivity).addOnCompleteListener {
                        Firebase.auth.signOut()
                        presenter.getUserCancel { _, _ ->
                            MyApplication.getInstance().dataViewModel.isRefreashBookShelf.value =
                                true
                            SpUserInfo.clear()
                            binding.signOut.visibility = View.GONE
                            binding.delAccount.visibility = View.GONE
                            finish()
                        }
                    }
                }

                override fun onCancel() {

                }

            })
        }
        binding.userAgreement.setOnClickListener {
            Config.toUserRulePage(this)
        }
        binding.clearCache.setOnClickListener {
            GlobalScope.launch {
                Glide.get(MyApplication.getInstance()).clearDiskCache()
                activity?.runOnUiThread {
                    binding.cacheSize.text = "0kb"
                    val toast: Toast =
                        Toast.makeText(
                            MyApplication.getInstance(),
                            getString(R.string.clear_cache_done),
                            Toast.LENGTH_SHORT
                        )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
            }
        }
        binding.privacyPolicy.setOnClickListener {
            Config.toPrivacyPage(this)
        }
        binding.activityCommonRule.setOnClickListener {
            Config.toActivityCommonPage(this)
        }
        binding.accountServiceRule.setOnClickListener {
            Config.toAccountServiceRulePage(this)
        }
        binding.ugcRule.setOnClickListener {
            Config.toUGCRulePage(this)
        }
        binding.softRule.setOnClickListener {
            Config.toSoftRule(this)
        }
        binding.accountSafe.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }
        binding.language.setOnClickListener {
            changeAppLanguage()
        }
        binding.userInfo.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
        }
        binding.newVersionTip.visibility =
            if (SpServiceConfig.isNeedUpdate()) View.VISIBLE else View.GONE
        binding.update.setOnClickListener {
            val serviceConfig = SpServiceConfig.getServiceConfig()
            serviceConfig.showUpdateDialog(mActivity, true)
        }
    }

    var bottomDialog: BottomDialog? = null
    private fun changeAppLanguage() {
        val list = mutableListOf<TextItem>()
        list.add(TextItem("繁体中文", clickListener, "zh", value = "HK"))
        list.add(TextItem("Tiếng Việt", clickListener, "vi"))//越南语
//        list.add(TextItem("ภาษาไทย", clickListener, "th"))//泰语
        list.add(TextItem("English", clickListener, "en"))
        list.add(TextItem(getString(R.string.cancel), clickListener, "", R.color.gray_9f9f9f))
        bottomDialog = BottomDialog(this, list)
        bottomDialog?.show()
    }

    private val clickListener = object : TextItemClickListener {
        override fun click(item: TextItem) {
            bottomDialog?.dismiss()
            if (item.tag.isEmpty()) {
                return
            }
            MulLanguageUtil.setAppLanguage(mActivity, item.tag, item.value)

            val intent = Intent(activity, LaunchActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val locale = newConfig.locale
        Log.d(
            "mulLanguage",
            "SettingActivity onConfigurationChanged = " + locale.language + "," + locale.country
        )
    }

}