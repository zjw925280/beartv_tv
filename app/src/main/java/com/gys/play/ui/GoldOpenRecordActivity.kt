package com.gys.play.ui

import android.os.Bundle
import android.widget.TextView
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.databinding.ActivityGoldOpenRecordBinding
import com.gys.play.fragment.home.PagerAdapter
import com.gys.play.fragment.open_record.ConsumeListFragment
import com.gys.play.fragment.open_record.RechargeListFragment
import com.gys.play.viewmodel.NovelBasePresent
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GoldOpenRecordActivity :
    NovelBaseActivity<NovelBasePresent<GoldOpenRecordActivity>, ActivityGoldOpenRecordBinding>(R.layout.activity_gold_open_record) {

    val fragmentList =
        listOf(
            RechargeListFragment(),
            ConsumeListFragment()
        )
    val stringList = listOf(
        MyApplication.getInstance().getActivityResources().getString(R.string.top_up_records),
        MyApplication.getInstance().getActivityResources().getString(R.string.consumption_records)
    )

    override fun loadData(savedInstanceState: Bundle?) {
        setTitleText(getString(R.string.details))
        val tabLayout = binding.tabLayout
        binding.viewPager.adapter = PagerAdapter(this, fragmentList)
        TabLayoutMediator(tabLayout, binding.viewPager, true) { tab, position ->
            tab.setCustomView(R.layout.item_layout_tab)
            var textView: TextView = tab.customView!!.findViewById(R.id.text)
            textView.text = stringList[position]
        }.attach()
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                var textView: TextView = tab?.customView!!.findViewById(R.id.text)
                textView.setTextColor(resources.getColor(R.color.key_color))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                var textView: TextView = tab?.customView!!.findViewById(R.id.text)
                textView.setTextColor(resources.getColor(R.color.C_353535))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        tabLayout.selectTab(tabLayout.getTabAt(0))
        initTabLayout(tabLayout)
    }

    fun initTabLayout(tabLayout: TabLayout) {
        var textView: TextView = tabLayout.getTabAt(0)?.customView!!.findViewById(R.id.text)
        textView.setTextColor(resources.getColor(R.color.key_color))
    }

    override fun setListener() {

    }

    override fun initView() {

    }

    override fun initData() {

    }
}