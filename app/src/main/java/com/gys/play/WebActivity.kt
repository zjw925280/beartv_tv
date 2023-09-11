package com.gys.play

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.liba.util.WebUtil
import com.atlight.novel.util.web.ZZJsInterface
import com.gys.play.databinding.ActivityWebBinding
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.viewmodel.NovelBasePresent

class WebActivity :
    NovelBaseActivity<NovelBasePresent<WebActivity>, ActivityWebBinding>(R.layout.activity_web) {

    companion object {
        fun start(context: Context?, title: String?, url: String?) {
            context?.run {
                val intent = Intent(this, WebActivity::class.java)
                    .putExtra(Config.WEB_TITLE, title)
                    .putExtra(Config.WEB_URL, url)
                startActivity(intent)
            }
        }
    }

    private val mWebUtil by lazy { WebUtil() }

    override fun initView() {
        val webTitle = intent.getStringExtra(Config.WEB_TITLE)
        val webUrl = intent.getStringExtra(Config.WEB_URL)
        binding.tvWebTitle.text = webTitle
        mWebUtil.init(this, binding.wbView, webUrl, binding.pbProgress, false)
        mWebUtil.doWebSet()

        binding.wbView.addJavascriptInterface(
            ZZJsInterface(this, binding.wbView, mWebUtil),
            "ZZJsInterface"
        )
    }

    override fun loadData(bundle: Bundle?) {
    }

    override fun setListener() {
        binding.rlBack.setOnClickListener {
            mWebUtil.backWeb()
        }
    }

    override fun initData() {
    }
}