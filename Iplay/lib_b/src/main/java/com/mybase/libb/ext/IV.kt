package com.mybase.libb.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions



object ImgUtil {

    @JvmStatic
    fun load(
        iv: ImageView?, path: String?,
        option: ((RequestOptions) -> Unit)? = null
    ) {
        realLoad(iv, path, circle = false, 0f,option)
    }

    @JvmStatic
    fun load(
        iv: ImageView?, path: Int,
        option: ((RequestOptions) -> Unit)? = null
    ) {
        realLoad(iv, path, circle = false, 0f,option)
    }

    @JvmStatic  //圆角
    fun loadCorner(
        iv: ImageView?, path: String?, corner: Float,
        option: ((RequestOptions) -> Unit)? = null
    ) {

        realLoad(iv, path, circle = false, corner, option)
    }

    @JvmStatic  //圆角
    fun loadCorner(
        iv: ImageView?, path: Int, corner: Float,
        option: ((RequestOptions) -> Unit)? = null
    ) {

        realLoad(iv, path, circle = false, corner, option)
    }


    @JvmStatic  //圆图
    fun loadCircle(
        iv: ImageView?, path: String?,
        option: ((RequestOptions) -> Unit)? = null
    ) {

        realLoad(iv, path, circle = true, 0f, option)
    }

    @JvmStatic  //圆图
    fun loadCircle(
        iv: ImageView?, path: Int,
        option: ((RequestOptions) -> Unit)? = null
    ) {

        realLoad(iv, path, circle = true, 0f, option)
    }

    @JvmStatic  //圆角
    fun realLoad(
        iv: ImageView?,
        path: Any?,
        circle: Boolean,
        corner: Float,
        option: ((RequestOptions) -> Unit)? = null
    ) {

//        if (!path.notEmpty()) return

        val options = RequestOptions()

        if (circle) {
            options.circleCrop()
        } else if (corner > 0) {
            options.transform(RoundedCorners(corner.dp2px()))
        }
        option?.invoke(options)

        iv?.let {
            Glide.with(it.context).load(path).apply(options).into(it)
        }
    }


}