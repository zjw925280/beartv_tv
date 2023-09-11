package com.mybase.libb.ui.web

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.FragmentUtils
import com.jccppp.start.argument
import com.jccppp.start.launchAc
import com.mybase.libb.R
import com.mybase.libb.ext.notEmpty
import com.mybase.libb.ui.BaseActivity


open class WebViewActivity(layoutId: Int = R.layout.include_lib_framelayout) :
    BaseActivity(layoutId) {


    companion object {
        fun newInstance(activity: FragmentActivity, title: String?, url: String) {
            if (url.notEmpty())
                activity.launchAc<WebViewActivity>("title" to title, "url" to url)
        }
    }

    private val title by argument("")

    private val url by argument("")

    private val enableState by argument(true)

    private lateinit var mWebView: WebViewFragment

    override fun initView(savedInstanceState: Bundle?) {

        mWebView = customWebViewFragment(title, url)

        FragmentUtils.replace(supportFragmentManager, mWebView, R.id.lib_framelayout)

    }

   open fun customWebViewFragment(title:String,url:String) = WebViewFragment.newInstance(title, url)

}