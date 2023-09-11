package com.gys.play.ui.view.star;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gys.play.R;


public class RatingBarCustom extends View {

    private int max = 5, num = 2, spacing = 8;
    private Paint mPaint;
    private Bitmap bitmapN;
    private Bitmap bitmapP;
    private OnListener<Integer> onRatingChangeListener;
    private OnListener<Integer> onRatingEnsureChangeListener;

    public RatingBarCustom(Context context) {
        super(context);
    }

    public RatingBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatingBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setOnRatingChangeListener(OnListener<Integer> onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
        setClickable(onRatingChangeListener != null || onRatingEnsureChangeListener != null);
    }

    public void setOnRatingEnsureChangeListener(OnListener<Integer> onRatingEnsureChangeListener) {
        this.onRatingEnsureChangeListener = onRatingEnsureChangeListener;
        setClickable(onRatingChangeListener != null || onRatingEnsureChangeListener != null);
    }

    public void setMax(int max) {
        this.max = max;
        requestLayout();
    }

    public void setNum(int num) {
        this.num = num;
        if (num > max) {
            throw new NullPointerException("RatingBarCustom num should bigger than max");
        }
        invalidate();
    }

    public int getNum() {
        return num;
    }

    public int getMax() {
        return max;
    }

    public void setStarN(Bitmap bitmapN) {
        this.bitmapN = bitmapN;
        requestLayout();
    }

    public void setStarP(Bitmap bitmapP) {
        this.bitmapP = bitmapP;
        requestLayout();
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        requestLayout();
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.RatingBarCustom);

            num = a.getInt(R.styleable.RatingBarCustom_rbc_num, num);
            max = a.getInt(R.styleable.RatingBarCustom_rbc_max, max);
            Drawable star_n = a.getDrawable(R.styleable.RatingBarCustom_rbc_star_n);
            Drawable star_p = a.getDrawable(R.styleable.RatingBarCustom_rbc_star_p);
            spacing = (int) a.getDimension(R.styleable.RatingBarCustom_rbc_spacing, spacing);

            if (star_n == null || star_p == null) {
                throw new NullPointerException("RatingBarCustom star_n and star_p should not be null");
            }
            if (num > max) {
                throw new NullPointerException("RatingBarCustom num should bigger than max");
            }

            bitmapN = drawable2Bitmap(star_n);
            bitmapP = drawable2Bitmap(star_p);

            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = getPaddingLeft();
        float top = getPaddingTop();
        if (mPaint == null) {
            mPaint = new Paint();
        }
        for (int i = 1; i <= num; i++) {
            canvas.drawBitmap(bitmapP, left, top, mPaint);
            left += (bitmapP.getWidth() + spacing);
        }
        for (int i = 1; i <= max - num; i++) {
            canvas.drawBitmap(bitmapN, left, top, mPaint);
            left += (bitmapN.getWidth() + spacing);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int starNWidth = bitmapN.getWidth();
        int starPWidth = bitmapP.getWidth();
        int width = starNWidth * (max - num) + starPWidth * num + spacing * (max - 1) + getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(width, Math.max(bitmapN.getHeight(), bitmapP.getHeight()) + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onRatingChangeListener == null && onRatingEnsureChangeListener == null) {
            return super.onTouchEvent(event);
        }
        float touchX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onEvent(touchX, false);
                return true;
            case MotionEvent.ACTION_MOVE:
                onEvent(touchX, false);
                return true;
            case MotionEvent.ACTION_UP:
                onEvent(touchX, true);
                return false;
//            case MotionEvent.ACTION_CANCEL:
//                onEvent(touchX, true);
//                return false;
        }
        return super.onTouchEvent(event);
    }

    private void onEvent(float touchX, boolean isEnd) {
        int touchNum = getTouchNum(touchX);
        if (touchNum != num) {
            num = touchNum;
            invalidate();
            if (onRatingChangeListener != null) {
                onRatingChangeListener.onListen(num);
            }
        }
        if (isEnd) {
            if (onRatingEnsureChangeListener != null) {
                onRatingEnsureChangeListener.onListen(num);
            }
        }
    }

    private int getTouchNum(float touchX) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (touchX < paddingLeft) {
            return 0;
        }
        if (touchX > getWidth() - paddingRight) {
            return max;
        }
        int starWidth = Math.max(bitmapN.getWidth(), bitmapP.getWidth());
        float startX;
        float endX;
        for (int i = 0; i < this.max; i++) {
            startX = paddingLeft + i * (starWidth + spacing) - spacing / 2;
            endX = startX + starWidth + spacing / 2;
            if (touchX >= startX && touchX <= endX) {
                return i + 1;
            }
        }
        return num;
    }

    //drawable转bitmap
    private Bitmap drawable2Bitmap(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }
}
