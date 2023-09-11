package com.mybase.libb.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.jccppp.dialog.JustDialog
import com.jccppp.dialog.addView
import com.jccppp.dialog.setText
import com.mybase.libb.R


abstract class BaseActivity(private val layoutId: Int) : AppCompatActivity(), IBase {

    var mLoadingDialog: Dialog? = null

    //    val mActivity: BaseActivity get() = this
    lateinit var mActivity: BaseActivity

    lateinit var mIBaseControl: IBaseControl

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        mActivity = this
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_lib_base_layout)


        mIBaseControl = IBaseControl(
            this,
            savedInstanceState,
            this,
            LayoutInflater.from(this),
            layoutId
        )

        mIBaseControl.initControl()
        mIBaseControl.initOther()
    }

    override fun inflateBinding(inflater: LayoutInflater, vg: ViewGroup) {
        //给  BaseVbActivity   用的,外部无需重写
    }

    var isPause = false

    override fun onResume() {
        super.onResume()
        if (isPause) {
            isPause = false
            if (KeyboardUtils.isSoftInputVisible(this))
                KeyboardUtils.hideSoftInput(this)
        }
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun observeUi(owner: LifecycleOwner) {
        //livedata observe 的地方

    }

    override fun initData(tag: Any?) {

    }

    open fun closeBack(): Boolean = false

    override fun onBackPressed() {
        if (!closeBack())
            super.onBackPressed()
    }

    override fun <T : View> findViewById(@IdRes id: Int): T = super.findViewById(id)

    override fun initTitle(): ViewBinding? = null

    override fun showProgress(txt: String?) {
        mLoadingDialog ?: let {
            mLoadingDialog =
                JustDialog(
                    it,
                    themeResId = com.jccppp.dialog.R.style.Transparent_Dialog
                ).cancelable(false)
                    .cancelOnTouchOutside(false)
                    .addView(R.layout.dialog_lib_load)

        }
        (mLoadingDialog as? JustDialog)?.setText(R.id.tipTextView, txt)
        if (mLoadingDialog?.isShowing != true) {
            mLoadingDialog?.show()
        }
    }

    override fun dismissProgress() {
        mLoadingDialog?.dismiss()
    }

}
