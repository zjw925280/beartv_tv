package com.gys.play.ui.comment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.entity.WowDetailBean
import com.gys.play.util.FileUtils
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.QRCodeUtil
import com.gys.play.util.saveToAlbum
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ui.dialog.BaseBottomDialogFragment
import post
import java.io.File
import kotlin.random.Random


class ShareWowDialog : BaseBottomDialogFragment() {

    companion object {
        private const val TAG = "ShareWowDialog"

        //order 0 默认 1热度
        fun newInstance(model: WowDetailBean) =
            ShareWowDialog().setBundle {
                it["model"] = model
            }
    }

    override val layoutId: Int
        get() = R.layout.dialog_share

    private val model: WowDetailBean by argument()

    private var downLoadPath = ""

    private var fileExtension = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TransBottomSheetDialogStyle)

    }

    override fun initView(savedInstanceState: Bundle?) {
        if (model.url.isNullOrEmpty()) {
            dismiss()
            return
        }

        val fileExtension = com.blankj.utilcode.util.FileUtils.getFileExtension(model.url)
        if (checkVideoPath(fileExtension)) {
            downLoadPath =
                "${PathUtils.getInternalAppCachePath()}/video_download/${model.title}_${model.id}"
            this.fileExtension = fileExtension
        } else {
            downLoadPath =
                "${PathUtils.getInternalAppCachePath()}/video_download/${model.title}_${model.id}"
            this.fileExtension = "mp4"
        }

        (mView.parent as? View)?.setBackgroundColor(Color.TRANSPARENT)

        findViewById<View>(R.id.ivClose).setNoDouble {
            dismiss()
        }
        findViewById<View>(R.id.close).setNoDouble {
            dismiss()
        }
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        findViewById<View>(R.id.appShare).setNoDouble {
            findViewById<TextView>(R.id.topText).text = "長按下載QR碼"
            findViewById<View>(R.id.llQr).visibility = View.VISIBLE
            findViewById<View>(R.id.llShare).visibility = View.GONE
        }
        findViewById<View>(R.id.videoShare).setNoDouble {

            if (!com.blankj.utilcode.util.FileUtils.isFileExists("${downLoadPath}.$fileExtension")) {
                post {
                    Quest.downLoad(model.url, downLoadPath) {
                        println("1111111111111  ${it.progress}")
                        progressBar.progress = it.progress
                    }.await()

                    com.blankj.utilcode.util.FileUtils.rename(
                        downLoadPath,
                        "${com.blankj.utilcode.util.FileUtils.getFileNameNoExtension(downLoadPath)}.$fileExtension"
                    )

                    if (com.blankj.utilcode.util.FileUtils.isFileExists("${downLoadPath}.$fileExtension")) {
                        val file = File("${downLoadPath}.$fileExtension")
                        FileUtils.share(file, context)
                        dismiss()
                    }

                }
            } else {
                val file = File("${downLoadPath}.$fileExtension")
                FileUtils.share(file, context)
                dismiss()
            }

            findViewById<View>(R.id.llShare).visibility = View.GONE
            findViewById<View>(R.id.llDownload).visibility = View.VISIBLE

        }
        findViewById<TextView>(R.id.name).text = model.title

        findViewById<ImageView>(R.id.image).let {
            ImageLoaderUtils.loadImageCenterCropWithRadius(it, model.url, radius = 6)
        }

        QRCodeUtil.createQRCodeBitmap(
            "https://play.google.com/store/apps/details?id=" + MyApplication.getInstance().packageName,
            480,
            480
        )?.let { bitmap ->
            findViewById<ImageView>(R.id.qrCode).let {
                it.setImageBitmap(bitmap)
                it.setOnLongClickListener {
                    bitmap.saveToAlbum(
                        it.context,
                        "${Random.nextInt(4123123)}_${System.currentTimeMillis()}.jpg"
                    )
                    ToastUtils.showLong("保存成功")
                    dismiss()
                    return@setOnLongClickListener false
                }
            }
        }
    }

    fun checkVideoPath(a: String?): Boolean {
        when (a?.lowercase()) {
            "mp4" -> return true
        }

        return false
    }

    override fun dismiss() {
        super.dismiss()
//       下载关闭
        Log.d(TAG, "dismiss() called")
    }
}