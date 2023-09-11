package com.android.liba.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.android.liba.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class UIUtil {


    //首页的展示图片。使用缩略图，无需压缩
    public static void setActivityCover(Context context, ImageView imageView, String url, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(url)
                    .placeholder(defImg)
                    .error(defImg)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

    public static void setActivityCenter(Context context, ImageView imageView, String url, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(url)
                    .placeholder(defImg)
                    .error(defImg)
//                .transform(new CenterInside())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }
    public static void setActivityCenter(Context context, ImageView imageView, String url) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.bg_default_img)
                    .error(R.drawable.bg_default_img)
//                .transform(new CenterInside())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

    public static void setRoundRecImage(ImageView imageView, String url, int radius, int defImg) {
        if(!AppUtil.isDestroy((Activity) imageView.getContext())){
            RoundedCorners roundedCorners = new RoundedCorners(radius);
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(defImg)
                    .error(defImg)
                    .apply(RequestOptions.bitmapTransform(roundedCorners))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

    public static void setRectBitmap(Context context, ImageView imageView, Bitmap bitmap, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(bitmap)
                    .placeholder(defImg)
                    .error(defImg)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

    public static void setRectRes(Context context, ImageView imageView, @DrawableRes int id, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(id)
                    .placeholder(defImg)
                    .error(defImg)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }

    public static void setGalleyImage(Context context, ImageView imageView, Uri uri, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            Glide.with(context)
                    .load(uri)
                    .apply(new RequestOptions()
//                        .override(resize, resize)
                            .placeholder(defImg)
                            .error(defImg)
                            .centerCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }


    public static void setCircleImage(ImageView imageView, String url) {
        setCircleImage( imageView,  url, R.drawable.bg_default_img);
    }
    public static void setCircleImage(ImageView imageView, String url, int defImg) {
        if(!AppUtil.isDestroy((Activity) imageView.getContext())){

            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(defImg)
                    .error(defImg)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .circleCrop()
                    .into(imageView);
        }
    }

    public static void setActivityCover(ImageView imageView, String url)
    {
        setActivityCover( imageView,  url,  R.drawable.bg_default_img);
    }
    //首页的展示图片。使用缩略图，无需压缩
    public static void setActivityCover(ImageView imageView, String url, int defImg) {
        if(!AppUtil.isDestroy((Activity) imageView.getContext())){
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(defImg)
                    .error(defImg)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }
    public static void setActivityCenter(ImageView imageView, String url){
        setActivityCenter(imageView,  url,  R.drawable.bg_default_img);
    }
    public static void setActivityCenter(ImageView imageView, String url, int defImg) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(defImg)
                .error(defImg)
//                .transform(new CenterInside())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }
    private static void setCoverThumb(Context context, ImageView imageView, String url, int width, int defImg) {
        if(!AppUtil.isDestroy((Activity) context)){
            String thumbUrl = width == -1 ? url : (url + "?imageView2/0/w/" + width);
            Glide.with(context)
                    .load(thumbUrl)
                    .placeholder(defImg)
                    .error(defImg)
                    .transform(new CenterCrop())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
        }
    }
}
