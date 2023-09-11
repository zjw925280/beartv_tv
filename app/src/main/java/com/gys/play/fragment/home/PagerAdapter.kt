package com.gys.play.fragment.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(activity: FragmentActivity, val list: List<Any>) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return list[position] as Fragment
    }

    override fun getItemCount(): Int = list.size
}
