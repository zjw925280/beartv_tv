package com.gys.play.wow

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Surface
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mybase.libb.ext.showGONE
import com.mybase.libb.ext.showVISIBLE
import com.gys.play.R
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class WowPlayer : StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var mCoverImage: ImageView? = null

    private var startPlay: ImageView? = null

    var doubleTouchListener: ((e: MotionEvent?) -> Unit)? = null

    private var isDoubleTouch = false

    private val mRunnable by lazy(LazyThreadSafetyMode.NONE) { Runnable { isDoubleTouch = false } }

    private val mGestureDetector by lazy {
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onDoubleTap(e: MotionEvent): Boolean {
                doubleTouchListener?.invoke(e)
                isDoubleTouch = true
                mInnerHandler.removeCallbacks(mRunnable)
                mInnerHandler.postDelayed(mRunnable, 500L)
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (!isDoubleTouch) {
                    clickStartIcon()
                }
                return super.onSingleTapConfirmed(e)
            }
        })
    }

    override fun init(context: Context?) {
        super.init(context)

        findViewById<View>(R.id.touchView).setOnTouchListener { v, event ->
            mGestureDetector.onTouchEvent(event)
            true
        }

        mCoverImage = findViewById(R.id.thumbImage)

        startPlay = findViewById(R.id.startPlay)

        startPlay?.setOnClickListener {
            clickStartIcon()
        }

        if (mThumbImageViewLayout != null &&
            (mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)
        ) {
            mThumbImageViewLayout.visibility = VISIBLE
        }
    }

    override fun updateStartImage() {
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            startPlay?.showVISIBLE()
            startPlay?.setImageResource(R.mipmap.icon_zt)
        } else {
            startPlay?.showGONE()
        }
    }

    fun loadCoverImage(url: String, res: Int) {
        mCoverImage ?: return
        Glide.with(mCoverImage!!.context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .centerCrop()
                    .error(res)
                    .placeholder(res)
            )
            .load(url)
            .into(mCoverImage!!)
    }

    override fun getLayoutId(): Int {
        return R.layout.wow_video_player
    }

    override fun onSurfaceUpdated(surface: Surface?) {
        super.onSurfaceUpdated(surface)
        if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == VISIBLE) {
            mThumbImageViewLayout.visibility = INVISIBLE
        }
    }

    override fun setViewShowState(view: View?, visibility: Int) {
        if (view === mThumbImageViewLayout && visibility != VISIBLE) {
            return
        }
        if (view?.id == R.id.bottom_progressbar && visibility != VISIBLE) {
            return
        }
        super.setViewShowState(view, visibility)
    }

    override fun onSurfaceAvailable(surface: Surface?) {
        super.onSurfaceAvailable(surface)
        if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE) {
            if (mThumbImageViewLayout != null && mThumbImageViewLayout.visibility == VISIBLE) {
                mThumbImageViewLayout.visibility = INVISIBLE
            }
        }
    }


    override fun setProgressAndTime(
        progress: Long,
        secProgress: Long,
        currentTime: Long,
        totalTime: Long,
        forceChange: Boolean
    ) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime, forceChange)
        if (mHadSeekTouch) {
            return
        }

        if (mBottomProgressBar != null) {
            if (progress != 0L || forceChange) mBottomProgressBar.progress = progress.toInt()
            setSecondaryProgress(secProgress)
        }
    }

    override fun touchDoubleUp(e: MotionEvent?) {
//        super.touchDoubleUp(e)
    }
}