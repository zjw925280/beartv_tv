package com.gys.play.ui.video

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.ActivityVideoListBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.VideoDetailBean
import com.gys.play.getString
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.ReportActivity
import com.gys.play.util.LiveDataUtil
import com.jccppp.start.argument
import com.jccppp.start.launchAc
import com.mybase.libb.ext.*
import com.mybase.libb.ui.vb.BaseVbActivity
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView
import post

class VideoListActivity : BaseVbActivity<ActivityVideoListBinding>() {

    private val viewModel by viewModels<VideoViewModel>()

    private val orientationUtils by lazy {
        MyOrientationUtils(this, mBind.player)
    }

    private var isPlay = false

    private val id by argument(0)

    private val chapterId by argument("")

    private val mCoinBayPop by lazy(LazyThreadSafetyMode.NONE) {
        CoinBayPop(this@VideoListActivity) {
            getVideoDetail(false, it)
        }
    }

    companion object {
        var OTHER_PLAYER_START = false

        fun launch(activity: FragmentActivity, id: Int, chapterId: String = "") {
            activity.launchAc<VideoListActivity>("id" to id, "chapterId" to chapterId)
            if (activity is VideoListActivity) {
                activity.finish()
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

        OTHER_PLAYER_START = true

        statusBar(hasImmersive = false, darkFont = false)

        val goneBottom: View.(tag: Any?) -> Unit = {
            findViewById<View>(R.id.bottom).showGONE()
        }

        mBind.state.onError(goneBottom)
        mBind.state.onLoading(goneBottom)

        mBind.player.fullscreenButton?.setOnClickListener { //直接横屏

            if (orientationUtils.isLand != 1) {
                //直接横屏
                //orientationUtils.resolveByClick()
                val videoHeight = GSYVideoManager.instance().currentVideoHeight
                val videoWidth = GSYVideoManager.instance().currentVideoWidth
                if (videoWidth > videoHeight){
                    orientationUtils.resolveByClick()
                }
            }
            //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
            mBind.player.startWindowFullscreen(this, true, true)
        }

        orientationUtils.isEnable = false

        mBind.player.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {

                //开始播放了才能旋转和全屏
                orientationUtils.isEnable = true
                isPlay = true
                val currentPosition = mBind.player.currentPlayer().getCurrentPosition()
                if (currentPosition != viewModel.playPosition.value) {
                    viewModel.playPosition.value = currentPosition
                }

            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                orientationUtils.backToProtVideo()
                viewModel.showChoosePop.value = false
            }

        })

        mBind.player.mListener = object : MyPlayerListener {
            override fun playChange(position: Int) {
                viewModel.playPosition.value = position
            }

            override fun showBayForCoin(index: Int, bean: VideoDetailBean.VideoDetailChapterBean) {
                orientationUtils.isEnable = false
                mCoinBayPop.show(index, bean)
            }

            override fun showChoosePop() {
                viewModel.showChoosePop.value = true
            }

        }

        //设置返回按键功能
        mBind.player.backButton.setOnClickListener { onBackPressed() }
        mBind.player.isShowFullAnimation = false
        mBind.player.isRotateViewAuto = false
        mBind.player.isLockLand = false
        mBind.player.isRotateWithSystem = true
        mBind.player.isNeedLockFull = true
        mBind.player.setLockClickListener { _, lock ->
            orientationUtils.isEnable = !lock
        }

        initBarAndVp()
         mBind.imgJubao.setOnClickListener {
             if (!SpUserInfo.isLogin()) {
                 startActivity(Intent(getBActivity(), LoginActivity::class.java))
                 return@setOnClickListener
             }
             ReportActivity.start(this)
         }
    }

    override fun initData(tag: Any?) {
        super.initData(tag)

        getVideoDetail(true)

    }

