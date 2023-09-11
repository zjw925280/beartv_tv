package com.gys.play.util

import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.coorchice.library.SuperTextView
import com.gys.play.R
import com.mybase.libb.ext.getColor
import com.mybase.libb.ui.rv.BaseItemRecyclerView


@BindingAdapter("LongToW")
fun LongtoW(view: TextView, number: Long) {
    if (number > 10000)
        view.text = "${(number / 10000)}萬次播放"
    else view.text = "${number}次播放"
}

@BindingAdapter("TaskStatusBg")
fun TaskStatusBg(view: SuperTextView, status: Int) {//0未完成，1待领取，2已领取
    when (status) {
        0 -> {
            view.shaderStartColor = R.color.C_FF604F.getColor()
            view.shaderEndColor = R.color.C_FF8953.getColor()
            view.isShaderEnable = true
            view.setTextColor(R.color.white.getColor())
        }
        1 -> {
            view.isShaderEnable = false
            view.solid = R.color.C_FFFAEB.getColor()
            view.setTextColor(R.color.C_FF8040.getColor())
        }
        2 -> {
            view.isShaderEnable = false
            view.solid = R.color.C_f2f2f2.getColor()
            view.setTextColor(R.color.C_999999.getColor())
        }
    }
}

@BindingAdapter("IntToW")
fun InttoW(view: TextView, number: Int) {
    if (number > 1000)
        view.text = ConversionUtil.IntToWString(number) + 'k'
    else view.text = number.toString()
}

@BindingAdapter("DoubleToW")
fun DoubleToW(view: TextView, number: Double) {
    if (number > 1000)
        view.text = ConversionUtil.DoubleToWString(number) + 'k'
    else view.text = number.toString()
}

@BindingAdapter("loadImageWithNoPlaceHolder")
fun loadImageWithNoPlaceHolder(
    view: ImageView, url: String?,
) {
    ImageLoaderUtils.loadImage(view, url, R.color.transparent, R.color.transparent)
}

@BindingAdapter("loadImageBigPlaceHolder")
fun loadImageBigPlaceHolder(
    view: ImageView, url: String?,
) {
    ImageLoaderUtils.loadImage(
        view, url,
        placeholder = R.mipmap.img_default_loading,
        error = R.mipmap.img_default_loading
    )
}

@BindingAdapter("loadImage")
fun loadImage(
    view: ImageView, url: String?,
) {
    ImageLoaderUtils.loadImage(view, url)
}

@BindingAdapter("loadImageCenterCropBigPlaceHolder")
fun loadImageCenterCropBigPlaceHolder(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageCenterCrop(
        view, url,
        placeholder = R.mipmap.img_default_loading,
        error = R.mipmap.img_default_loading
    )
}


@BindingAdapter("loadImageCenterCrop")
fun loadImageCenterCrop(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageCenterCrop(view, url)
}

@BindingAdapter("loadRoundImage")
fun loadRoundImage(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadRoundImage(view, url)
}

@BindingAdapter("loadImageCenterCropWithRadius")
fun loadImageCenterCropWithRadius(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageCenterCropWithRadius(view, url)
}

@BindingAdapter("loadImageWithRightRadius")
fun loadImageWithRightRadius(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageWithRightRadius(view, url)
}

@BindingAdapter("loadImageCenterCropWith9Radius")
fun loadImageCenterCropWith9Radius(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageCenterCropWithRadius(view, url, radius = 9)
}

@BindingAdapter("loadImageCenterCropWith4Radius")
fun loadImageCenterCropWith4Radius(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadImageCenterCropWithRadius(view, url, radius = 6)
}

@BindingAdapter("loadCircle")
fun loadCircle(
    view: ImageView,
    url: String?,
) {
    ImageLoaderUtils.loadCircle(view, url)
}

@BindingAdapter("isSelect")
fun isSelect(view: View, isSelect: Boolean) {
    view.isSelected = isSelect
}

@BindingAdapter("setDrawableResource")
fun setDrawableResource(
    view: ImageView,
    drawableId: Int?,
) {
    if (drawableId == null) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
        view.setImageResource(drawableId)
    }
}

@BindingAdapter("setTextColor")
fun setTextColor(
    view: TextView,
    color: Int,
) {
    view.setTextColor(color)
}

@BindingAdapter("setIndexTextBg")
fun setIndexTextBg(
    view: TextView,
    index: Int
) {
    view.text = ""
    when (index) {
        0 -> view.setBackgroundResource(R.mipmap.ranking1)
        1 -> view.setBackgroundResource(R.mipmap.ranking2)
        2 -> view.setBackgroundResource(R.mipmap.ranking3)
        3 -> view.setBackgroundResource(R.mipmap.ranking4)
        4 -> view.setBackgroundResource(R.mipmap.ranking5)
        5 -> view.setBackgroundResource(R.mipmap.ranking6)
        else -> {
            view.text = " ${(index + 1)} "
            view.setBackgroundResource(R.drawable.shape_oval_afb5cd)
        }
    }

}

// 根据View的高度和宽高比，设置高度
@BindingAdapter("setWidthRatio")
fun setWidthRatio(view: View, ratio: Float) {
    view.viewTreeObserver
        .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height = view.height;
                if (height > 0) {
                    view.layoutParams.width = ((height * ratio).toInt());
                    view.invalidate()
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
}

// 根据View的高度和宽高比，设置宽度
@BindingAdapter("setHeightRatio")
fun setHeightRatio(view: View, ratio: Double) {
    view.viewTreeObserver
        .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = view.width
                if (width > 0) {
                    view.layoutParams.height = ((width / ratio).toInt());
                    view.invalidate()
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
}

@BindingAdapter("adapterModel")
fun adapterModel(rv: BaseItemRecyclerView, list: List<Any?>?) {
    rv.models = list
}