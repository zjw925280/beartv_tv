package com.gys.play.util.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.liba.context.AppContext
import com.android.liba.ui.base.BaseActivity
import com.android.liba.util.LifeUtil
import com.android.liba.util.UIHelper
import com.android.liba.util.jdownload.DownloadManager
import com.android.liba.util.jdownload.SimpleDownloadFileListen
import com.gys.play.BuildConfig
import com.gys.play.R
import com.gys.play.util.InstallUtil
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView

class UpdateDialog(
    context: Context, val rescissible: Boolean, var text: String?,
    val update_url: String?, val update_type: Int, val update_packagename: String?,
    val google_update_version_code: String?
) :
    JustDialog(context) {

    init {
        addView(R.layout.update_dialog_layout).setWidth(1f).cancelable(rescissible)
            .cancelOnTouchOutside(rescissible)
        getContentView()
        if (BuildConfig.DEBUG) {
            setCanceledOnTouchOutside(true)
        }
    }

    fun getContentView(): View {
        val tvLeft = view.findViewById<TextView>(R.id.tv_left)
        val tvRight = view.findViewById<TextView>(R.id.tv_right)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        view.findViewById<TextView>(R.id.text2).text = google_update_version_code
        if (!rescissible) {
            tvLeft.visibility = View.GONE
        }
        tvLeft.setOnClickListener {
            dismiss()
        }
        text?.run {
            tvContent?.text = this
        }
        tvRight.setOnClickListener {
            when (update_type) {
                0 -> {
                    if (update_packagename.isNullOrEmpty()) {
                        transferToGooglePlay(context)
                    } else {
                        transferToGooglePlay(context, appPackageName = update_packagename)
                    }
                    if (!rescissible) {
                        System.exit(0)
                    } else {
                        dismiss()
                    }
                }
                1 -> {
                    DownloadManager.instance().startDownloadApkAndInstall(
                        context,
                        update_url,
                        object : SimpleDownloadFileListen() {
                            override fun onFail(error: String?) {
                                super.onFail(error)
                                AppContext.showToast(context.getString(R.string.download_fail))
                            }

                            override fun onLoading(loadedLength: Long, progress: Float) {
                                super.onLoading(loadedLength, progress)
                                tvRight.text = "$progress%"
                            }

                            override fun onFinish(filePath: String?) {
                                super.onFinish(filePath)
                                tvRight.text = context.getString(R.string.click_to_install)
                                if (InstallUtil.checkInstallPermission(context)) {
                                    InstallUtil.installAPK(
                                        filePath,
                                        context,
                                        context.packageName + ".fileProvider"
                                    )
                                } else {
                                    if (context is ComponentActivity) {
                                        LifeUtil.addListener(
                                            context as ComponentActivity,
                                            object : LifeUtil.OnSimpleLifeListener() {
                                                override fun onResume() {
                                                    super.onResume()
                                                    if (InstallUtil.isHasInstallPermission(context)) {
                                                        InstallUtil.installAPK(
                                                            filePath,
                                                            context,
                                                            context.packageName + ".fileProvider"
                                                        )
                                                    }
                                                    LifeUtil.removeListener(
                                                        (context as ComponentActivity).lifecycle,
                                                        this
                                                    )
                                                }
                                            })
                                    }
                                }
                            }
                        })
                }
                2 -> {
                    UIHelper.openWebBrowser(context, update_url)
                    if (!rescissible) {
                        System.exit(0)
                    } else {
                        dismiss()
                    }
                }
            }
        }
        return view
    }

    private fun transferToGooglePlay(
        context: Context,
        googlePlay: String = "com.android.vending",
        appPackageName: String = context.packageName
    ) {
        try {
            val uri = Uri.parse("market://details?id=$appPackageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage(googlePlay)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            BaseActivity.showToast(e.message)
        }
    }

    companion object {
        fun show(
            context: Context,
            rescissible: Boolean,
            text: String?,
            update_url: String?, update_type: Int,
            update_packagename: String?,
            google_update_version_code: String?
        ): UpdateDialog {
            val dialog = UpdateDialog(
                context,
                rescissible,
                text,
                update_url,
                update_type,
                update_packagename,
                google_update_version_code
            )
            dialog.show()
            return dialog
        }
    }
}