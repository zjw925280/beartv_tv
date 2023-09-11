package com.gys.play.fragment.home

import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.android.liba.context.AppContext
import com.android.liba.network.protocol.BaseListInfo
import com.android.liba.ui.base.BindingFragment
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.databinding.FragmentHomeBinding
import com.gys.play.entity.GiftInfo
import com.gys.play.entity.HomeChannelInfo
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.StatusInfo
import com.gys.play.fragment.book_city.HomeNew2Fragment
import com.gys.play.fragment.book_city.HomeNewFragment
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.search.SearchActivity
import com.gys.play.util.KvUserInfo
import com.gys.play.util.LiveDataUtil
import com.gys.play.util.dialog.OpenBoxDialog
import com.gys.play.viewmodel.HomeModel

class HomeFragment :
    BindingFragment<HomeModel, FragmentHomeBinding>() {

    private var fragmentList = ArrayList<Fragment>()
    private var item: MutableList<HomeChannelInfo> = mutableListOf()
    private val titles = mutableListOf<String>()
    private var newIndex = -1
    private var fullIndex = -1
    private var boyIndex = -1
    private var girlIndex = -1
    private var vipIndex = -1
    private var classifyIndex = -1

    override fun getLayoutId() = R.layout.fragment_home

    override fun initView() {
        binding.searchLayout.setOnClickListener {
            MyApplication.getAnalytics().setClassify("搜索点击量")
            activity?.let {
                startActivity(Intent(it, SearchActivity::class.java))
            }
        }
        binding.bookHeadHeight.post {
            LiveDataUtil.categoryHeadHeight.value = binding.bookHeadHeight.height
        }

        presenter.getHomeChannel { _: HomeFragment, data: BaseListInfo<HomeChannelInfo> ->
            item = data.items

            titles.clear()

            data.items.forEach { value ->
                titles.add(value.title)
                when (value.id) {
//                    短剧是2
                    2 -> {
                        fragmentList.add(HomeNew2Fragment.newInStane(value.id))
                    }
                    else -> {
                        fragmentList.add(HomeNewFragment.newInStane(value.id))
                    }
                }
            }


            var defaultIndex = 0
            if (!MyApplication.getInstance().isCurrentChina()) {
                //英文版，默认Home
                defaultIndex = 0
            } else {
                when (KvUserInfo.lookType) {
                    0 -> defaultIndex = 0
                    1 -> if (boyIndex >= 0) defaultIndex = boyIndex
                    2 -> if (girlIndex >= 0) defaultIndex = girlIndex
                }
            }

            binding.viewPager.adapter = PagerAdapter(requireActivity(), fragmentList)
            binding.viewPager.offscreenPageLimit = fragmentList.size
            binding.viewPager.isUserInputEnabled = false//禁止ViewPager滑动
            binding.viewPager.currentItem = defaultIndex

            bindTabViewPager()

            val firstMustReadBookId =
                AppContext.getMMKV().decodeInt(Config.FIRST_MUST_READ_BOOKID, 0)
            if (firstMustReadBookId != 0) {
                AppContext.getMMKV().encode(Config.FIRST_MUST_READ_BOOKID, 0)
            }
        }
    }

    private fun bindTabViewPager() {
        TabLayoutMediator(
            binding.homeTab, binding.viewPager, true, true
        ) { tab, position ->
            tab.setCustomView(R.layout.item_home_tab)
            val customView = tab.customView
            customView?.apply {
                val textTv = customView.findViewById<TextView>(R.id.text)
                textTv.setTextColor(
                    AppCompatResources.getColorStateList(
                        context,
                        R.color.tab_normal
                    )
                )
                textTv.text = titles[position]
                if (position != 0) {
                    textTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                }
            }
        }.attach()

        binding.homeTab.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.text)
                    ?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<TextView>(R.id.text)
                    ?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    override fun initData() {
        setClickListener()
        getFloatList()
    }

    private fun getFloatList() {
        presenter.getfloatInfo { _, data ->
            val item = data.items
            if (item != null && item.size > 0) {
                binding.rlFloatClose.visibility = View.VISIBLE
                val homeFloatInfo = item[0]
                if (context != null)
                    Glide.with(this).load(homeFloatInfo.thumb).into(binding.imgFloat)
                binding.imgFloat.setOnClickListener {
                    activity?.let {
                        homeFloatInfo.jump(it)
                    }
                }
            } else {
                binding.rlFloatView.visibility = View.GONE
            }
        }
    }

    private fun getGiftData() {
        val userInfo = SpUserInfo.getUserInfo()
        if (userInfo.id != "") {
            presenter.getUserGiftStatus { _, data: GiftInfo ->
                if (data.is_get == 1) { //新人礼包已领取
                    binding.redView.visibility = View.GONE
                } else {
                    binding.redView.visibility = View.VISIBLE
                    val firstGift = AppContext.getMMKV().decodeBool(Config.FIRST_GIFT, true)
                    if (firstGift) {
                        showFirstGift()
                    }
                }
            }
        } else {
            binding.redView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getGiftData()
    }

    private fun showFirstGift() {
        presenter.getUserGiftStatus { _, data: GiftInfo ->
            OpenBoxDialog.show(this.context, data.is_get, data.coins) {
                presenter.userGetGift { _, data: StatusInfo ->
                    showToast(data.desc)
                    binding.redView.visibility = View.GONE
                    MyApplication.getPay().updateUserInfo()
                }
            }
        }
    }

    fun removeTabLayoutLongClick(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                tab.view.isLongClickable = false
                // 针对android 26及以上需要设置setTooltipText为null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // 可以设置null也可以是""
                    tab.view.tooltipText = null
                    //                     tab.view.setTooltipText("");
                }
            }
        }
    }

    private fun setClickListener() {
        binding.imgGift.setOnClickListener {
            MyApplication.getAnalytics().setHomeTab("礼包点击量")
            if (!SpUserInfo.isLogin()) {
                activity?.let {
                    startActivity(Intent(it, LoginActivity::class.java))
                }
                return@setOnClickListener
            }
            showFirstGift()
        }
        binding.rlFloatClose.setOnClickListener {
            binding.rlFloatView.visibility = View.GONE
        }
    }


}