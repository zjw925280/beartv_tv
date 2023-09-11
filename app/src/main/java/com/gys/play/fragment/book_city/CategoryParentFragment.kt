package com.gys.play.fragment.book_city

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.FragmentUtils
import com.mybase.libb.ui.BaseFragment
import com.gys.play.R
import com.gys.play.util.KvUserInfo
import com.gys.play.util.boy


class CategoryParentFragment : BaseFragment() {
    override fun layoutId() = R.layout.fragment_parent_category

    private val mFragments = arrayListOf<Fragment>()

    private var curIndex: Int = 0

    companion object {
        fun newInstance() = CategoryParentFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {

        curIndex = 0.boy(1)
//        mFragments.add(HomeSexFragment.newInStane(1))
//        mFragments.add(HomeSexFragment.newInStane(2))
//        mFragments.add(CategoryFragment.newInStane(1))
//        mFragments.add(CategoryFragment.newInStane(2))

        FragmentUtils.add(
            childFragmentManager,
            mFragments,
            R.id.category,
            arrayOf("CategoryFragment1", "CategoryFragment2"),
            curIndex
        )

    }

    override fun observeUi(owner: LifecycleOwner) {
        KvUserInfo.lookTypeLd.observe(owner) {
            curIndex = if (it) 0 else 1
            showCurrentFragment()
        }

    }

    private fun showCurrentFragment() {
        FragmentUtils.showHide(curIndex, mFragments)
    }


}