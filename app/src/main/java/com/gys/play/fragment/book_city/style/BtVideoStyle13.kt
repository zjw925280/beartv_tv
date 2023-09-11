package com.gys.play.fragment.book_city.style

import androidx.fragment.app.FragmentActivity
import com.android.liba.ui.base.listgroup.holder.ViewHolder
import com.gys.play.Config
import com.gys.play.R
import com.gys.play.adapter.NovelStyleAdapter
import com.gys.play.databinding.ItemNovelStyle13Binding
import com.gys.play.entity.BtVideoInfo
import com.gys.play.util.ImageLoaderUtils
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener

object BtVideoStyle13 {

    fun getType(): Int {
        return Config.HOME_NOVEL_STYLE_13
    }

    fun getItemViewDelegate(activity: FragmentActivity): NovelStyleAdapter.NewUserReadItemViewDelegate {
        return object : NovelStyleAdapter.NewUserReadItemViewDelegate(
            R.layout.item_novel_style_13,
            getType(),
            0,
            true
        ) {
            override fun convert(holder: ViewHolder?, item: BtVideoInfo?, position: Int) {
                holder?.let {
                    val binding: ItemNovelStyle13Binding = it.getViewDataBinding()

                    binding.banner.addBannerLifecycleObserver(activity)
                        .setAdapter(object : BannerImageAdapter<BtVideoInfo>(item!!.list) {
                            override fun onBindView(
                                holder: BannerImageHolder?,
                                data: BtVideoInfo?,
                                position: Int,
                                size: Int
                            ) {
                                if (holder != null) {
                                    ImageLoaderUtils.loadImageCenterCropWithRadius(
                                        holder.imageView, data?.cover,
                                        radius = 7,
                                        placeholder = R.mipmap.img_default_loading,
                                        error = R.mipmap.img_default_loading
                                    )
                                }
                            }
                        })
                        .setIndicator(CircleIndicator(activity))
                        .setIndicatorNormalColorRes(R.color.C_CCEBCC)
                        .setIndicatorSelectedColorRes(R.color.key_color)
                        .setBannerGalleryEffect(8, 8)
                        .isAutoLoop(true)
                    binding.banner.adapter.setOnBannerListener(object :
                        OnBannerListener<BtVideoInfo> {
                        override fun OnBannerClick(data: BtVideoInfo?, position: Int) {
                            data?.jump(activity)
                        }
                    })
                }
            }
        }
    }
}