    private fun getVideoDetail(first: Boolean, index: Int = -1) {
        val state: View? = if (first) mBind.state else null
        post(stateView = state) {
            val videoDetail = Quest.api.getVideoDetail(Quest.getHead("App.ShortTv.Detail") {
                add("id", "$id")
            })

            viewModel.videoDetailBean.value = videoDetail
            mBind.player.loadCoverImage(videoDetail.cover, R.mipmap.img_4)

            var localIndex = VideoKv.getLocalIndex("$id")

            if (index < 0) {
                if (chapterId.notEmpty()) {
                    breaking@ run {
                        videoDetail.chapter_list.forEachIndexed { index, bean ->
                            if (bean.id == chapterId) {
                                localIndex = index
                            }
                        }
                        return@run
                    }
                }
                if (videoDetail.chapter_list.size <= localIndex) {
                    localIndex = 0
                }
            } else {
                localIndex = index
            }

            mBind.player.setVideoId("$id")

            mBind.player.setUp(videoDetail.pay_type, videoDetail.chapter_list, localIndex)
            if (videoDetail.chapter_list.size > localIndex) {
                val localRecord = VideoKv.getLocalRecord(videoDetail.chapter_list[localIndex].id)
                if (localRecord > 0) mBind.player.seekOnStart = localRecord
            }
            mBind.player.playForIndex(localIndex)

        }

    }

    private fun initBarAndVp() {

        val titles = arrayListOf(R.string.video.getString(), R.string.comment.getString())

        val commonNavigator = CommonNavigator(this)

        var num = ""

        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount() = 2

            override fun getTitleView(context: Context?, index: Int) =
                CommonPagerTitleView(context).apply {
                    val view =
                        LayoutInflater.from(context).inflate(R.layout.pager_title_layout, null)
                    val tv1 = view.findViewById<TextView>(R.id.tv1)
                    tv1.text = titles[index]
                    if (index == 1) view.findViewById<TextView>(R.id.tv2).text = num
                    view.setOnClickListener {
                        if (mBind.vp.currentItem != index) mBind.vp.currentItem = index
                    }
                    setContentView(view)
                    onPagerTitleChangeListener = object :
                        CommonPagerTitleView.OnPagerTitleChangeListener {
                        override fun onSelected(index: Int, totalCount: Int) {
                            tv1.paint.typeface = Typeface.DEFAULT_BOLD
                            tv1.setTextColor(R.color.C_00ACFF.getColor())
                        }

                        override fun onDeselected(index: Int, totalCount: Int) {
                            tv1.paint.typeface = Typeface.DEFAULT
                            tv1.setTextColor(R.color.C_52576B.getColor())
                        }

                        override fun onLeave(
                            index: Int,
                            totalCount: Int,
                            leavePercent: Float,
                            leftToRight: Boolean
                        ) {
                        }

                        override fun onEnter(
                            index: Int,
                            totalCount: Int,
                            enterPercent: Float,
                            leftToRight: Boolean
                        ) {
                        }
                    }
                }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.roundRadius = 8f
                indicator.xOffset = 14f
                indicator.yOffset = 2f
                indicator.lineWidth = 12f.dp2px().toFloat()
                indicator.setColors(R.color.C_00ACFF.getColor())
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                return indicator
            }

        }

        mBind.mi.navigator = commonNavigator

        mBind.vp.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return if (position == 0) MoreVideoFragment.newInstance(id) else CommentVideoFragment.newInstance(
                    "$id"
                )
            }
        }

        mBind.vp.offscreenPageLimit = 2

        mBind.mi.bind(mBind.vp)

        viewModel.commentNum.observe(this) {
            num = it
            commonNavigator.notifyDataSetChanged()
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mBind.player.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
        }
    }

    override fun onPause() {
        super.onPause()
        mBind.player.currentPlayer.onVideoPause()
        orientationUtils.setIsPause(true)
    }

    override fun onResume() {
        super.onResume()
        mBind.player.currentPlayer.onVideoResume()
        orientationUtils.setIsPause(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay) {
            mBind.player.currentPlayer.release()
        }
        orientationUtils.releaseListener()
        mBind.player.setVideoAllCallBack(null)

        VideoKv.onDestroy()
    }

    override fun onBackPressed() {

        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }

    var isVipRecharge = true

    override fun observeUi(owner: LifecycleOwner) {
        super.observeUi(owner)
        viewModel.playPosition.observe(owner) {
            if (mBind.player.currentPlayer().getCurrentPosition() != it)
                mBind.player.currentPlayer().playForIndex(it)
        }

        if (SpUserInfo.isVip()) {
            isVipRecharge = false
        }

        LiveDataUtil.userInfo.observe(owner) {
            if(it.isVip()&&mBind.player.currentPlayer().showVipHint){
                mBind.player.handlerTime(180*1000L)

            }
            if (isVipRecharge) {
                isVipRecharge = false
                mBind.player.currentPlayer()
                    .playForIndex(mBind.player.currentPlayer().getCurrentPosition())
            }
        }
    }
}