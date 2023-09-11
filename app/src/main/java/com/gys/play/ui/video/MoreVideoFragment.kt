package com.gys.play.ui.video

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.annotaion.DividerOrientation
import com.drake.brv.layoutmanager.HoverGridLayoutManager
import com.drake.brv.utils.divider
import com.drake.brv.utils.linear
import com.gys.play.R
import com.gys.play.coroutines.Quest
import com.gys.play.databinding.FragmentMoreVideoBinding
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.dp2px
import com.mybase.libb.ext.setNoDouble
import com.mybase.libb.ui.vb.BaseVbFragment

class MoreVideoFragment : BaseVbFragment<FragmentMoreVideoBinding>() {

    private val viewModel by activityViewModels<VideoViewModel>()

    private val cid by argument(0)

    private val chooseAdapter by lazy(LazyThreadSafetyMode.NONE) { ChooseVideoAdapter() }

    private lateinit var mAdapter: BindingAdapter

    private val mChooseVideoPop by lazy(LazyThreadSafetyMode.NONE) { ChooseVideoPop(mActivity) }

    //控制滑到的选集的指定position
    //private var rvAnthology: RecyclerView? = null

    companion object {
        fun newInstance(cid: Int) = MoreVideoFragment().setBundle { it["cid"] = cid }
    }

    override fun initView(savedInstanceState: Bundle?) {

        chooseAdapter.onClick(R.id.parent) {
            if (chooseAdapter.playIndex != modelPosition) viewModel.playPosition.value =
                modelPosition
            if (mChooseVideoPop.isShowing) {
                mChooseVideoPop.dismiss()
            }
        }

        mBind.rvAnthology.linear(RecyclerView.HORIZONTAL).adapter = chooseAdapter

        mBind.prv.setEnableRefresh(false)
        mBind.prv.setEnableLoadMore(false)
        mBind.prv.config {
            mBind.prv.setLimitSize(if (it == 1) 6 else 20)
            val guessList = Quest.api.getGuessList(Quest.getHead("App.ShortTv.GuessLike") {
                add("page", "$it").add("limit", if (it == 1) "6" else "20").add("cid", "$cid")
            })

            if (it == 1 && guessList.items.size >= 6) {
                mAdapter.addFooter("")
            }

            guessList.items
        }

        (mBind.prv.rv.adapter as? BindingAdapter)?.let {
            mAdapter = it

            val hoverGridLayoutManager = HoverGridLayoutManager(context, 2)

            hoverGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (mAdapter.isHeader(position)) return 2
                    if (mAdapter.isFooter(position)) return 2
                    return 1
                }
            }

            mBind.prv.rv.setPadding(16f.dp2px(), 0, 16f.dp2px(), 0)
            mBind.prv.rv.divider {
                setDrawable(R.drawable.divider_size_8)
//                endVisible = true
                orientation = DividerOrientation.GRID
            }

            mBind.prv.rv.layoutManager = hoverGridLayoutManager

//            it.addType<VideoDetailBean>(R.layout.include_more_video_header)

            it.addType<String>(R.layout.include_more_video_footer)

            it.onBind {
                /*  if (itemViewType == R.layout.include_more_video_header) {
                      val binding = getBinding<IncludeMoreVideoHeaderBinding>()
                      rvAnthology = binding.rvAnthology
                      binding.bean = getModel()
                      binding.rvAnthology.linear(RecyclerView.HORIZONTAL).adapter = chooseAdapter
                  } */

                if (itemViewType == R.layout.include_more_video_footer) {
                    findView<TextView>(R.id.tvMore).setNoDouble {
                        mAdapter.removeFooter("")
                        mBind.prv.setEnableLoadMore(true)
                        mBind.prv.prl.autoLoadMore()
                    }
                }
            }

        }


    }

    override fun initData(tag: Any?) {
        super.initData(tag)

        mBind.prv.onRestart()
    }


    override fun observeUi(owner: LifecycleOwner) {
        super.observeUi(owner)
        viewModel.playPosition.observe(owner) {
            var playIndex = chooseAdapter.playIndex
            if (playIndex != it) {
                if (playIndex > -1) {
                    val linshi = playIndex
                    chooseAdapter.notifyItemChanged(linshi)
                }
                playIndex = it
                chooseAdapter.playIndex = playIndex
                chooseAdapter.notifyItemChanged(playIndex)

                if (chooseAdapter.playIndex >= 0)
                    mBind.rvAnthology?.postDelayed({
                        mBind.rvAnthology?.scrollToPosition(playIndex)
                    }, 200)

            }
        }

        viewModel.videoDetailBean.observe(owner) {
            chooseAdapter.payType = it.pay_type
            chooseAdapter.models = it.chapter_list

            mBind.bean = it

            /*
               if (!mAdapter.isHeader(0)) {
                   mAdapter.addHeader(it, index = 0)
               } else {
                   mAdapter.removeHeader(0, false)
                   mAdapter.addHeader(it, index = 0)
               }
                */
        }

        viewModel.showChoosePop.observe(owner) {
            if (it)
                mChooseVideoPop.show(chooseAdapter)
            else if (mChooseVideoPop.isShowing) {
                mChooseVideoPop.dismiss()
            }

        }
    }

}