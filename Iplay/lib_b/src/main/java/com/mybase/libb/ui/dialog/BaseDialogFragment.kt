package com.mybase.libb.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.*
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ScreenUtils
import com.mybase.libb.R



abstract class BaseDialogFragment : DialogFragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    lateinit var mActivity: FragmentActivity

    lateinit var mView: View

    private var amount: Float = -1f

    private var width: Float = -1f

    private var height: Float = -1f

    private var gravity = Gravity.CENTER

    private var cancelableOutSide = true

    private var mCanCancel = true

    private var isShow = false

    //更改了配置
    private var changeConfig = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    companion object {
        private const val REQUEST_KEY = "base_dialog_request_key"
    }

    open fun setMyStyle() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (setMyStyle())
            setStyle(STYLE_NO_TITLE, R.style.lib_fragment_dialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(layoutId, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        observeUi(viewLifecycleOwner)
        initData()
    }

    abstract fun initView(savedInstanceState: Bundle?)

    open fun <T : View> findViewById(@IdRes id: Int) = mView.findViewById<T>(id)

    open fun show(
        manager: FragmentManager,
        tag: String = this.javaClass.name,
        listener: FragmentResultListener? = null
    ) {
        if (checkShow(manager, tag, listener)) {
            this.show(valManager = manager, valTag = tag)
        }
    }

    fun show(
        ac: FragmentActivity,
        tag: String = this.javaClass.name,
        listener: FragmentResultListener? = null
    ) {
        this.show(ac.supportFragmentManager, tag, listener)
    }

    fun show(
        ac: Fragment,
        tag: String = this.javaClass.name,
        listener: FragmentResultListener? = null
    ) {
        this.show(ac.childFragmentManager, tag, listener)
    }

    private fun checkShow(
        manager: FragmentManager,
        tag: String,
        listener: FragmentResultListener?
    ): Boolean {
        val mFragment: Fragment? = manager.findFragmentByTag(tag)

        listener?.let {
            if (mFragment != null)
                manager.setFragmentResultListener(REQUEST_KEY, mFragment, it)
            else
                manager.setFragmentResultListener(REQUEST_KEY, this, it)
        }

        return mFragment == null
    }

    override fun show(valManager: FragmentManager, valTag: String?) {
        isShow = true
        if (!valManager.isDestroyed && !valManager.isStateSaved)
            super.show(valManager, valTag)
        else
            showWithAllowingStateLoss(valManager, valTag)
    }

    override fun onDismiss(dialog: DialogInterface) {
        changeConfig = true
        super.onDismiss(dialog)
        isShow = false
    }

    fun isShowing() = isShow

    fun setBack(bundle: (Bundle.() -> Unit)? = null) {
        parentFragmentManager.setFragmentResult(
            REQUEST_KEY,
            Bundle().also { bundle?.invoke(it) })
    }

    override fun dismiss() {

        val manager = parentFragmentManager
        if (manager.isStateSaved) {
            dismissAllowingStateLoss()
            return
        }
        if (!isAdded) {
            return
        }
        try {
            super.dismiss()
        } catch (t: Throwable) {

        }
    }

    private fun showWithAllowingStateLoss(manager: FragmentManager, tag: String?) {
        // 解决 onSaveInstanceState崩溃问题
        try {
            val c = DialogFragment::class.java
            val dismissed = c.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed.set(this, false)
            val shownByMe = c.getDeclaredField("mShownByMe")
            shownByMe.isAccessible = true
            dismissed.set(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }


    open fun observeUi(owner: LifecycleOwner) {

    }

    open fun initData(tag: Any? = null) {

    }


    //透明度
    open fun setAmount(amount: Float) = apply {
        changeConfig = true
        this.amount = amount
    }

    //位置
    open fun setGravity(gravity: Int) = apply {
        changeConfig = true
        this.gravity = gravity
    }

    //宽度 1f 为百分百
    open fun setWidth(width: Float) = apply {
        changeConfig = true
        this.width = width
    }

    //高度 1f 为百分百
    open fun setHeight(height: Float) = apply {
        changeConfig = true
        this.height = height
    }

    //返回键拦截
    open fun setCanCancel(cancelable: Boolean) = apply {
        changeConfig = true
        mCanCancel = cancelable
    }

    //外部点击拦截
    open fun setCanceledOnTouchOutside(cancelable: Boolean) = apply {
        changeConfig = true
        cancelableOutSide = cancelable
    }


    override fun onStart() {
        super.onStart()
        if (changeConfig) {
            changeConfig = false
            configWindowPercent(gravity, width, height)
        }
    }

    protected open fun configWindowPercent(
        gravity: Int,
        widthPercent: Float,
        heightPercent: Float
    ) {
        dialog?.also {
            it.setCanceledOnTouchOutside(cancelableOutSide)
//            it.setCancelable(mCanCancel)
            if (!mCanCancel)
                it.setOnKeyListener { _, keyCode, event ->
                    keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN
                }
        }?.window?.run {
            setGravity(gravity)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (amount != -1f) {
                setDimAmount(amount)
            }
            val width = if (widthPercent > 0) {
                (ScreenUtils.getScreenWidth() * widthPercent).toInt()
            } else {
                ViewGroup.LayoutParams.MATCH_PARENT
            }

            val height = if (heightPercent > 0) {
                (ScreenUtils.getScreenHeight() * heightPercent).toInt()
            } else {
                ViewGroup.LayoutParams.WRAP_CONTENT
            }

            setLayout(width, height)
            setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )

            //fullScreenImmersive(decorView)
            clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        }
    }


}