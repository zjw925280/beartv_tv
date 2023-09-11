package com.gys.play.fragment.home

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.gys.play.R
import com.gys.play.entity.HomeBannerInfo
import com.gys.play.util.ImageLoaderUtils
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener

class PositionBanner {
    constructor(
        lifecycleOwner: LifecycleOwner,
        ctx: Context,
        banner: Banner<Any, BannerAdapter<*, *>>,
        list: List<HomeBannerInfo>
    ) {
        banner.addBannerLifecycleObserver(lifecycleOwner)
            .setAdapter(object : BannerImageAdapter<HomeBannerInfo>(list) {
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
            })
            .setIndicator(CircleIndicator(ctx))
            .setIndicatorNormalColorRes(R.color.C_CCEBCC)
            .setIndicatorSelectedColorRes(R.color.key_color)
            .setBannerGalleryEffect(8, 8)
            .isAutoLoop(true)
        banner.adapter.setOnBannerListener(object :
            OnBannerListener<HomeBannerInfo> {
            override fun OnBannerClick(data: HomeBannerInfo?, position: Int) {
                data?.jump(ctx)
            }
        })
    }
}