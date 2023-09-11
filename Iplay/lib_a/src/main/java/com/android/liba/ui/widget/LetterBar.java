package com.android.liba.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.liba.R;
import com.android.liba.util.UIHelper;


public class LetterBar extends View {
    private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private int choose = 0;
    private Paint paint;
    private OnLetterChangedListener onLetterChangedListener;
    private int textColor;
    private int textColorP;
    private int selectedBg;
    private float textSize;
    private float selectedBgR;

    public LetterBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public LetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LetterBar(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterBar);
            textColor = a.getColor(R.styleable.LetterBar_lb_textColor, 0xff333333);
            textColorP = a.getColor(R.styleable.LetterBar_lb_textColor_p, 0xffffffff);
            selectedBg = a.getColor(R.styleable.LetterBar_lb_selected_bg, 0xff2B63F1);
            textSize = a.getDimension(R.styleable.LetterBar_lb_textSize, UIHelper.sp2px(context, 14));
            selectedBgR = a.getDimension(R.styleable.LetterBar_lb_selected_bg_r, UIHelper.dip2px(context, 7));
            a.recycle();
        }
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
        invalidate();
    }

    public void setSelected(int selectedPosition) {
        if (selectedPosition < 0 || selectedPosition > letters.length - 1) {
            return;
        }
        choose = selectedPosition;
        invalidate();
    }

    public void setSelected(String selectedStr) {
        for (int i = 0; i < letters.length; i++) {
            if (selectedStr.equals(letters[i])) {
                setSelected(i);
                return;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (letters == null || letters.length == 0) return;
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / letters.length;
        for (int i = 0; i < letters.length; i++) {
            float xPos = width / 2f;
            float yPos = singleHeight * i + singleHeight / 2f;
            if (i == choose) {
                paint.setColor(selectedBg);
                canvas.drawCircle(xPos, yPos, selectedBgR, paint);
                paint.setColor(textColorP);
            } else {
                paint.setColor(textColor);
            }
            canvas.drawText(letters[i], xPos, yPos + textSize / 2f * 2 / 3, paint);

            //调试位置的线条
//            paint.setColor(0xffff0000);
//            canvas.drawLine(xPos - 50, yPos, xPos + 50, yPos, paint);
//            canvas.drawLine(xPos, yPos - 50, xPos, yPos + 50, paint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (letters == null || letters.length == 0) return false;
        final int action = event.getAction();
        final float y = event.getY();
        final int c = (int) (y / getHeight() * letters.length);

        if (c >= 0 && c < letters.length) {
            if (c != choose) {
                choose = c;
            }
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (onLetterChangedListener != null) {
                    onLetterChangedListener.onLetterChanged(letters[choose], choose, true);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                if (onLetterChangedListener != null) {
                    onLetterChangedListener.onLetterChanged(letters[choose], choose, false);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter, int selectedPosition, boolean isTouching);
    }

}
