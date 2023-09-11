package com.mybase.libb.ext

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.gyf.immersionbar.ImmersionBar
import com.mybase.libb.R
import com.mybase.libb.databinding.IncludeLibBaseTitleBinding
import com.mybase.libb.ui.BaseActivity
import com.mybase.libb.ui.IBase



/**
 *
 * BaseActivity 加载dialog
 *
 * */

fun getAppGlobal(): Context = Utils.getApp()



fun Array<out View>.setNoDouble(duration: Long = 500, listener: View.OnClickListener) {
    ClickUtils.applySingleDebouncing(this, duration, listener)
}

fun View.setNoDouble(duration: Long = 500, listener: View.OnClickListener) {
    ClickUtils.applySingleDebouncing(this, duration, listener)
}

fun View.isVISIBLE() = visibility == View.VISIBLE

fun View.showVISIBLE() = apply { visibility = View.VISIBLE }
private fun View._showINVISIBLE() = apply { visibility = View.INVISIBLE }
private fun View._showGONE() = apply { visibility = View.GONE }
fun View.showGONE(show: Boolean = false) = apply { if (show) showVISIBLE() else _showGONE() }
fun View.showINVISIBLE(show: Boolean = false) =
    apply { if (show) showVISIBLE() else _showINVISIBLE() }

fun View.setSize(width: Int = 0, height: Int = 0) = apply {
    val l = layoutParams
    l.width = width
    l.height = height
    layoutParams = l
}

fun showToast(msg: String?, isLong: Boolean = true) {
    if (msg.isNullOrEmpty()) {
        return
    }
    if (isLong)
        ToastUtils.showLong(msg)
    else
        ToastUtils.showShort(msg)
}

fun showToastShort(msg: String?) {
    showToast(msg, false)
}

fun Context.getLifeActivity(): FragmentActivity? {
    if (this is FragmentActivity) {
        return this
    }
    return null
}

fun Context.getBActivity(): BaseActivity? {
    if (this is BaseActivity) {
        return this
    }
    return null
}

fun Fragment.getBActivity(): BaseActivity? {
    if (this.context is BaseActivity) {
        return this.context as BaseActivity
    }
    return null
}

fun Activity.getBActivity(): BaseActivity? {
    if (this is BaseActivity) {
        return this
    }
    return null
}


fun Fragment.createTitle(
    title: String?,
    @ColorRes
    bgColor: Int = R.color.white,
    showLeft: Boolean = true,
    hasImmersive: Boolean = true,
    root: ViewGroup? = null,
    leftImg: Int = 0,
    leftView: View? = null,
    rightView: View? = null,
    centerView: View? = null,
    onLeftListener: View.OnClickListener? = null,
    onRightListener: View.OnClickListener? = null
): IncludeLibBaseTitleBinding {

    return createTitle(
        this,
        title,
        bgColor,
        showLeft,
        hasImmersive,
        root,
        leftImg,
        leftView,
        rightView,
        centerView,
        onLeftListener,
        onRightListener
    )
}


/**
 *   common title 的调用
 *
 * */

fun Activity.createTitle(
    title: String?,
    @ColorRes
    bgColor: Int = R.color.white,
    showLeft: Boolean = true,
    hasImmersive: Boolean = true,
    root: ViewGroup? = null,
    leftImg: Int = 0,
    leftView: View? = null,
    rightView: View? = null,
    centerView: View? = null,
    onLeftListener: View.OnClickListener? = null,
    onRightListener: View.OnClickListener? = null
): IncludeLibBaseTitleBinding {

    return createTitle(
        this,
        title,
        bgColor,
        showLeft,
        hasImmersive,
        root,
        leftImg,
        leftView,
        rightView,
        centerView,
        onLeftListener,
        onRightListener
    )
}

private fun createTitle(
    any: Any,
    title: String?,
    @ColorRes
    bgColor: Int = R.color.white,
    showLeft: Boolean = true,
    hasImmersive: Boolean = true,
    root: ViewGroup? = null,
    leftImg: Int = 0,
    leftView: View? = null,
    rightView: View? = null,
    centerView: View? = null,
    onLeftListener: View.OnClickListener? = null,
    onRightListener: View.OnClickListener? = null
): IncludeLibBaseTitleBinding {

    val context = if (any is Fragment) any.context else any as Activity

    val binding: IncludeLibBaseTitleBinding =
        if (root != null) IncludeLibBaseTitleBinding.inflate(
            if (any is Fragment) any.layoutInflater else (any as Activity).layoutInflater,
            root,
            true
        )
        else IncludeLibBaseTitleBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.include_lib_base_title, null)
        )

    if (hasImmersive) {
        statusBar(any, bgColor = bgColor)
    }

    binding.apply {
        if (!title.isNullOrEmpty())
            titleText.text = "$title"
        if (bgColor != 0)
            mTitle.setBackgroundResource(bgColor)

        if (showLeft) {
            val left = leftView ?: ImageView(context).also {
                it.setImageResource(if (leftImg != 0) leftImg else R.mipmap.nav_back)
            }

            val leftListener = onLeftListener ?: View.OnClickListener {
                (context as Activity).onBackPressed()
            }
            leftParent.setNoDouble(listener = leftListener)

            leftParent.addView(
                left,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also {
                    it.gravity = Gravity.CENTER
                }
            )
        }

        rightView?.let {
            val layoutParams = it.layoutParams ?: FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also { it.gravity = Gravity.CENTER }
            rightParent.addView(it, layoutParams)
            if (onRightListener != null) rightParent.setNoDouble(listener = onRightListener)
        }
        centerView?.let {
            val layoutParams = it.layoutParams ?: FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also { it.gravity = Gravity.CENTER }
            centerParent.addView(it, layoutParams)
        }
    }

    return binding
}

fun Fragment.statusBar(
    hasImmersive: Boolean = true, //是否沉浸式
    statusView: View? = null,
    bgColor: Int = R.color.white,
    darkFont: Boolean = true,//字体是深色的(黑色)
    im: (ImmersionBar.() -> ImmersionBar)? = null
) {
    statusBar(this, hasImmersive, statusView, bgColor, darkFont, im)
}

fun Activity.statusBar(
    hasImmersive: Boolean = true, //是否沉浸式
    statusView: View? = null,
    bgColor: Int = R.color.white,
    darkFont: Boolean = true,//字体是深色的(黑色)
    im: (ImmersionBar.() -> ImmersionBar)? = null
) {
    statusBar(this, hasImmersive, statusView, bgColor, darkFont, im)
}


private fun statusBar(
    any: Any,
    hasImmersive: Boolean = true, //是否沉浸式
    statusView: View? = null,
    bgColor: Int = R.color.white,
    darkFont: Boolean = true,//字体是深色的(黑色)
    im: (ImmersionBar.() -> ImmersionBar)? = null
) {

    val statusHeight: View? =
        if (hasImmersive && any is IBase) any.findViewById<View>(R.id.lib_statusHeight) else statusView

    val i =
        (if (any is Activity) ImmersionBar.with(any) else ImmersionBar.with(any as Fragment)).also {
            it.statusBarDarkFont(darkFont)
            if (hasImmersive)
                it.statusBarColor(bgColor)
            statusHeight?.let { left ->
                it.titleBarMarginTop(left)
            }
        }

    if (im != null) {
        im.invoke(i).init()
    } else {
        i.init()
    }
}













