package com.gys.play.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.gys.play.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

object HeadUtils {
    //圆形图片(加载网络图片)
    fun circleImageUrl(view: ImageView, url: String) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.mipmap.def_notice)
            .error(R.mipmap.def_notice)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }
    //圆形图片(加载本地图片)
    fun circleImageUrl(view: ImageView, loginurl: Int) {
        Glide.with(view.context)
            .load(loginurl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.mipmap.def_notice)
            .error(R.mipmap.def_notice)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }
    //圆形图片(加载bitmap)
    fun circleImageUrl(view: ImageView, bitmap: Bitmap) {
        Glide.with(view.context)
            .load(bitmap)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.mipmap.def_notice)
            .error(R.mipmap.def_notice)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }
    //圆角图片(加载网络图片)
    fun roundedCornerImageUrl(view: ImageView, url: String) {
        Glide.with(view.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.mipmap.img_default_loading)
            .error(R.mipmap.img_default_loading)
            .transforms(RoundedCorners(30))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }

    //圆角图片(加载本地图片)
    fun roundedCornerImageUrl(view: ImageView, loginurl: Int) {
        Glide.with(view.context)
            .load(loginurl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.mipmap.img_default_loading)
            .error(R.mipmap.img_default_loading)
            .transforms(RoundedCorners(30))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }

    //圆角图片(加载bitmap)
    fun roundedCornerImageUrl(view: ImageView, bitmap: Bitmap) {
        Glide.with(view.context)
            .load(bitmap)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .placeholder(R.mipmap.img_default_loading)
            .error(R.mipmap.img_default_loading)
            .transforms(RoundedCorners(30))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }
}