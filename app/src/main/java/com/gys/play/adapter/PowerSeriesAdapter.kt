package com.gys.play.adapter

import android.app.Activity
import android.view.View
import com.android.liba.ui.base.listgroup.MixTypeAdapter
import com.android.liba.ui.base.listgroup.holder.ItemViewDelegate
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.databinding.*
import com.gys.play.entity.BtVideoInfo
import com.gys.play.util.ConversionUtil
import com.gys.play.util.ImageLoaderUtils
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter

class PowerSeriesAdapter(data: MutableList<BtVideoInfo>, ctx: Activity) :
    MixTypeAdapter<BtVideoInfo>(ctx, data) {

    var bannerListener: ItemBannerListener? = null

    fun addData(data: MutableList<BtVideoInfo>) {
        val start = mDatas.size - 1
        mDatas.addAll(data)
        notifyItemRangeChanged(start, itemCount)
    }

    open inner abstract class PowerItemViewDelegate(
        var layoutId: Int,
        var itemType: Int,
        var spanSize: Int,
        var fullSpan: Boolean
    ) : ItemViewDelegate<BtVideoInfo> {
        override fun getItemViewLayoutId(): Int {
            return layoutId;
        }

        override fun isForViewType(item: BtVideoInfo?, Int: Int): Boolean {
            return item?.datatype == itemType;
        }

        override fun isFullSpan(): Boolean {
            return fullSpan;
        }

        override fun getSpanCount(): Int {
            return spanSize;
        }
    }

    init {
        addItemViewDelegate(
            Config.HOME_BANNER,
            object : PowerItemViewDelegate(
                R.layout.item_novel_style_12,
                Config.HOME_BANNER,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemNovelStyle12Binding = it.getViewDataBinding()
                        bannerListener?.initBanner(binding.banner)
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_SEX_18NEW,
            object : PowerItemViewDelegate(
                R.layout.item_power_series,
                Config.HOME_SEX_18NEW,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemPowerSeriesBinding = it.getViewDataBinding()
                        binding.tvCategory.text = ctx.getString(R.string.new_book)
                        var novelList = item?.list
                        var infoList = ArrayList<BtVideoInfo>()
                        novelList?.let {
                            if (it.size > 0) {
                                binding.llView.visibility = View.VISIBLE
                                var info = it.get(0)
                                binding.tvMustName.text = info.title
                                binding.tvMustScore.text = info.score.toString()
                                binding.tvMustDes.text = info.description
                                binding.tvMustType.text = info.category_name + "·"
                                if (info.total_views > 1000)
                                    binding.tvMustNum.text =
                                        ConversionUtil.LongToWString(info.total_views) + "k"
                                else binding.tvMustNum.text = info.total_views.toString()
                                ImageLoaderUtils.loadImageCenterCropWithRadius(
                                    binding.imgMust, info.cover,
                                    radius = 5,
                                    placeholder = R.mipmap.img_default_loading,
                                    error = R.mipmap.img_default_loading
                                )
                                for (pos in 1..it.size - 1) {
                                    infoList.add(it.get(pos))
                                }
                                binding.llMustRead.setOnClickListener {
                                    info?.goBookDetail(it)
                                }
                            } else {
                                binding.llView.visibility = View.GONE
                            }
                        }
                        binding.virv.models = infoList
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_SAVE_SHORTAGE_BOOK,
            object : PowerItemViewDelegate(
                R.layout.item_home_for_your,
                Config.HOME_SAVE_SHORTAGE_BOOK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {

                }
            })
        addItemViewDelegate(
            Config.HOME_FOR_YOUR_LIST,
            object : PowerItemViewDelegate(
                R.layout.item_home_for_your_list,
                Config.HOME_FOR_YOUR_LIST,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeForYourListBinding = it.getViewDataBinding()
                        binding.item = item
                        if (position == mDatas.size - 1) {
                            binding.rlBg.setBackgroundResource(R.drawable.shape_rect_ffffff_bottom_left_right_12)
                        } else {
                            binding.rlBg.setBackgroundResource(R.drawable.shape_rect_ffffff)
                        }
                    }
                }
            })
    }

    interface ItemBannerListener {
        fun initBanner(banner: Banner<Any, BannerAdapter<*, *>>)
    }
}