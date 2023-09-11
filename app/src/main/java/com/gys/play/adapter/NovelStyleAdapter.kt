package com.gys.play.adapter

import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.MixTypeAdapter
import com.android.liba.ui.base.listgroup.holder.ItemViewDelegate
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.databinding.ItemHomeForYourBinding
import com.gys.play.databinding.ItemHomeForYourListBinding
import com.gys.play.databinding.ItemHomeShortTvTypeBinding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.fragment.book_city.style.BtVideoStyle
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter


class NovelStyleAdapter(data: MutableList<BtVideoInfo>, val activity: FragmentActivity) :
    MixTypeAdapter<BtVideoInfo>(activity, data) {

//    var bannerListener: ItemBannerListener? = null

    fun addData(data: MutableList<BtVideoInfo>) {
        val start = mDatas.size - 1
        mDatas.addAll(data)
        notifyItemRangeChanged(start, itemCount)
    }

    abstract class NewUserReadItemViewDelegate(
        var layoutId: Int,
        var itemType: Int,
        var spanSize: Int,
        var fullSpan: Boolean
    ) : ItemViewDelegate<BtVideoInfo> {
        override fun getItemViewLayoutId(): Int {
            return layoutId;
        }

        override fun isForViewType(item: BtVideoInfo?, Int: Int): Boolean {
            return item?.datatype == itemType
        }

        override fun isFullSpan(): Boolean {
            return fullSpan;
        }

        override fun getSpanCount(): Int {
            return spanSize;
        }
    }

    init {
        BtVideoStyle.addAllStyle(activity, this)

        addItemViewDelegate(
            Config.HOME_SHORT_TV_TYPE,
            object : NewUserReadItemViewDelegate(
                R.layout.item_home_short_tv_type,
                Config.HOME_SHORT_TV_TYPE,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeShortTvTypeBinding = it.getViewDataBinding()
                        binding.type4.isSelected = item?.shortTvType == 4
                        binding.type5.isSelected = item?.shortTvType == 5
                        binding.type4.setOnClickListener {
                            item?.shortTvType = 4
                            item?.listener?.select(4)
                            notifyItemChanged(position)
                        }
                        binding.type5.setOnClickListener {
                            item?.shortTvType = 5
                            item?.listener?.select(5)
                            notifyItemChanged(position)
                        }
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_SAVE_SHORTAGE_BOOK,
            object : NewUserReadItemViewDelegate(
                R.layout.item_home_for_your,
                Config.HOME_SAVE_SHORTAGE_BOOK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeForYourBinding = it.getViewDataBinding()
                        binding.tvTitle.text = MyApplication.getInstance().getActivityResources()
                            .getString(R.string.save_shortage)
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_FOR_YOUR_LIST,
            object : NewUserReadItemViewDelegate(
                R.layout.item_home_for_your_list,
                Config.HOME_FOR_YOUR_LIST,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeForYourListBinding = it.getViewDataBinding()
                        binding.item = item
//                        if (position == mDatas.size - 1) {
//                            binding.rlBg.setBackgroundResource(R.drawable.shape_rect_ffffff_bottom_left_right_12)
//                        } else {
//                            binding.rlBg.setBackgroundResource(R.drawable.shape_rect_ffffff)
//                        }
                    }
                }
            })
    }

    interface ItemBannerListener {
        fun initBanner(banner: Banner<Any, BannerAdapter<*, *>>)
    }
}