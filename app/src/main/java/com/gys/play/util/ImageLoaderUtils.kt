package com.gys.play.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.annotation.NonNull
import com.gys.play.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

object ImageLoaderUtils {
//    fun loadImage(
//        view: PhotoView, url: String?,
//        placeholder: Int = R.color.black,
//        error: Int = R.color.black
//    ) {
//        val options = RequestOptions()
//            .error(error) //加载失败图片
//            .placeholder(placeholder) //预加载图片
//        load(view, url, options)
//    }


    fun loadImage(
        view: ImageView, url: String?,
        placeholder: Int = R.mipmap.img_default_loading,
        error: Int = R.mipmap.img_default_loading,
        listener: RequestListener<Bitmap>? = null
    ) {
        val options = RequestOptions()
            .error(error) //加载失败图片
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) //加载原始大小 (部分图片会模糊)
            .placeholder(placeholder) //预加载图片
        load(view, url, options, listener)
    }

    fun loadImage(
        url: String?,
        placeholder: Int,
        error: Int,
        width: Int = Target.SIZE_ORIGINAL,
        height: Int = Target.SIZE_ORIGINAL, view: ImageView
    ) {
        val options = RequestOptions()
            .placeholder(placeholder)
            .error(error)
            .override(width, height)
        load(view, url, options)
    }

    fun loadUrlGifImage(
        view: ImageView, url: String?,
        placeholder: Int = R.color.black,
        error: Int = R.color.black
    ) {
        val options = RequestOptions()
            .error(error) //加载失败图片
            .placeholder(placeholder) //预加载图片
        loadGif(view, url, options)
    }

    fun loadrRsourceGif(
        view: ImageView,
        img: Int,
        count: Int = 1
    ) {
        try {
            Glide.with(view.context)
                .load(img)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        if (resource is GifDrawable) {
                            val gifDrawable = resource as GifDrawable
                            gifDrawable.setLoopCount(count)
                            view.setImageDrawable(resource)
                            gifDrawable.start()
                        } else {
                            view.setImageDrawable(resource)
                        }
                    }
                })
        } catch (e: Exception) {
        } catch (e: OutOfMemoryError) {
        }
    }


    fun loadImageCenterCrop(
        view: ImageView,
        url: String?,
        placeholder: Int = R.mipmap.img_default_loading,
        error: Int = R.mipmap.img_default_loading
    ) {
        val options = RequestOptions()
            .centerCrop()
            .placeholder(placeholder) //预加载图片
            .error(error) //加载失败图片
        load(view, url, options)
    }

    fun loadImageWithRightRadius(
        view: ImageView,
        url: String?, radius: Int = 9,
        placeholder: Int = R.mipmap.img_default_loading,
        error: Int = R.mipmap.img_default_loading
    ) {
        val options = RequestOptions()
            .transform(com.gys.play.util.GlideRightRoundTransform(radius))
            .placeholder(placeholder) //预加载图片
            .error(error) //加载失败图片
        load(view, url, options)
    }


    fun loadImageCenterCropWithRadius(
        view: ImageView,
        url: String?, radius: Int = 4,
        placeholder: Int = R.mipmap.img_default_loading,
        error: Int = R.mipmap.img_default_loading
    ) {
        val options = RequestOptions()
            .centerCrop()
            .transform(com.gys.play.util.GlideRoundTransform(radius))
            .error(error) //加载失败图片
            .placeholder(placeholder) //预加载图片
        load(view, url, options)
    }

    fun loadRoundImage(
        view: ImageView,
        url: String?,
        placeholder: Int = R.mipmap.img_default_loading,
        error: Int = R.mipmap.img_default_loading
    ) {
        val options = RequestOptions()
            .transform(com.gys.play.util.GlideRoundTransform(180)) //圆角
            .placeholder(placeholder) //预加载图片
            .error(error) //加载失败图片
        load(view, url, options)
    }

    private fun load(
        view: ImageView,
        url: String?, @NonNull options: BaseRequestOptions<*>,
        listener: RequestListener<Bitmap>? = null
    ) {
        var url = url
        if (url != null && !TextUtils.isEmpty(url) && url.startsWith("//"))
            url = url.replaceFirst("//", "http://")

        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        try {
            Glide.with(view.context)
                .asBitmap()
                .load(url)
                .apply(options)
//                .thumbnail(0.2f)
                .listener(listener)
                .into(view)
        } catch (e: Exception) {
        } catch (e: OutOfMemoryError) {
        }
    }

    fun loadCircle(imageView: ImageView, id: Int) {
        Glide.with(imageView)
            .load(id)
            .placeholder(R.mipmap.logo_circle)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    fun loadCircle(imageView: ImageView, path: String?) {
        Glide.with(imageView)
            .load(path)
            .placeholder(R.mipmap.logo_circle)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    fun loadCircle(imageView: ImageView, uri: Uri?) {
        Glide.with(imageView)
            .load(uri)
            .placeholder(R.mipmap.logo_circle)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imageView)
    }

    private fun loadGif(
        view: ImageView,
        url: String?, @NonNull options: BaseRequestOptions<*>,
    ) {
        var url = url
        if (url != null && !TextUtils.isEmpty(url) && url.startsWith("//"))
            url = url.replaceFirst("//", "http://")

        options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        try {
            Glide.with(view.context)
                .asGif()
                .load(url)
                .apply(options)
                .into(view)
        } catch (e: Exception) {
        } catch (e: OutOfMemoryError) {
        }
    }

    /**
     * 加载成功之后显示view
     */
    fun loadEndShowImage(imageView: ImageView, thumb: String) {
        Glide.with(imageView).asBitmap().load(thumb)
            .centerCrop()
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    imageView.visibility = View.VISIBLE
                    imageView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

}

