package com.gys.play.wow

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentWowBinding
import com.gys.play.ui.video.VideoListActivity
import com.mybase.libb.ext.statusBar
import com.mybase.libb.ui.vb.BaseVbFragment
import com.shuyu.gsyvideoplayer.GSYVideoManager
import post


class WowFragment : BaseVbFragment<FragmentWowBinding>() {

    private val adapter by lazy { WowAdapter(mActivity) }

    private var playPosition = 0

    private var page = 1

    private var isLoadEnd = false


    override fun initView(savedInstanceState: Bundle?) {

        mBind.srl.run {
            setEnableRefresh(false)
            setEnableLoadMore(false)
            setEnableAutoLoadMore(false)
            setFooterHeight(50f)
            setEnableScrollContentWhenLoaded(false)
            setEnableScrollContentWhenRefreshed(false)
            /*setOnRefreshListener {
                postDelayed({
                    getData(true)
                    finishRefresh()
                    mBind.vp.post {
                        playPosition(0)
                    }
                }, 2000)

            }*/
            setOnLoadMoreListener {
                postDelayed({
                    getData()
                    mBind.vp.currentItem = mBind.vp.currentItem.plus(1)
                }, 2000)

            }
        }

        mBind.vp.orientation = ViewPager2.ORIENTATION_VERTICAL

        mBind.vp.adapter = adapter

        mBind.vp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 大于0说明有播放
                if (playPosition != position)
                    playPosition(position)
                if (!isLoadEnd && position + 5 > adapter.getData().size) {
                    getData()
                }
                mBind.srl.setEnableLoadMore(position == adapter.getData().size - 1)

                getItemHolder()?.loadLikeDetail()
            }
        })
        mBind.vp.offscreenPageLimit = 1

        setVpSlide(mBind.vp)
    }

    private fun playPosition(position: Int) {
        playPosition = position

        mBind.vp.postDelayed({
            getPlayer(true)?.startPlayLogic()
        }, 100)
    }

    private fun setVpSlide(vp: ViewPager2) {
        for (i in 0 until vp.childCount) {
            val child = vp.getChildAt(i)
            if (child is RecyclerView) {
                //解决滑动太灵敏
                val declaredField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
                declaredField.isAccessible = true
                val touchSlop = declaredField.get(child) as Int
                declaredField.set(child, touchSlop * 3)
            }
        }
    }

    private fun getPlayer(cancel: Boolean = false): WowPlayer? {
        return getItemHolder()?.getPlayer(cancel)
    }

    private fun getItemHolder(): WowItemHolder? {
        val viewHolder =
            (mBind.vp.getChildAt(0) as RecyclerView).findViewHolderForAdapterPosition(playPosition)
        if (viewHolder != null) {
            return viewHolder as WowItemHolder
        }
        return null
    }

    private fun getData(isFirst: Boolean = false) {
        val stateView: View? = if (isFirst) mBind.sl else null
        if (isFirst) page = 1
        post(stateView = stateView, onError = {
            mBind.srl.finishRefresh()
            mBind.srl.finishLoadMore()
        }) {
            val wowList = Quest.api.getWowList(Quest.getHead("App.Wow.Index") {
                add("page", "$page")
            })
            if (isFirst) {
                mBind.srl.finishRefresh()
                adapter.newData(wowList.items)
                mBind.vp.post {
                    playPosition(0)
                }
            } else {
                mBind.srl.finishLoadMore()
                adapter.addData(wowList.items)
            }
            if (wowList.items.size < 20) {
                isLoadEnd = true
                mBind.srl.finishLoadMoreWithNoMoreData()
            } else {
                isLoadEnd = false
                page++
            }

        }


    }

    override fun lazyInit() {
        super.lazyInit()
        getData(true)
    }

    override fun onResume() {
        super.onResume()
        if (VideoListActivity.OTHER_PLAYER_START) {
            VideoListActivity.OTHER_PLAYER_START = false
            getPlayer()?.let {
                //it.setSeekOnStart(it.seekOnStart)
                it.startPlayLogic()
            }
        } else
            getPlayer()?.onVideoResume(false)

        statusBar(hasImmersive = false, darkFont = false)
    }

    override fun onPause() {
        super.onPause()
        getPlayer()?.onVideoPause()
        statusBar(hasImmersive = false, darkFont = true)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (viewInit) {
            if (hidden) {
                statusBar(hasImmersive = false, darkFont = false)
                getPlayer()?.onVideoResume(false)
            } else {
                statusBar(hasImmersive = false, darkFont = true)
                getPlayer()?.onVideoPause()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}