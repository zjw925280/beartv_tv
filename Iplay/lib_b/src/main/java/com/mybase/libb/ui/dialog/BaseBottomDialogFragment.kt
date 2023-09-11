package com.mybase.libb.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.*
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



abstract class BaseBottomDialogFragment : BottomSheetDialogFragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    lateinit var mActivity: FragmentActivity

    lateinit var mView: View


    private var isShow = false

    //更改了配置
    private var changeConfig = true

    var behavior : BottomSheetBehavior<*>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    companion object {
        private const val REQUEST_KEY = "base_bottom_dialog_request_key"
    }

    private val mHeight get() = (ScreenUtils.getScreenHeight() * 0.7).toInt()

    override fun onStart() {
        super.onStart()

        if (behavior == null) {
            val parent = mView.parent
            val params: CoordinatorLayout.LayoutParams =
                (parent as ViewGroup).layoutParams as CoordinatorLayout.LayoutParams

            behavior = params.behavior as BottomSheetBehavior
        }

        mView.post {

            behavior?.setPeekHeight(mHeight,true)//让dialog的内容显示完整
            behavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(layoutId, container, false)

        mView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight)
        return mView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setCanceledOnTouchOutside(true)//设置点击外部可消失

        val win = dialog.window!!

        win.setDimAmount(0f)

        return dialog
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

}