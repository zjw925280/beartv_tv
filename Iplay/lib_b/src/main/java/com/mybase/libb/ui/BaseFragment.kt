package com.mybase.libb.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.mybase.libb.R
import com.mybase.libb.ext.getLifeActivity


abstract class BaseFragment : Fragment(), IBase {

    protected abstract fun layoutId(): Int

    lateinit var mContext: Context

    val mActivity: FragmentActivity get() = mContext.getLifeActivity()!!

    private lateinit var mView: View

    private lateinit var mIBaseControl: IBaseControl

    private var isViewCreated = false//布局是否被创建

    private var isLoadData = false//数据是否加载

    val viewInit get() = isViewCreated

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    open fun lazyInit() {
        //vp默认适配器下的懒加载
    }

    override fun <T : View> findViewById(@IdRes id: Int): T = mView.findViewById(id)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mView = inflater.inflate(
            R.layout.activity_lib_base_layout,
            container,
            false
        )

        mIBaseControl = IBaseControl(
            this,
            savedInstanceState,
            viewLifecycleOwner,
            inflater,
            layoutId()
        )
        mIBaseControl.initControl()

        return mView
    }

    override fun inflateBinding(inflater: LayoutInflater, vg: ViewGroup) {
        //给  BaseVbActivity   用的,外部无需重写
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        mIBaseControl.initOther()
    }


    override fun observeUi(owner: LifecycleOwner) {

    }

    override fun initData(tag: Any?) {

    }

    override fun showProgress(txt: String?) {
        (mActivity as? BaseActivity)?.showProgress(txt)
    }

    override fun dismissProgress() {
        (mActivity as? BaseActivity)?.dismissProgress()
    }


    override fun onResume() {
        super.onResume()
        if (!isLoadData && isFragmentVisible(this) && this.isAdded) {

            if (parentFragment == null || isFragmentVisible(requireParentFragment())) {
                lazyInit()
                isLoadData = true
            }
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isFragmentVisible(this) && !isLoadData && isViewCreated && this.isAdded) {
            lazyInit()
            isLoadData = true
        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //onHiddenChanged调用在Resumed之前，所以此时可能fragment被add, 但还没resumed
        if (!hidden && !this.isResumed)
            return
        //使用hide和show时，fragment的所有生命周期方法都不会调用，除了onHiddenChanged（）
        if (!isLoadData && !hidden && this.isAdded) {
            lazyInit()
            isLoadData = true
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        isViewCreated = false
        isLoadData = false
    }


    override fun initTitle(): ViewBinding? = null


    /**
     * 当前Fragment是否对用户是否可见
     * @param fragment 要判断的fragment
     * @return true表示对用户可见
     */
    private fun isFragmentVisible(fragment: Fragment): Boolean {
        return !fragment.isHidden && fragment.userVisibleHint
    }


}