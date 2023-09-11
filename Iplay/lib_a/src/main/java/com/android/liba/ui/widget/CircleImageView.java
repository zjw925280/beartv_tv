package com.android.liba.ui.widget;

import static android.graphics.PorterDuff.Mode.SRC_IN;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.android.liba.util.ImageUtil;


/**
 * 圆形ImageView
 */
public class CircleImageView extends AppCompatImageView {

    private Paint mPaint;
    private Paint mTargetPaint;
    private Bitmap mTargetBitmap;
    private Canvas mTargetCanvas;

    private int mWidth;
    private int mHeight;

    private Matrix matrix;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTargetPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTargetPaint.setXfermode(new PorterDuffXfermode(SRC_IN));
        matrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mTargetBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mTargetCanvas = new Canvas(mTargetBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(mWidth, mHeight) / 2;
        // 先绘制圆形
        mTargetCanvas.drawCircle(mWidth / 2f, mHeight / 2f, radius, mPaint);

        // 再绘制Bitmap
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ImageUtil.drawable2Bitmap(drawable);
            if (bitmap == null) return;
            matrix.reset();
            float scaleWidth = Math.max(mWidth * 1f / bitmap.getWidth(), mHeight * 1f / bitmap.getHeight());
//            float scaleWidth;
//            if (getScaleType() == ScaleType.CENTER_CROP) {
//            } else {
//                scaleWidth = mWidth * 1f / bitmap.getWidth();
//            }
            matrix.postScale(scaleWidth, scaleWidth);
            matrix.postTranslate(mWidth / 2f - bitmap.getWidth() * scaleWidth / 2f, mHeight / 2f - bitmap.getHeight() * scaleWidth / 2f);
            mTargetCanvas.drawBitmap(bitmap, matrix, mTargetPaint);
        }

        //取交集
        canvas.drawBitmap(mTargetBitmap, 0, 0, null);
    }
}