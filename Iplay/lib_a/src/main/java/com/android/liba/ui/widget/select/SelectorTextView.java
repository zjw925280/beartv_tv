package com.android.liba.ui.widget.select;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.android.liba.R;


//                    常用的字体风格名称还有：
//                    * Typeface.BOLD //粗体
//                            * Typeface.BOLD_ITALIC //粗斜体
//                            * Typeface.ITALIC //斜体
//                            * Typeface.NORMAL //常规
//
//                    但是有时上面那些设置在绘图过程中是不起作用的，所以还有如下设置方式：
//                    Paint mp = new Paint();
//                    mp.setFakeBoldText(true); //true为粗体，false为非粗体
//                    mp.setTextSkewX(-0.5f); //float类型参数，负数表示右斜，整数左斜
//                    mp.setUnderlineText(true); //true为下划线，false为非下划线
//                    mp.setStrikeThruText(true); //true为删除线，false为非删除线

public class SelectorTextView extends AppCompatTextView {

    private int textStyle_n = -1;
    private int textStyle_p = -1;
    private float textSize_n = -1;
    private float textSize_p = -1;
    private int textColor_n = Color.BLACK;
    private int textColor_p = Color.BLACK;
    private String text_n;
    private String text_p;
    private boolean isOnlySelected;

    public SelectorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SelectorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectorTextView(Context context) {
        super(context);
    }

    private StateListDrawable background_drawable = new MyStateListDrawable();

    private class MyStateListDrawable extends StateListDrawable {
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean isPress = false;
            for (int state : stateSet) {
                if (isOnlySelected) {
                    if (state == android.R.attr.state_selected) {
                        isPress = true;
                        break;
                    }
                } else {
                    if (state == android.R.attr.state_pressed || state == android.R.attr.state_selected) {
                        isPress = true;
                        break;
                    }
                }
            }
            if (isPress) {
                onStatusSelected();
            } else {
                onStatusNormal();
            }
            return super.onStateChange(stateSet);
        }
    }

    private void onStatusSelected() {
        if (textSize_p != -1) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize_p);
        }
        setTextColor(textColor_p);
        TextPaint paint = getPaint();
        if (textStyle_p == 0) {
            paint.setFakeBoldText(false);
            paint.setTextSkewX(0);
        } else if (textStyle_p == 1)
            paint.setFakeBoldText(true);
        else if (textStyle_p == 2)
            paint.setTextSkewX(-0.5f);
        if (text_p != null) {
            if (text_n == null) {
                text_n = getText().toString();
            }
            setText(text_p);
        }
    }

    private void onStatusNormal() {
        if (textSize_n != -1) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize_n);
        }
        setTextColor(textColor_n);
        TextPaint paint = getPaint();
        if (textStyle_n == 0) {
            paint.setFakeBoldText(false);
            paint.setTextSkewX(0);
        } else if (textStyle_n == 1)
            paint.setFakeBoldText(true);
        else if (textStyle_n == 2)
            paint.setTextSkewX(-0.5f);
        if (text_p != null) {
            setText(text_n);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SelectorTextView);

            textStyle_n = a.getInt(R.styleable.SelectorTextView_textStyle_n, textStyle_n);
            textStyle_p = a.getInt(R.styleable.SelectorTextView_textStyle_p, textStyle_p);

            textSize_n = a.getDimension(R.styleable.SelectorTextView_textSize_n, textSize_n);
            textSize_p = a.getDimension(R.styleable.SelectorTextView_textSize_p, textSize_p);

            textColor_n = a.getColor(R.styleable.SelectorTextView_textColor_n, textColor_n);
            textColor_p = a.getColor(R.styleable.SelectorTextView_textColor_p, textColor_p);

            text_n = a.getString(R.styleable.SelectorTextView_text_n);
            text_p = a.getString(R.styleable.SelectorTextView_text_p);

            isOnlySelected = a.getBoolean(R.styleable.SelectorTextView_isOnlySelected, isOnlySelected);

            Drawable background_n = a.getDrawable(R.styleable.SelectorTextView_background_n);
            Drawable background_p = a.getDrawable(R.styleable.SelectorTextView_background_p);
            setBackGroundSelector(background_n, background_p);

            a.recycle();
        }
        onStatusNormal();
    }

    public void setBackGroundSelector(Drawable normalDrawer, Drawable pressDrawer) {
//        if(normalDrawer==null&&pressDrawer==null){
//            return;
//        }
        background_drawable = new MyStateListDrawable();
        if (isOnlySelected) {
            background_drawable.addState(new int[]{-android.R.attr.state_selected},
                    normalDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    pressDrawer);
        } else {
            background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    normalDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_focused,},
                    pressDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    pressDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_pressed},
                    pressDrawer);
        }
        setBackground(background_drawable);
    }

    public void setBackGroundSelector(int normalId, int pressId) {
        if (normalId == 0 && pressId == 0) {
            return;
        }
        background_drawable = new MyStateListDrawable();
        if (isOnlySelected) {
            background_drawable.addState(new int[]{-android.R.attr.state_selected},
                    getDrawable(normalId));
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    getDrawable(pressId));
        } else {
            background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    getDrawable(normalId));
            background_drawable.addState(new int[]{android.R.attr.state_focused,},
                    getDrawable(pressId));
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    getDrawable(pressId));
            background_drawable.addState(new int[]{android.R.attr.state_pressed},
                    getDrawable(pressId));
        }
        setBackground(background_drawable);
    }

    private Drawable getDrawable(int drawId) {
        return ResourcesCompat.getDrawable(getResources(), drawId, null);
    }
}
