package com.gys.play

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.android.liba.util.UIHelper
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.material.tabs.TabLayout
import com.gys.play.databinding.ActivityMainBinding
import com.gys.play.databinding.ItemTablayoutBinding
import com.gys.play.entity.HomeBannerInfo
import com.gys.play.entity.TabInfo
import com.gys.play.fragment.home.ExploreFragment
import com.gys.play.fragment.home.HomeFragment
import com.gys.play.fragment.home.MineFragment
import com.gys.play.fragment.home.PagerAdapter
import com.gys.play.ui.NovelBaseActivity
import com.gys.play.ui.video.VideoListActivity
import com.gys.play.util.AppSigning
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.LiveDataUtil
import com.gys.play.util.dialog.HomeHuoDongDialog
import com.gys.play.viewmodel.MainViewModel
import com.gys.play.wow.WowFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class
MainActivity :
    NovelBaseActivity<MainViewModel<MainActivity>, ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        UIHelper.showLog(TAG, "onCreate: $action  $data")
        data?.let {
            goBookDetail(it)
        }
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        AppLinkData.fetchDeferredAppLinkData(
            this
        ) {
            UIHelper.showLog(TAG, "onCreate: AppLinkData $it")
            if (it != null) {
                UIHelper.showLog(TAG, "onCreate: AppLinkData.targetUri " + it.targetUri)
            }
            it?.targetUri?.let {
                goBookDetail(it)
            }
        }
        presenter.guest()
        facebookKeyHash()
        // 获取签名MD5值
        val mD5 = AppSigning.getMD5(activity)
        val sha1 = AppSigning.getSha1(activity)
        val shA256 = AppSigning.getSHA256(activity)
        UIHelper.showLog(" 密钥获取 获取签名MD5值   "+mD5)
        UIHelper.showLog("密钥获取  获取签名sha1值   "+sha1)
        UIHelper.showLog(" 密钥获取 获取签名MshA256 值   "+shA256 )
    }
    /**
     * facebook 散类密钥获取
     */
    private fun facebookKeyHash() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("密钥获取  KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }
    private fun goBookDetail(it: Uri) {
        UIHelper.showLog(TAG, "goBookDetail: Uri $it")
        val id = it.getQueryParameter("id")
        UIHelper.showLog(TAG, "goDetail: id $id")
        if (!id.isNullOrEmpty()) {
            try {
                val chapterId = it.getQueryParameter("chapterId")
                UIHelper.showLog(TAG, "goBookDetail: chapterId $chapterId")
                VideoListActivity.launch(this, "$id".toInt(), "$chapterId")
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }


    private val fragmentList by lazy {
        mutableListOf<Fragment>(
            HomeFragment(),
            WowFragment(),
            ExploreFragment(),
//            FrequencyDivisionFragment(),
            MineFragment()
        )
    }
    val tabInfoList = arrayListOf<TabInfo>()
    var textView: TextView? = null
    var imageView: ImageView? = null
    var isGoTop = false
    var isClickGoTop = false

    override fun initView() {
        LiveDataUtil.mainActivityTabIndex.observe(this) {
            goPager(it)
        }
    }

    override fun initData() {
        MyApplication.getPay().setPresenter(presenter)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: " + BuildConfig.FLAVOR + " " + Config.CHANNEL)
        MyApplication.getPay().queryPurchases()
    }

    override fun loadData(savedInstanceState: Bundle?) {
        handleMain(savedInstanceState)

        val serviceConfig = SpServiceConfig.getServiceConfig()
        showHomePositionAd(serviceConfig.home_popup_frequency)
//            serviceConfig.showUpdateDialog(this)
    }

    private fun showHomePositionAd(home_popup_frequency: Int) {
        presenter.getHomePositionAdInfo { _, data ->
            val item = data.items
            if (home_popup_frequency == 0) {
                showHomeDialog(item)
            } else {
                if (HomeHuoDongDialog.isNotToday()) {
                    showHomeDialog(item)
                }
            }

            //升级弹窗要在活动弹窗之上，所以改下位置！
            val updateDialog = SpServiceConfig.getServiceConfig().showUpdateDialog(this)
            if (updateDialog == null) {
                //不升级才显示签到，
//                checkSignDialog()
            } else {
                updateDialog.setOnDismissListener {
//                    checkSignDialog()
                }
            }
        }
    }

//    private fun checkSignDialog() {
////        if (!SpUserInfo.isLogin()) return
//        if (!UIHelper.isTodayFirst(
//                mActivity,
//                "isTodayFirstShowSignDialog" + SpUserInfo.checkId()
//            )
//        ) return
//        post {
//            val data = Quest.api.getSignListSSS(Quest.getHead(Config.APP_USER_SIGN_LIST))
//            if (data.isTodaySigned()) return@post
//        }
//    }

    private fun showHomeDialog(item: MutableList<HomeBannerInfo>) {
        if (item.size > 0) {
            HomeHuoDongDialog.show(this, item)
        }
    }

    private fun handleMain(savedInstanceState: Bundle?) {
        val viewPager = binding.viewPager
        tabInfoList.addAll(
            arrayListOf(
                TabInfo(
                    getString(R.string.book_city),
                    R.mipmap.home_p,
                    R.mipmap.home_n,
                    R.mipmap.home_wow,
                ),
                TabInfo(
                    "WOW",
                    R.mipmap.wow_p,
                    R.mipmap.wow_n,
                    R.mipmap.wow_yes,

                    ),
                TabInfo(
                    getString(R.string.explore),
                    R.mipmap.explore_p,
                    R.mipmap.explore_n,
                    R.mipmap.explore_wow
                ),
                TabInfo(
                    getString(R.string.mine),
                    R.mipmap.mine_p,
                    R.mipmap.mine_n,
                    R.mipmap.home_wd_p
                )
            )
        )
        viewPager.adapter = PagerAdapter(this, fragmentList)
        viewPager.offscreenPageLimit = tabInfoList.size
        viewPager.registerOnPageChangeCallback(pageChangeListener)
        viewPager.isUserInputEnabled = false
        val tabLayout = binding.tabLayout
        for (i in 0 until tabInfoList.size) {
            tabLayout.addTab(tabLayout.newTab())
            tabLayout.getTabAt(i)?.customView = getTabView(tabInfoList[i])
        }
        tabLayout.addOnTabSelectedListener(tabSelectListener)
        setWowBg(0)
        goPager(0)

        LiveDataUtil.mainActivityTabVisible.observe(this) {
            if (it) {
                tabLayout.visibility = View.VISIBLE
            } else {
                tabLayout.visibility = View.GONE
            }
        }
    }

    private val HOME_POSITION = 1
    fun isGoTopTx() {
        if (true || binding.tabLayout.selectedTabPosition != HOME_POSITION) {
            return
        }
        if (isGoTop) {
            isClickGoTop = true
            setTabTx(getString(R.string.go_top), R.mipmap.tab_top)
        } else {
            setTabTx(getString(R.string.book_city), R.mipmap.tab_home_p)
        }
    }

    private fun setTabTx(s: String, i: Int) {
        textView?.text = s
        imageView?.let {
            ImageLoaderUtils.loadrRsourceGif(it, i)
        }
        binding.tabLayout.getTabAt(HOME_POSITION)?.customView?.findViewById<View>(R.id.ll_view)
            ?.setOnClickListener {
                if (isGoTop) {
                    if (isClickGoTop) {
                        LiveDataUtil.mainHomeGoTopClick.value = true
                    } else {
                        binding.viewPager.setCurrentItem(HOME_POSITION, false)
                    }
                } else {
                    binding.viewPager.setCurrentItem(HOME_POSITION, false)
                }
            }
    }

    private val tabSelectListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            tab.let {
                binding.viewPager.setCurrentItem(tab.position, false)
            }
            var str = ""
            when (tab.position) {
                0 -> str = "1"
                1 -> {
                    str = "2"
                    if (isGoTop) {
                        isClickGoTop = true
                        isGoTopTx()
                        return
                    }
                }
                2 -> str = "3"
            }
            if (str.isNotEmpty()) {
                MyApplication.getAnalytics().setIcon(str)
            }
            setWowBg(tab.position)

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }

    }

    fun setUnSelectTab(tab: TabLayout.Tab, isSelect: Boolean, tabInfo: TabInfo) {
//        tabInfo.wowModel = isWowModel
        if (isSelect) {
            tab.customView?.findViewById<ImageView>(R.id.imageview)?.let {
                ImageLoaderUtils.loadrRsourceGif(
                    it, tabInfo.selimage_src
                )
            }

        } else {
            tab.customView?.findViewById<ImageView>(R.id.imageview)?.let {
                ImageLoaderUtils.loadrRsourceGif(
                    it, tabInfo.nolimage_src
                )
            }
        }

    }

    private fun selTabOne(tab: TabLayout.Tab) {
        if (tab.position == 1) {
            if (isGoTop) {
                isClickGoTop = false
                setTabTx(getString(R.string.book_city), R.mipmap.tab_home_n)
            }
            return
        }
    }

    private val pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.tabLayout.setBackgroundColor(
                if (position != 1) Color.WHITE else Color.parseColor(
                    "#030A1F"
                )
            )
            binding.tabLayout.getTabAt(position)?.select()
        }
    }

    private fun getTabView(tabInfo: TabInfo): View {
        val v: View = LayoutInflater.from(this).inflate(R.layout.item_tablayout, null)
        val binding: ItemTablayoutBinding? = DataBindingUtil.bind(v)
        binding?.item = tabInfo
        binding?.imageview?.setImageResource(tabInfo.nolimage_src)
        return v
    }

    private fun setWowBg(index: Int) {
        val isWow = index == 1
        for (i in 0 until binding.tabLayout.tabCount) {
            val tv =
                binding.tabLayout.getTabAt(i)?.customView?.findViewById<TextView>(R.id.textview)
            if (index == i) {
                tv?.setTextColor(Color.parseColor(if (isWow) "#FFFFFF" else "#00ACFF"))
            } else {
                tv?.setTextColor(Color.parseColor(if (isWow) "#AFB5CD" else "#333333"))
            }

            val iv =
                binding.tabLayout.getTabAt(i)?.customView?.findViewById<ImageView>(R.id.imageview)
            val tabInfo = tabInfoList[i]
            tabInfo.wowModel = isWow
            iv?.setImageResource(if (index == i) tabInfo.selimage_src else tabInfo.nolimage_src)
        }
    }

    private fun goPager(position: Int) {
        if (binding.tabLayout.selectedTabPosition == position) return
        binding.tabLayout.getTabAt(position)?.select()
    }

    override fun setListener() {

    }

}