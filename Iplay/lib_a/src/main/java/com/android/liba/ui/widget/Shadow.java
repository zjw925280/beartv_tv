package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.android.liba.R;


public class Shadow extends androidx.appcompat.widget.AppCompatTextView {

    private int width;
    private int height;
    private Paint paint;
    private RectF rectF;
    private int shadowColor = 0x01000000;//
    private float shadowWidth = 30;
    private float shadowSingle = 2;
    private boolean connerHalfWidth;
    private boolean connerHalfHeight;
    private float conner;

    public Shadow(Context context) {
        super(context);
        init(context, null);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public Shadow(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, attrs);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Shadow);
            shadowColor = a.getColor(R.styleable.Shadow_shadowColor, shadowColor);
            shadowWidth = a.getDimension(R.styleable.Shadow_shadowWidth, shadowWidth);
            shadowSingle = a.getDimension(R.styleable.Shadow_shadowSingle, shadowSingle);
            connerHalfWidth = a.getBoolean(R.styleable.Shadow_connerHalfWidth, connerHalfWidth);
            connerHalfHeight = a.getBoolean(R.styleable.Shadow_connerHalfHeight, connerHalfHeight);
            conner = a.getDimension(R.styleable.Shadow_conner, conner);

            a.recycle();
        }

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(shadowColor);

        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (height <= 0) return;
        for (int h = height; h > height - shadowWidth; h -= shadowSingle) {
            if (h <= 0) continue;
            int dis = height - h;
            rectF.left = dis;
            rectF.right = width - dis;
            rectF.top = dis;
            rectF.bottom = height - dis;
            float r;
            if (connerHalfHeight) {
                r = h / 2f;
            } else if (connerHalfWidth) {
                r = (width - dis * 2) / 2f;
            } else {
                r = conner;
            }
            canvas.drawRoundRect(rectF, r, r, paint);
        }
    }
}
