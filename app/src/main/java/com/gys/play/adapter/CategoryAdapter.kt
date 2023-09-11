package com.gys.play.adapter

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.liba.ui.base.listgroup.MixTypeAdapter
import com.android.liba.ui.base.listgroup.holder.ItemViewDelegate
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.BR
import com.gys.play.Config
import com.gys.play.MyApplication
import com.gys.play.R
import com.gys.play.adapter.base.Adapter
import com.gys.play.databinding.*
import com.gys.play.entity.HomeBannerInfo
import com.gys.play.entity.BtVideoInfo
import com.gys.play.entity.RankList
import com.gys.play.fragment.book_city.style.BtVideoStyle
import com.gys.play.ui.rank.RankActivity
import com.gys.play.util.FreeCountDownUtil
import com.gys.play.util.ImageLoaderUtils
import com.gys.play.util.KvUserInfo
import com.gys.play.util.getBoyColor
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnBannerListener

class CategoryAdapter(data: MutableList<BtVideoInfo>, activity: FragmentActivity) :
    MixTypeAdapter<BtVideoInfo>(activity, data) {
    var itemOperatingListener: ItemOperatingListener? = null

    var bannerListener: ItemBannerListener? = null
    var itemRankListener: ItemRankListener? = null
    var freeLimitListener: ItemFreeLimitListener? = null

    fun addData(data: MutableList<BtVideoInfo>) {
        val start = mDatas.size - 1
        mDatas.addAll(data)
        notifyItemRangeChanged(start, itemCount)
    }

    protected fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        recyclerView?.run {
            return (recyclerView.computeHorizontalScrollExtent() + recyclerView.computeHorizontalScrollOffset() >= recyclerView.computeHorizontalScrollRange())
        }
        return false
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

    var startRank = false

    init {
        BtVideoStyle.addAllStyle(activity, this)

        addItemViewDelegate(
            Config.HOME_OPERATING,
            object : RecommandItemViewDelegate(
                R.layout.item_home_operating,
                Config.HOME_OPERATING,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeOperatingBinding = it.getViewDataBinding()
                        val datas = itemOperatingListener?.getOperatingData()
                        datas?.run {
                            if (activity is LifecycleOwner) {
                                binding.banner.addBannerLifecycleObserver(activity)
                            }
                            binding.banner.viewPager2.isUserInputEnabled = false
                            binding.banner.setAdapter(object :
                                BannerImageAdapter<HomeBannerInfo>(this) {
                                override fun onBindView(
                                    holder: BannerImageHolder?,
                                    data: HomeBannerInfo?,
                                    position: Int,
                                    size: Int
                                ) {
                                    if (holder != null) {
                                        ImageLoaderUtils.loadImageCenterCropWithRadius(
                                            holder!!.imageView, data?.thumb,
                                            radius = 7,
                                            placeholder = R.mipmap.img_default_loading,
                                            error = R.mipmap.img_default_loading
                                        )
                                    }
                                }
                            }).setOnBannerListener(object : OnBannerListener<HomeBannerInfo> {
                                override fun OnBannerClick(data: HomeBannerInfo?, position: Int) {
                                    data?.jump(activity)
                                }
                            })
                        }
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_BANNER,
            object : RecommandItemViewDelegate(
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
            Config.HOME_FREE_LIMIT_EVERYDAY,
            object : RecommandItemViewDelegate(
                R.layout.item_free_limit_heng,
                Config.HOME_FREE_LIMIT_EVERYDAY,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemFreeLimitHengBinding = it.getViewDataBinding()
//                        binding.llGoFree.visibility = View.VISIBLE
                        binding.tvCategory.text = activity.getString(R.string.free_limit)
                        val linearLayoutManager = LinearLayoutManager(activity)
                        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                        binding.rvCategory.layoutManager = linearLayoutManager
                        val list = ArrayList<BtVideoInfo>()
                        item?.list?.let {
                            it.forEach {
                                if (it.free_status != 0) {
                                    list.add(it)
                                }
                            }
                            if (item.remaining_time > 0) {
                                FreeCountDownUtil.start(item.remaining_time)
                                freeLimitListener?.onDoLimit(binding)
                            }
                        }
                        val adapter = FreeLimitHomeAdapter(list, activity)
                        binding.rvCategory.adapter = adapter
                        binding.rvCategory.isNestedScrollingEnabled = false
                    }
                }
            })

        addItemViewDelegate(
            Config.JINGPING_BOOK,
            object : RecommandItemViewDelegate(
                R.layout.person_recommend_header,
                Config.JINGPING_BOOK,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: PersonRecommendHeaderBinding = it.getViewDataBinding()

                        binding.recommendHeader.setImageResource(
                            if (KvUserInfo.lookBoy) {
                                R.mipmap.img_zbtuij_b
                            } else R.mipmap.img_zbtuij_g
                        )

                        binding.sl.setStrokeColor(getBoyColor())

                        var novelList = item?.list

                        binding.virv.models = novelList
                    }
                }
            })
        addItemViewDelegate(
            Config.HOME_MUST_READ,
            object : RecommandItemViewDelegate(
                R.layout.item_home_new_book,
                Config.HOME_MUST_READ,
                0,
                true
            ) {
                override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                    holder?.let {
                        val binding: ItemHomeNewBookBinding = it.getViewDataBinding()
                        var gridLayoutManager = GridLayoutManager(activity, 4)
                        binding.rvNewBook.layoutManager = gridLayoutManager
                        binding.tvCategory.setText(activity.getString(R.string.potential_book))
                        binding.llMore.visibility = View.GONE
                        var list = ArrayList<BtVideoInfo>()
                        item?.list?.let {
                            list.addAll(it)
                        }
                        binding.rvNewBook.adapter =
                            Adapter(BR.item, R.layout.item_common_102_4, list)
//                        binding.llMore.setOnClickListener {
//                            ctx.startActivity(Intent(ctx, FullMarkBookActivity::class.java))
//                        }
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
                    holder?.let {
                        val binding: ItemHomeForYourBinding = it.getViewDataBinding()
                        binding.tvTitle.text = MyApplication.getInstance().getActivityResources()
                            .getString(R.string.detail_like)
                    }
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
                    }
                }
            })
    }

    @Synchronized
    private fun goRankActivity(
        ctx: Activity,
        recyclerView: RecyclerView,
        channelId: Int
    ) {
        if (!startRank) {
            RankActivity.start(ctx)
            recyclerView.smoothScrollBy(-50, 0)
            startRank = true
        }
    }

    interface ItemOperatingListener {
        fun getOperatingData(): MutableList<HomeBannerInfo>
    }

    interface ItemBannerListener {
        fun initBanner(banner: Banner<Any, BannerAdapter<*, *>>)
    }

    interface ItemRankListener {
        fun getRankDatas(): MutableList<RankList>
    }

    interface ItemFreeLimitListener {
        fun onDoLimit(binding: ItemFreeLimitHengBinding)
    }
}