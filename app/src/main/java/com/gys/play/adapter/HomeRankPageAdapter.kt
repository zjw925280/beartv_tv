package com.gys.play.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeRankPageAdapter(fm: FragmentManager, mFragments: ArrayList<Fragment>,
                           mTitles: Array<String?>) :
    FragmentPagerAdapter(fm) {

    var mFragments = ArrayList<Fragment>()
    var mTitles :Array<String?>

    init {
        this.mFragments = mFragments
        this.mTitles = mTitles
    }

    override fun getCount(): Int = mFragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles.get(position)
    }

    override fun getItem(position: Int): Fragment {
        return mFragments.get(position)
    }
}
