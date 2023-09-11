package com.android.liba.ui.widget.select;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.android.liba.R;


public class SelectorImage extends AppCompatImageView {

    private boolean isOnlySelected;

    public SelectorImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public SelectorImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectorImage(Context context) {
        super(context);
    }

    private StateListDrawable src_drawable;
    private StateListDrawable background_drawable;

    private static class MyStateListDrawable extends StateListDrawable {
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean stateChange = super.onStateChange(stateSet);
            if (stateChange) {
                boolean isPress = false;
                for (int state : stateSet) {
                    if (state == android.R.attr.state_pressed) {
                        isPress = true;
                        break;
                    }
                }
//                if(isPress){
//                    setTextS
//                }
            }
            return stateChange;
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SelectorImage);
            Drawable background_n = a.getDrawable(R.styleable.SelectorImage_background_n);
            Drawable background_p = a.getDrawable(R.styleable.SelectorImage_background_p);
            Drawable src_n = a.getDrawable(R.styleable.SelectorImage_src_n);
            Drawable src_p = a.getDrawable(R.styleable.SelectorImage_src_p);
            isOnlySelected = a.getBoolean(R.styleable.SelectorImage_isOnlySelected, isOnlySelected);
            setImageSelector(src_n, src_p);
            setBackGroundSelector(background_n, background_p);
            a.recycle();
        }

    }

    public void setBackGroundSelector(Drawable normalDrawer, Drawable pressDrawer) {
        if (normalDrawer == null && pressDrawer == null) {
            return;
        }
        background_drawable = new MyStateListDrawable();
        if (isOnlySelected) {
            background_drawable.addState(new int[]{-android.R.attr.state_pressed},
                    normalDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    pressDrawer);
        } else {
            background_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    normalDrawer);
            background_drawable.addState(new int[]{android.R.attr.state_focused},
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
            background_drawable.addState(new int[]{android.R.attr.state_focused},
                    getDrawable(pressId));
            background_drawable.addState(new int[]{android.R.attr.state_selected},
                    getDrawable(pressId));
            background_drawable.addState(new int[]{android.R.attr.state_pressed},
                    getDrawable(pressId));
        }
        setBackground(background_drawable);
    }

    public void setImageSelector(Drawable normalDrawer, Drawable pressDrawer) {
        if (normalDrawer == null && pressDrawer == null) {
            return;
        }
        src_drawable = new MyStateListDrawable();
        if (isOnlySelected) {
            src_drawable.addState(new int[]{-android.R.attr.state_selected},
                    normalDrawer);
            src_drawable.addState(new int[]{android.R.attr.state_selected},
                    pressDrawer);
        } else {
            src_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    normalDrawer);
            src_drawable.addState(new int[]{android.R.attr.state_focused},
                    pressDrawer);
            src_drawable.addState(new int[]{android.R.attr.state_selected},
                    pressDrawer);
            src_drawable.addState(new int[]{android.R.attr.state_pressed},
                    pressDrawer);
        }
        setImageDrawable(src_drawable);
    }

    public void setImageSelector(int normalId, int pressId) {
        if (normalId == 0 && pressId == 0) {
            return;
        }
        src_drawable = new MyStateListDrawable();
        if (isOnlySelected) {
            src_drawable.addState(new int[]{-android.R.attr.state_selected},
                    getDrawable(normalId));
            src_drawable.addState(new int[]{android.R.attr.state_selected},
                    getDrawable(pressId));
        } else {
            src_drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                    getDrawable(normalId));
            src_drawable.addState(new int[]{android.R.attr.state_focused},
                    getDrawable(pressId));
            src_drawable.addState(new int[]{android.R.attr.state_selected},
                    getDrawable(pressId));
            src_drawable.addState(new int[]{android.R.attr.state_pressed},
                    getDrawable(pressId));
        }
        setImageDrawable(src_drawable);
    }

    private Drawable getDrawable(int drawId) {
        return ResourcesCompat.getDrawable(getResources(), drawId, null);
    }
}
