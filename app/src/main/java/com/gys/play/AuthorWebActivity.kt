package com.gys.play

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.android.liba.util.WebUtil
import com.atlight.novel.util.web.ZZJsInterface
import com.gys.play.databinding.ActivityAuthorWebBinding
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.util.FileUtils
import com.gys.play.util.MediaUtility
import com.gys.play.util.OpenFileWebChromeClient
import com.gys.play.viewmodel.NovelBasePresent
import java.io.File

const val IS_SHOW_TOP = "IS_SHOW_TOP"

class AuthorWebActivity :
    NovelBaseActivity<NovelBasePresent<AuthorWebActivity>, ActivityAuthorWebBinding>(R.layout.activity_author_web) {
    companion object {
        fun start(context: Context?, title: String?, url: String, isShowTop: Boolean = false) {
            context?.run {
                val intent = Intent(this, AuthorWebActivity::class.java)
                    .putExtra(Config.WEB_TITLE, title)
                    .putExtra(Config.WEB_URL, url)
                    .putExtra(IS_SHOW_TOP, isShowTop)
                startActivity(intent)
            }
        }
    }

    private val mWebUtil by lazy { WebUtil() }

    var mOpenFileWebChromeClient = OpenFileWebChromeClient(this)
    override fun initView() {
        val webTitle = intent.getStringExtra(Config.WEB_TITLE)
        val showTop = intent.getBooleanExtra(IS_SHOW_TOP, false)
        val webUrl = intent.getStringExtra(Config.WEB_URL)
        mWebUtil.init(this, binding.wbView, webUrl, binding.pbProgress, false)
        mWebUtil.doWebSet()
//        webUrl?.let { binding.wbView.loadUrl(it) }
        if (showTop) {
            binding.topView.visibility = View.GONE
            webTitle?.let {
                setTitleText(it)
            }
        } else {
            binding.topLayout.visibility = View.GONE
        }
        binding.wbView.webChromeClient = mOpenFileWebChromeClient
        binding.wbView.addJavascriptInterface(
            ZZJsInterface(this, binding.wbView, mWebUtil),
            "ZZJsInterface"
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER) {
            if (mOpenFileWebChromeClient.mFilePathCallback != null) {
                val result: Uri? =
                    if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                if (result != null) {
                    val path: String = MediaUtility.getPath(
                        applicationContext,
                        result
                    )
                    val uri = Uri.fromFile(File(path))
                    mOpenFileWebChromeClient.mFilePathCallback
                        .onReceiveValue(uri)
                } else {
                    mOpenFileWebChromeClient.mFilePathCallback
                        .onReceiveValue(null)
                }
            }
            if (mOpenFileWebChromeClient.mFilePathCallbacks != null) {
                val result: Uri? =
                    if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                if (result != null) {
//                    val path: String = MediaUtility.getPath(
//                        applicationContext,
//                        result
//                    )
//                    val uri = Uri.fromFile(File(path))
//                    mOpenFileWebChromeClient.mFilePathCallbacks
//                        .onReceiveValue(arrayOf(uri))

                    val file = FileUtils.uriToFileApiQ(this, result)
                    file?.let {
                        val uri = Uri.fromFile(file)
                        mOpenFileWebChromeClient.mFilePathCallbacks
                            .onReceiveValue(arrayOf(uri))
                    }

                } else {
                    mOpenFileWebChromeClient.mFilePathCallbacks
                        .onReceiveValue(null)
                }
            }
            mOpenFileWebChromeClient.mFilePathCallback = null
            mOpenFileWebChromeClient.mFilePathCallbacks = null
        }
    }


    override fun loadData(bundle: Bundle?) {
    }

    override fun setListener() {

    }

    override fun initData() {
    }
}