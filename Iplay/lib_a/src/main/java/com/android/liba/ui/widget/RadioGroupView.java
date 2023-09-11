package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.liba.R;

import java.util.ArrayList;
import java.util.List;

public class RadioGroupView extends View {

    private float size = 10;
    private float space = 10;
    private int colorN = 0xffABB5CC;
    private int colorP = 0xff2B63F1;
    private int count = 3;
    private final List<Integer> selectPositionList = new ArrayList<>();
    private Paint paint;

    public RadioGroupView(Context context) {
        super(context);
        init(context, null);
    }

    public RadioGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadioGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RadioGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RadioGroupView);
            //获取设置在子控件上的位置属性
            size = a.getDimension(R.styleable.RadioGroupView_rg_size, size);
            space = a.getDimension(R.styleable.RadioGroupView_rg_space, space);
            colorN = a.getColor(R.styleable.RadioGroupView_rg_color_n, colorN);
            colorP = a.getColor(R.styleable.RadioGroupView_rg_color_p, colorP);
            count = a.getInt(R.styleable.RadioGroupView_rg_count, count);
            int selectPosition = a.getInt(R.styleable.RadioGroupView_rg_select_position, 0);
            selectPositionList.add(selectPosition);
            a.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setSize(float size) {
        this.size = size;
        requestLayout();
        invalidate();
    }

    public void setSpace(float space) {
        this.space = space;
        requestLayout();
        invalidate();
    }

    public void setColorN(int colorN) {
        this.colorN = colorN;
        invalidate();
    }

    public void setColorP(int colorP) {
        this.colorP = colorP;
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
        requestLayout();
        invalidate();
    }

    public int getCount() {
        return count;
    }

    public void setSelect(int... positions) {
        selectPositionList.clear();
        if (positions != null) {
            for (int i : positions) {
                selectPositionList.add(i);
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (count > 0) {
            setMeasuredDimension((int) (getPaddingLeft() + getPaddingRight() + count * size + (count - 1) * space),
                    getPaddingTop() + getPaddingBottom() + (int) size);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (count <= 0) return;
//        int width = getWidth();
        int height = getHeight();
        float x = getPaddingLeft() + size / 2f;
        float y = height / 2f;
        for (int i = 0; i < count; i++) {
            if (selectPositionList.contains((Integer) i)) {
                paint.setColor(colorP);
            } else {
                paint.setColor(colorN);
            }
            canvas.drawCircle(x, y, size / 2f, paint);
            x += space;
            x += size;
        }
    }
}
