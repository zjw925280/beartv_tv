package com.gys.play.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.android.liba.ui.base.BindingActivity
import com.android.liba.ui.dialog.ProgressDialog
import com.gyf.immersionbar.ImmersionBar
import com.gys.play.R
import com.gys.play.entity.SpUserInfo
import com.gys.play.viewmodel.NovelBasePresent
import kotlinx.coroutines.*

abstract class NovelBaseActivity<P : NovelBasePresent<*>, T : ViewBinding>(layout: Int) :
    BindingActivity<P, T>(layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarTextDark(isStatusBarTextDark())
    }

    var immersionBar: ImmersionBar? = null
    open fun setStatusBarTextDark(isDark: Boolean) {
        immersionBar = ImmersionBar.with(this).also {
            it.fitsSystemWindows(false).statusBarDarkFont(isDark).init()
            isStatusBarTextDark = isDark
        }
    }

    private var isStatusBarTextDark = true
    open fun isStatusBarTextDark(): Boolean {
        return isStatusBarTextDark
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

    fun setTitleText(text: String, color: Int = -1) {
        setTextColor(R.id.itemTopTitle, text, color)
    }

    fun setTopRightText(text: String, color: Int = -1) {
        setTextColor(R.id.itemTopRight, text, color)
    }

    fun setTopRightClick(click: View.OnClickListener) {
        findViewById<View>(R.id.itemTopRight)?.setOnClickListener(click)
    }

    private fun setTextColor(id: Int, text: String, color: Int) {
        findViewById<TextView>(id)?.let {
            it.text = text
            setTextColor(it, color)
        }
    }

    fun setTopBackImage(id: Int) {
        findViewById<ImageView>(R.id.itemTopBack)?.setImageResource(id)
    }

    fun setTextColor(view: TextView, color: Int) {
        if (color > -1) {
            view.setTextColor(resources.getColor(color))
        }
    }

    override fun initView(savedInstanceState: Bundle?, fragmentView: View?) {
        super.initView(savedInstanceState, fragmentView)
        findViewById<View>(R.id.itemTopBack)?.setOnClickListener { finish() }
    }

    fun isLogin(): Boolean {
        return SpUserInfo.isLogin()
    }

    fun goLogin() {
        activity?.let {
            startActivity(Intent(it, LoginActivity::class.java))
        }
    }

    var dialoge: ProgressDialog? = null
    fun showProgressDialog() {
        activity?.let {
            if (it.isFinishing) {
                return
            }
            dialoge?.run {
                show()
            } ?: let {
                dialoge = ProgressDialog.show(this)
                dialoge?.setLoadingText("")
            }
        }
    }

    private val progressDialogScope = MainScope()
    fun showProgressDialog(time: Long) {
        showProgressDialog()
        progressDialogScope.launch {
            withContext(Dispatchers.IO) {
                delay(time)
            }
            dismissProgressDialog()
        }
    }

    fun dismissProgressDialog() {
        dialoge?.dismiss()
        progressDialogScope.cancel()
    }

}