package com.gys.play.wow

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.ctetin.expandabletextviewlibrary.ExpandableTextView
import com.danikula.videocache.HttpProxyCacheServer
import com.gys.play.R
import com.gys.play.coroutines.Quest
import post
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.WowDetailBean
import com.gys.play.ui.LoginActivity
import com.gys.play.ui.comment.CommentWowDialog
import com.gys.play.ui.comment.ShareWowDialog
import com.gys.play.wow.like.LikeButton
import com.gys.play.wow.like.OnLikeListener
import com.jccppp.start.launchAc
import com.mybase.libb.ext.*
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.squareup.okhttp.*
import java.io.IOException

class WowItemHolder(private val context: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val gsyVideoOptionBuilder = GSYVideoOptionBuilder()

    private val TAG = "WowItemlHolder"

    private val gsyVideoPlayer: WowPlayer = itemView.findViewById(R.id.wow_player)

    private val likeLayout: LikeButton = itemView.findViewById(R.id.likeLayout)

    private val tvTitle: ExpandableTextView = itemView.findViewById(R.id.tvTitle)
    private val tvName: TextView = itemView.findViewById(R.id.tvName)

    private val tvComment: TextView = itemView.findViewById(R.id.tvComment)
    private val tvDianZan: TextView = itemView.findViewById(R.id.tvDianZan)

    private val ivUser: ImageView = itemView.findViewById(R.id.ivUser)

    private val llComment: View = itemView.findViewById(R.id.llComment)

    private val llShare: View = itemView.findViewById(R.id.llShare)

    private val addSubscribe: View = itemView.findViewById(R.id.addSubscribe)

    private val ivSubscribeTag: View = itemView.findViewById(R.id.ivSubscribeTag)

    private lateinit var mShareWowDialog: ShareWowDialog

    private val proxyCacheServer by lazy(LazyThreadSafetyMode.NONE) {
        ProxyCacheManager.instance().newProxy(getAppGlobal()) as HttpProxyCacheServer
    }

    private var model: WowDetailBean? = null

    fun onBind(position: Int, model: WowDetailBean) {
        this.model = model
        llShare.setNoDouble {
            if (!::mShareWowDialog.isInitialized) {
                mShareWowDialog = ShareWowDialog.newInstance(model)
            }
            if (mShareWowDialog.isShowing()) {
                return@setNoDouble
            }
            context.getLifeActivity()?.let {
                mShareWowDialog.show(it.supportFragmentManager)
            }
        }

        llComment.setNoDouble(800L) {
            val mCommentWowDialog = CommentWowDialog.newInstance(2, model.id, "")
            context.getLifeActivity()?.let {
                mCommentWowDialog.show(it.supportFragmentManager)
            }
        }

        addSubscribe.setNoDouble {
            if (!SpUserInfo.isLogin()) {
                context.getLifeActivity()?.launchAc<LoginActivity>()

            } else if (!model.isSub) {
                context.getLifeActivity()?.post {
                    Quest.api.addSubscribe(Quest.getHead("App.Wow.Subscribe") {
                        add("wow_id", model.id)
                    })
                    model.isSub = true
                    ivSubscribeTag.showINVISIBLE(false)
                }
            }
        }

        tvTitle.setContent(model.title)

        tvName.text = model.user_name

        tvDianZan.text = model.like_num
        tvComment.text = model.comment_num

        ImgUtil.loadCircle(ivUser, model.avatar)

//        startDownload(model.url)

        gsyVideoOptionBuilder
            .setIsTouchWiget(false) //.setThumbImageView(imageView)
            .setUrl(model.url)
            .setCacheWithPlay(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setLooping(true)
            .setNeedShowWifiTip(false)
            .setPlayTag("${TAG}_$position")
            .setNeedLockFull(false)
            .setPlayPosition(position)
            .setVideoAllCallBack(object : GSYSampleCallBack() {

            }).build(gsyVideoPlayer)

        likeLayout.setLiked(model.isLike)

        gsyVideoPlayer.loadCoverImage(model.cover, R.mipmap.img_4)


        likeLayout.setOnLikeListener(object : OnLikeListener {
            var isLikeing = false//是否正在收藏
            var isCancelLike = false
            override fun liked(likeButton: LikeButton?) {
                if (!SpUserInfo.isLogin()) {
                    context.getLifeActivity()?.launchAc<LoginActivity>()

                } else if (!model.isLike && !isLikeing) {
                    isLikeing = true
                    context.getLifeActivity()?.post(onError = {
                        isLikeing = false
                    }) {
                        Quest.api.setLike(Quest.getHead("App.Wow.Like") {
                            add("wow_id", model.id)
                        })
                        model.like_num = "${model.like_num.getSafeInt(0).plus(1)}"
                        tvDianZan.text = model.like_num
                    }
                }
            }

            override fun unLiked(likeButton: LikeButton?) {
                if (SpUserInfo.isLogin() && model.isLike && !isCancelLike) {
                    isCancelLike = true
                    context.getLifeActivity()?.post(onError = {
                        isCancelLike = false
                    }) {
                        Quest.api.setLike(Quest.getHead("App.Wow.Like") {
                            add("wow_id", model.id)
                        })
                        model.isLike = false
                        likeLayout.setLiked(model.isLike)
                    }
                }
            }
        })

        gsyVideoPlayer.doubleTouchListener = {
            if (SpUserInfo.isLogin()) {

                likeLayout.setLikeAndCallBack(true)
            } else {
                context.getLifeActivity()?.launchAc<LoginActivity>()
            }
        }

        loadLikeDetail()

    }

    fun loadLikeDetail() {
        if (model == null) return

        val model: WowDetailBean = model!!

        if (!model.isLoadDetail && SpUserInfo.getUserInfo().isLogin()) {
            model.isLoadDetail = true
            context.getLifeActivity()?.post(onError = {
                model.isLoadDetail = false
            }) {
                val likeDetail = Quest.api.getLikeDetail(Quest.getHead("App.Wow.Info") {
                    add("wow_id", model.id)
                })

                if (model.isLike != (likeDetail.is_like == 1)) {
                    model.isLike = likeDetail.is_like == 1
                    likeLayout.setLiked(model.isLike)
                }

                if (model.isSub != (likeDetail.is_subscribe == 1)) {
                    model.isSub = likeDetail.is_subscribe == 1
                    ivSubscribeTag.showINVISIBLE(!model.isSub)
                }
            }
        } else if (!SpUserInfo.getUserInfo().isLogin()) {
            if (model.isLoadDetail) {
                model.isLoadDetail = false
            }
            if (model.isLike) {
                model.isLike = false
                likeLayout.setLiked(model.isLike)
            }

            if (model.isSub) {
                model.isSub = false
                ivSubscribeTag.showINVISIBLE(!model.isSub)
            }
        }

    }

    private var newCall: Call? = null

    private fun startDownload(url: String) {

        try {
            val proxyUrl = proxyCacheServer.getProxyUrl(url)

            val build = Request.Builder().url(proxyUrl).build()

            newCall = OkHttpClient().newCall(build)

            newCall?.enqueue(object : Callback {
                override fun onFailure(request: Request?, e: IOException?) {
                    LogUtils.e("11111onFailure ", e)
                }

                override fun onResponse(response: Response?) {
                    LogUtils.e("111111onResponse ", response)
                }
            })
        } catch (e: Throwable) {
            e.printStackTrace()
        }


    }

    private fun stopCache() {
        newCall?.cancel()
        newCall = null
        proxyCacheServer.shutdown()
    }

    fun getPlayer(cancel: Boolean): WowPlayer {
        /* if (cancel)
             stopCache()*/
        return gsyVideoPlayer
    }
}