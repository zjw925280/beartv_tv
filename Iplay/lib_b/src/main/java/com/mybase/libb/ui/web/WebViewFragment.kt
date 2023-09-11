package com.mybase.libb.ui.web

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.just.agentweb.AgentWeb
import com.mybase.libb.R
import com.mybase.libb.ext.createTitle
import com.mybase.libb.ext.notEmpty
import com.mybase.libb.ui.BaseFragment


open class WebViewFragment : BaseFragment() {

    override fun layoutId() = R.layout.include_lib_framelayout

    companion object {
        fun newInstance(title: String?, url: String): WebViewFragment {
            return WebViewFragment().setBundle {
                it["title"] = title
                it["url"] = url
            }
        }
    }

    private val title by argument("")

    private val url by argument("")

    private lateinit var mAgentWeb: AgentWeb

    override fun initView(savedInstanceState: Bundle?) {

        val fl = findViewById<FrameLayout>(R.id.lib_framelayout)

        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(fl, FrameLayout.LayoutParams(-1, -1))
            .useDefaultIndicator(R.color.key_color).setWebChromeClient(getWebChromeClient())
            .setWebViewClient(getWebViewClient())
            .createAgentWeb()
            .ready()
            .go(url)


        mActivity.onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!mAgentWeb.back()) {
                        mActivity.finish()
                    }
                }
            })

    }

    open fun getAgentWeb() = mAgentWeb

    open fun getWebChromeClient() = LibWebChromeClient()

    open fun getWebViewClient() = LibWebViewClient()

    override fun initTitle(): ViewBinding? {
        if (title.notEmpty()) return createTitle(title)
        return super.initTitle()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroyView() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroyView()
    }

}