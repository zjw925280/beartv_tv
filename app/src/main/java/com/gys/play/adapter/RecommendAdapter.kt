package com.gys.play.adapter

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.android.liba.ui.base.listgroup.MixTypeAdapter
import com.android.liba.ui.base.listgroup.holder.ItemViewDelegate
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.android.liba.ui.widget.tabLayout.SlidingTabLayout
import com.gys.play.BR
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.*
import com.gys.play.entity.BtVideoInfo
import com.gys.play.ui.rank.RankActivity
import com.gys.play.util.ConversionUtil
import com.gys.play.util.ImageLoaderUtils
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter

class RecommendAdapter(data: MutableList<BtVideoInfo>, ctx: Activity) :
    MixTypeAdapter<BtVideoInfo>(ctx, data) {

    var bannerListener: ItemBannerListener? = null
    var rankListener: ItemRankListener? = null
    var freeLimitListener: ItemFreeLimitListener? = null
    var itemBookShelfListener: ItemBookShelfListener? = null

    fun addData(data: MutableList<BtVideoInfo>) {
        val start = mDatas.size - 1
        mDatas.addAll(data)
        notifyItemRangeChanged(start, itemCount)
    }

    open inner abstract class RecommandItemViewDelegate(
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
            object : RecommandItemViewDelegate(
                R.layout.item_home_banner,
                Config.HOME_BANNER,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeBannerBinding = it.getViewDataBinding()
                        bannerListener?.initBanner(binding.banner)
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_RANK,
            object : RecommandItemViewDelegate(
                R.layout.item_home_rank,
                Config.HOME_RANK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeRankBinding = it.getViewDataBinding()
                        rankListener?.initRank(binding.segTab, binding.vpView)
                        binding.llGoRank.setOnClickListener {
                            MyApplication.getAnalytics().setHomeTab("查看更多点击量")
                            RankActivity.start(ctx)
                        }
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_MUST_READ,
            object : RecommandItemViewDelegate(
                R.layout.item_home_must_read,
                Config.HOME_MUST_READ,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeMustReadBinding = it.getViewDataBinding()
                        binding.tvCategory.text = ctx.getString(R.string.must_read)
                        var novelList = item?.list
                        var infoList = ArrayList<BtVideoInfo>()
                        novelList?.let {
                            if (it.size > 0) {
                                binding.llView.visibility = View.VISIBLE
                                var info = it.get(0)
                                binding.tvMustName.text = info.title
                                binding.tvMustScore.text = info.score.toString()
                                binding.tvMustDes.text = info.description
                                binding.tvMustType.text = info.category_name
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
                        var gridLayoutManager = GridLayoutManager(ctx, 3)
                        binding.rvMustRead.layoutManager = gridLayoutManager
                        binding.rvMustRead.isNestedScrollingEnabled = false
                        binding.rvMustRead.adapter =
                            Adapter(BR.item, R.layout.item_home_must_read_other, infoList)
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_OVER_BOOK,
            object : RecommandItemViewDelegate(
                R.layout.item_home_must_read,
                Config.HOME_OVER_BOOK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeMustReadBinding = it.getViewDataBinding()
                        binding.tvCategory.text = ctx.getString(R.string.over_book)
                        var novelList = item?.list
                        var infoList = ArrayList<BtVideoInfo>()
                        novelList?.let {
                            if (novelList.size > 0) {
                                var info = it.get(0)
                                binding.tvMustName.text = info.title
                                binding.tvMustScore.text = info.score.toString()
                                binding.tvMustDes.text = info.description
                                binding.tvMustType.text = info.category_name
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
                            }
                        }
                        var gridLayoutManager = GridLayoutManager(ctx, 3)
                        binding.rvMustRead.layoutManager = gridLayoutManager
                        binding.rvMustRead.isNestedScrollingEnabled = false
                        binding.rvMustRead.adapter =
                            Adapter(BR.item, R.layout.item_home_must_read_other, infoList)
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_FREE_LIMIT,
            object : RecommandItemViewDelegate(
                R.layout.item_free_limit_heng,
                Config.HOME_FREE_LIMIT,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_FREE_LIMIT_EVERYDAY,
            object : RecommandItemViewDelegate(
                R.layout.item_free_limit_heng,
                Config.HOME_FREE_LIMIT_EVERYDAY,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_NEW_BOOK,
            object : RecommandItemViewDelegate(
                R.layout.item_home_new_book,
                Config.HOME_NEW_BOOK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeNewBookBinding = it.getViewDataBinding()
                        var gridLayoutManager = GridLayoutManager(ctx, 3)
                        binding.rvNewBook.layoutManager = gridLayoutManager
                        binding.rvNewBook.isNestedScrollingEnabled = false
                        var list = ArrayList<BtVideoInfo>()
                        item?.let {
                            list.addAll(it.list!!)
                        }
                        binding.rvNewBook.adapter =
                            Adapter(BR.item, R.layout.item_home_new_book_list, list)
                        binding.llChange.setOnClickListener {
                            freeLimitListener?.onChangeNewBook(item, binding.imgChange)
                        }
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_FOR_YOUR,
            object : RecommandItemViewDelegate(
                R.layout.item_home_for_your,
                Config.HOME_FOR_YOUR,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {

                }
            })
        addItemViewDelegate(
            Config.HOME_FOR_YOUR_LIST,
            object : RecommandItemViewDelegate(
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

    interface ItemRankListener {
        fun initRank(seg_tab: SlidingTabLayout, vp_view: ViewPager)
    }

    interface ItemFreeLimitListener {
        fun onDoLimit(binding: ItemFreeLimitHengBinding)
        fun onChangeNewBook(item: BtVideoInfo?, img: ImageView)
    }

    interface ItemBookShelfListener {
        fun addBookShyelf(novelInfo: BtVideoInfo)
    }
}