package com.android.liba.ui.widget;

import static android.graphics.PorterDuff.Mode.SRC_IN;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.android.liba.R;
import com.android.liba.util.ImageUtil;


/**
 * 圆角图片控件
 */
public class ConnerImageView extends AppCompatImageView {

    private Paint mPaint;
    private Paint mTargetPaint;
    private Bitmap mTargetBitmap;
    private Canvas mTargetCanvas;

    private int mWidth;
    private int mHeight;

    private Matrix matrix;
    private float conner;
    private RectF rectF;

    public ConnerImageView(Context context) {
        this(context, null);
    }

    public ConnerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ConnerImageView);

            conner = a.getDimension(R.styleable.ConnerImageView_conner, conner);

            a.recycle();
        }

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
        rectF = new RectF();
        rectF.left = 0;
        rectF.top = 0;
        rectF.right = mWidth;
        rectF.bottom = mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 先绘制圆角矩形
        mTargetCanvas.drawRoundRect(rectF, conner, conner, mPaint);

        // 再绘制Bitmap
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = ImageUtil.drawable2Bitmap(drawable);
            if (bitmap == null) return;
            matrix.reset();
            float scaleWidth = Math.max(mWidth * 1f / bitmap.getWidth(), mHeight * 1f / bitmap.getHeight());
            matrix.postScale(scaleWidth, scaleWidth);
            matrix.postTranslate(mWidth / 2f - bitmap.getWidth() * scaleWidth / 2f, mHeight / 2f - bitmap.getHeight() * scaleWidth / 2f);
            mTargetCanvas.drawBitmap(bitmap, matrix, mTargetPaint);
        }

        //取交集
        canvas.drawBitmap(mTargetBitmap, 0, 0, null);
    }
}