package com.gys.play.ui.video

import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.gys.play.R
import com.gys.play.coroutines.Quest
import post
import com.gys.play.databinding.PopVideoCoinBayBinding
import com.gys.play.entity.SpUserInfo
import com.gys.play.entity.VideoDetailBean
import com.gys.play.ui.LoginActivity
import com.gys.play.util.LiveDataUtil
import com.jccppp.start.launchAc
import com.mybase.libb.ext.ImgUtil
import com.mybase.libb.ext.notEmpty
import com.mybase.libb.ext.setNoDouble
import razerdp.basepopup.BasePopupWindow
import razerdp.util.animation.AnimationHelper
import razerdp.util.animation.TranslationConfig

class CoinBayPop(private val mContext: FragmentActivity, private val paySuccess: (Int) -> Unit) :
    BasePopupWindow(mContext) {

    private val viewModel by mContext.viewModels<VideoViewModel>()

    init {
        popupGravity = Gravity.BOTTOM

        contentView = createPopupById(R.layout.pop_video_coin_bay)

        setOutSideDismiss(false)

        setBackPressEnable(false)

//        setBackgroundColor(Color.TRANSPARENT)
    }

    private lateinit var mBind: PopVideoCoinBayBinding

    private var loginTag = false

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)
        mBind = PopVideoCoinBayBinding.bind(contentView)

        mBind.ivClose.setNoDouble {
            dismiss()
        }

        var imgPath = ""

        val adapter = mBind.rvPay.linear().setup {
            addType<VideoDetailBean.VideoDetailChapterBean>(R.layout.item_bay_coins)
            onBind {
                val item = getModel<VideoDetailBean.VideoDetailChapterBean>()
                findView<TextView>(R.id.tvCoins).text = "${item.coins}金幣"
                findView<TextView>(R.id.tvName).text = "${item.title}"
                findView<TextView>(R.id.tvNum).text = "第${index.plus(1)}集"
                if (imgPath.notEmpty())
                    ImgUtil.loadCorner(findView(R.id.ivThumb), imgPath, 6f)
            }
        }

        viewModel.videoDetailBean.observe(context as LifecycleOwner) {
            imgPath = it.cover
            adapter.notifyDataSetChanged()
        }

        LiveDataUtil.userInfo.observe(mContext) {
            if (loginTag) {
                loginTag = false
                paySuccess.invoke(index)
                dismiss()
            }

        }

    }

    private var index = 0


    fun show(index: Int, bean: VideoDetailBean.VideoDetailChapterBean) {
        this.index = index
        mBind.rvPay.models = arrayListOf(bean)
        mBind.tvMoney.text = "合計${bean.coins}金幣"
        mBind.tvSubmit.setNoDouble {
            if (!SpUserInfo.isLogin()) {
                mContext.launchAc<LoginActivity>()
                loginTag = true
            } else {
                mContext.post(showLoading = true) {

                    Quest.api.payVideo(Quest.getHead("App.ShortTv.Buy") {
                        add("chapter_id", bean.id)
                    })
                    paySuccess.invoke(index)
                    dismiss()
                }
            }
        }
        if (!isShowing)
            showPopupWindow()

    }


    override fun onCreateShowAnimation(): Animation? {
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.FROM_BOTTOM)
            .toShow()
    }

    override fun onCreateDismissAnimation(): Animation? {
        return AnimationHelper.asAnimation()
            .withTranslation(TranslationConfig.TO_BOTTOM)
            .toDismiss()
    }
}