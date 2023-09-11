package com.gys.play.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class GlideRightRoundTransform extends BitmapTransformation {
    private float radius = 0f;

    public GlideRightRoundTransform() {
        this(4);
    }

    public GlideRightRoundTransform(int dp) {
        super();
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        //变换的时候裁切
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        //先绘制圆角矩形
        canvas.drawRoundRect(rectF, radius, radius, paint);


        //左上角圆角
        canvas.drawRect(0, 0, radius, radius, paint);
        //右上角圆角
//        canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
//        //左下角圆角
        canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
//        右下角圆角
//        canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), paint);


        return result;
    }
}