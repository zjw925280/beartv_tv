package com.gys.play.ui.video

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.blankj.utilcode.util.FragmentUtils
import com.gys.play.R
import com.gys.play.databinding.FragmentCommentVideoBinding
import com.gys.play.ui.comment.CommentListFragment
import com.gys.play.ui.comment.EditCommentViewModel
import com.jccppp.start.argument
import com.jccppp.start.set
import com.jccppp.start.setBundle
import com.mybase.libb.ext.dp2px
import com.mybase.libb.ui.vb.BaseVbFragment
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

class CommentVideoFragment : BaseVbFragment<FragmentCommentVideoBinding>() {

    companion object {

        fun newInstance(resource_id: String) =
            CommentVideoFragment().setBundle {
                it["resource_id"] = resource_id
            }

        fun newMyInstance() =
            CommentVideoFragment().setBundle {
                it["mySelf"] = true
            }
    }


    private val editViewModel by activityViewModels<EditCommentViewModel>()

    private val resource_id by argument("")

    private val mySelf by argument(false)

    private val mFragmentContainerHelper = FragmentContainerHelper()


    override fun initView(savedInstanceState: Bundle?) {

        val fragment =
            (if (mySelf) CommentListFragment.newMyInstance("1") else CommentListFragment.newInstance(
                1,
                resource_id,
                "",
                "1"
            ))

        FragmentUtils.replace(childFragmentManager, fragment, R.id.fl)


        val commonNavigator = CommonNavigator(mContext)
        commonNavigator.scrollPivotX = 0.35f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount() = 2

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = if (index == 0) " 按熱度   " else " 按時間   "
                clipPagerTitleView.textColor = Color.parseColor("#00ACFF")
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.textSize = 12f.dp2px().toFloat()
                clipPagerTitleView.setOnClickListener {
                    if (editViewModel.changeOrderType.value != index) {
                        mFragmentContainerHelper.handlePageSelected(index, true)
                        editViewModel.changeOrderType.value = index
                    }
                }
                return clipPagerTitleView

            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight = 24f.dp2px()
                val borderWidth = UIUtil.dip2px(context, 1.0).toFloat()
                val lineHeight = navigatorHeight - 2 * borderWidth
                indicator.lineHeight = lineHeight
                indicator.roundRadius = lineHeight / 2
                indicator.yOffset = borderWidth
                indicator.setColors(Color.parseColor("#00ACFF"))
                return indicator
            }

        }


        mBind.mi.navigator = commonNavigator


        mFragmentContainerHelper.attachMagicIndicator(mBind.mi)
    }

}