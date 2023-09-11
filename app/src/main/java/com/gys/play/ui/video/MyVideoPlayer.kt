package com.gys.play.ui.video

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gys.play.R
import com.gys.play.entity.VideoDetailBean.VideoDetailChapterBean
import com.mybase.libb.ext.*
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import moe.codeest.enviews.ENDownloadView

class MyVideoPlayer : StandardGSYVideoPlayer, GSYVideoProgressListener {

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag)

    private var mUriList: List<VideoDetailChapterBean> = ArrayList()

    private var mPlayPosition = 0

    private var mCoverImage: ImageView? = null

    private var ivCoin: ImageView? = null

    private var videoId = ""

    fun setVideoId(videoId: String) {
        this.videoId = videoId
    }

    var showBayCoin = false
        set(value) {
            field = value
            ivCoin?.showGONE(value)
        }

    var showVipHint = false
        set(value) {
            field = value
            if (value) {
                vipHintPop?.showVISIBLE()
                onVideoPause()
            } else {
                vipHintPop?.showGONE()
                onVideoResume()
            }
        }

    override fun getLayoutId(): Int {
        return if (mIfCurrentIsFullscreen) {
            R.layout.custom_my_video_player_full
        } else R.layout.custom_my_video_player
    }

    private lateinit var startBtn: ImageView

    private var ivPlayNext: ImageView? = null

    private var tvChoose: TextView? = null

    private val mSpeedPop by lazy(LazyThreadSafetyMode.NONE) {
        SpeedPop(context, speed) {
            speed = it
        }
    }

    private var vipHintPop: VipHintView? = null

    var mListener: MyPlayerListener? = null

    private var videoBayHelper: VideoBayHelper? = null

    override fun init(context: Context?) {
        super.init(context)

        setGSYVideoProgressListener(this)

        vipHintPop = findViewById(R.id.viv)

        startBtn = findViewById(R.id.ivStart)

        mCoverImage = findViewById(R.id.thumbImage)

        tvChoose = findViewById(R.id.tvChoose)

        ivCoin = findViewById(R.id.ivCoin)

        ivPlayNext = findViewById(R.id.ivPlayNext)

        startBtn.setOnClickListener {
            clickStartIcon()
        }

        findViewById<View?>(R.id.openSeed)?.setNoDouble {
            mSpeedPop.showPopupWindow()
        }

        ivPlayNext?.setOnClickListener {
            playForIndex(mPlayPosition.plus(1))
        }

        tvChoose?.setNoDouble {
            mListener?.showChoosePop()
        }

        vipHintPop?.setBackListener {
            if (!GSYVideoManager.backFromWindowFull(context)) {
                context?.getLifeActivity()?.let {
                    it.finish()
                }
            }
        }


    }

    fun setUp(
        payType: Int?,
        url: List<VideoDetailChapterBean>?,
        position: Int,
        changeState: Boolean = true,
    ): Boolean {
        if (url.isNullOrEmpty()) return false
        if (videoBayHelper == null && payType != null)
            videoBayHelper = VideoBayHelper(this, payType)
        mUriList = url
        mPlayPosition = position
        val gsyVideoModel = url[position]
        val set =
            setUp(gsyVideoModel.url, true, null, gsyVideoModel.title, changeState)
        if (!TextUtils.isEmpty(gsyVideoModel.title) && mTitleTextView != null) {
            mTitleTextView.text = gsyVideoModel.title
        }
        return set
    }

    fun loadCoverImage(url: String, res: Int) {

        val load = Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .centerCrop()
                    .error(res)
                    .placeholder(res)
            )
            .load(url)
        mCoverImage?.let {
            load.into(it)
        }
        ivCoin?.let {
            load.into(it)
        }
    }

    override fun getEnlargeImageRes(): Int {
        return R.mipmap.icon_hp
    }

    override fun getShrinkImageRes(): Int {
        return R.mipmap.icon_hp
    }

    override fun cloneParams(from: GSYBaseVideoPlayer?, to: GSYBaseVideoPlayer?) {
        super.cloneParams(from, to)

        val sf = from as MyVideoPlayer
        val st = to as MyVideoPlayer
        st.mPlayPosition = sf.mPlayPosition
        st.mUriList = sf.mUriList
        st.mListener = sf.mListener
        st.showVipHint = sf.showVipHint
        st.videoBayHelper = sf.videoBayHelper
        st.showBayCoin = sf.showBayCoin
        st.videoId = sf.videoId
    }

    override fun resolveNormalVideoShow(
        oldF: View?,
        vp: ViewGroup?,
        gsyVideoPlayer: GSYVideoPlayer?
    ) {
        if (gsyVideoPlayer is MyVideoPlayer && gsyVideoPlayer.mSpeedPop.isShowing) {
            gsyVideoPlayer.mSpeedPop.dismiss()
        }
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)

    }

    override fun onVideoPause() {
        super.onVideoPause()
    }

    override fun onVideoResume(seek: Boolean) {
        if (!showVipHint && !showBayCoin)
            super.onVideoResume(seek)
    }


    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer? {
        val gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar)
        if (gsyBaseVideoPlayer != null) {
            val listGSYVideoPlayer = gsyBaseVideoPlayer as MyVideoPlayer
            val gsyVideoModel: VideoDetailChapterBean = mUriList[mPlayPosition]
            if (!TextUtils.isEmpty(gsyVideoModel.title) && mTitleTextView != null) {
                listGSYVideoPlayer.mTitleTextView.text = gsyVideoModel.title
            }
        }

        return gsyBaseVideoPlayer
    }

    override fun updateStartImage() {
        super.updateStartImage()
        val imageView = startBtn
        if (mCurrentState == CURRENT_STATE_PLAYING) {
            imageView.setImageResource(R.mipmap.icon_bf)
        } else if (mCurrentState == CURRENT_STATE_ERROR) {
            imageView.setImageResource(R.mipmap.icon_zt)
        } else {
            imageView.setImageResource(R.mipmap.icon_zt)
        }
    }

    private fun playNext(): Boolean {
        return playForIndex(mPlayPosition.plus(1))
    }

    fun playForIndex(index: Int): Boolean {
        if (index < mUriList.size) {

            checkStart(index) {
                if (mPlayPosition != index) {
                    val gsyVideoModel = mUriList[index]
                    mPlayPosition = index
                    mListener?.playChange(index)
                    mSaveChangeViewTIme = 0
                    setUp(null, mUriList, index, false)
                    if (!TextUtils.isEmpty(gsyVideoModel.title) && mTitleTextView != null) {
                        mTitleTextView.text = gsyVideoModel.title
                    }
                }
            }
            return true
        }
        return false
    }

    //检查播放权限
    fun checkStart(index: Int, onNext: (() -> Unit)? = null) {
        val checkNeedBuy = videoBayHelper?.checkNeedBuy(mUriList[index])

        if (checkNeedBuy == 1) {
            onVideoPause()
            showBayCoin = true
            var delay = 0L
            if (mIfCurrentIsFullscreen) {
                GSYVideoManager.backFromWindowFull(context)
                delay = 500L
            }

            onNext?.invoke()
            mInnerHandler.postDelayed({
                mListener?.showBayForCoin(index, mUriList[index])
            }, delay)

        } else {
            showBayCoin = false
            onNext?.invoke()

            startPlayLogic()
        }
    }

    override fun onCompletion() {
        releaseNetWorkState()
        if (mPlayPosition < mUriList.size) {
            return
        }
        super.onCompletion()
    }

    override fun prepareVideo() {
        super.prepareVideo()
        if (mHadPlay && mPlayPosition < mUriList.size) {
            setViewShowState(mLoadingProgressBar, VISIBLE)
            if (mLoadingProgressBar is ENDownloadView) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }

    override fun changeUiToNormal() {
        super.changeUiToNormal()
        if (mHadPlay && mPlayPosition < mUriList.size) {
            setViewShowState(mThumbImageViewLayout, GONE)
            setViewShowState(mTopContainer, INVISIBLE)
            setViewShowState(mBottomContainer, INVISIBLE)
            setViewShowState(mStartButton, GONE)
            setViewShowState(mLoadingProgressBar, VISIBLE)
            setViewShowState(mBottomProgressBar, INVISIBLE)
            setViewShowState(mLockScreen, GONE)
            if (mLoadingProgressBar is ENDownloadView) {
                (mLoadingProgressBar as ENDownloadView).start()
            }
        }
    }

    override fun onAutoCompletion() {
        if (playNext()) {
            return
        }
        super.onAutoCompletion()
    }


    fun getCurrentPosition() = mPlayPosition

    override fun onProgress(
        progress: Long,
        secProgress: Long,
        currentPosition: Long,
        duration: Long
    ) {

        handlerTime(currentPosition)

        VideoKv.recordHistory(
            videoId,
            "${mUriList[mPlayPosition].id}",
            mPlayPosition,
            progress.toInt(), currentPosition
        )

    }

    fun handlerTime(currentPosition: Long) {
        videoBayHelper?.handlerTime(
            mUriList[mPlayPosition],
            currentPosition
        )
    }

}