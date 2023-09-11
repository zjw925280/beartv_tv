package com.android.liba.ui.widget.conner;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import com.android.liba.jk.OnListener;
import com.android.liba.util.UIHelper;


public class JBackgroundView {

    public int bgColor;
    public int bgColor_p;
    public float conner;
    public float connerLeftTop, connerLeftBottom, connerRightTop, connerRightBottom;
    public GradientDrawable shapeDrawable;
    public GradientDrawable shapeDrawableP;
    public boolean connerHalfWidth;
    public boolean connerHalfHeight;
    public int lineColor;
    public int lineColor_p;
    public float lineWidth;
    public int orientation;
    public int[] colors;
    public int[] colors_p;

    public static final int orientation_TOP_BOTTOM = 0;
    public static final int orientation_TR_BL = 1;
    public static final int orientation_RIGHT_LEFT = 2;
    public static final int orientation_BR_TL = 3;
    public static final int orientation_BOTTOM_TOP = 4;
    public static final int orientation_BL_TR = 5;
    public static final int orientation_LEFT_RIGHT = 6;
    public static final int orientation_TL_BR = 7;
    private View view;

    public JBackgroundView() {
    }

    public void setBgColor_p(int bgColor_p) {
        this.bgColor_p = bgColor_p;
    }

    public void setLineColor_p(int lineColor_p) {
        this.lineColor_p = lineColor_p;
    }

    public void setColors_p(int[] colors_p) {
        this.colors_p = colors_p;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        if (shapeDrawable != null) {
            shapeDrawable.setColors(colors);
            invalidate();
        }
    }

    public void setConner(float conner) {
        this.conner = conner;
    }

    public void setConnerHalfHeight(boolean connerHalfHeight) {
        this.connerHalfHeight = connerHalfHeight;
    }

    public void setConnerHalfWidth(boolean connerHalfWidth) {
        this.connerHalfWidth = connerHalfWidth;
    }

    public void setConnerLeftBottom(float connerLeftBottom) {
        this.connerLeftBottom = connerLeftBottom;
    }

    public void setConnerLeftTop(float connerLeftTop) {
        this.connerLeftTop = connerLeftTop;
    }

    public void setConnerRightBottom(float connerRightBottom) {
        this.connerRightBottom = connerRightBottom;
    }

    public void setConnerRightTop(float connerRightTop) {
        this.connerRightTop = connerRightTop;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void invalidate() {
        if (view == null) return;
        checkSetBg(view);
    }

    public void onSizeChange(View view, int width, int height) {
        this.view = view;
        if (connerHalfHeight) {
            if (shapeDrawable != null) {
                shapeDrawable.setCornerRadius(height / 2f);
            }
            if (shapeDrawableP != null) {
                shapeDrawableP.setCornerRadius(height / 2f);
            }
        }
        if (connerHalfWidth) {
            if (shapeDrawable != null) {
                shapeDrawable.setCornerRadius(width / 2f);
            }
            if (shapeDrawableP != null) {
                shapeDrawableP.setCornerRadius(width / 2f);
            }
        }
        setBackGroundSelector(view, shapeDrawable, shapeDrawableP);
    }

    public void checkSetBg(View view) {
        this.view = view;
        if (bgColor != 0 || lineColor != 0 || colors != null) {
            if (shapeDrawable == null) {
                if (colors == null) {
                    shapeDrawable = new GradientDrawable();
                }
                switch (orientation) {
                    default:
                    case orientation_TOP_BOTTOM:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                        break;
                    case orientation_TR_BL:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.TR_BL, colors);
                        break;
                    case orientation_RIGHT_LEFT:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
                        break;
                    case orientation_BR_TL:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
                        break;
                    case orientation_BOTTOM_TOP:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
                        break;
                    case orientation_BL_TR:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
                        break;
                    case orientation_LEFT_RIGHT:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                        break;
                    case orientation_TL_BR:
                        shapeDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
                        break;
                }
            }
            shapeDrawable.setShape(GradientDrawable.RECTANGLE);
            if (bgColor != 0) shapeDrawable.setColor(bgColor);
            if (lineColor != 0) {
                if (lineWidth == 0) lineWidth = UIHelper.getDp1Px();
                shapeDrawable.setStroke((int) lineWidth, lineColor);
            }
            if (conner > 0) {
                shapeDrawable.setCornerRadius(conner);
            } else {
//            设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                shapeDrawable.setCornerRadii(new float[]{connerLeftTop, connerLeftTop, connerRightTop, connerRightTop, connerRightBottom, connerRightBottom, connerLeftBottom, connerLeftBottom});
            }

        } else {
            shapeDrawable = null;
        }

        if (bgColor_p != 0 || lineColor_p != 0 || colors_p != null) {
            if (shapeDrawableP == null) {
                if (colors_p == null) {
                    shapeDrawableP = new GradientDrawable();
                }
                switch (orientation) {
                    default:
                    case orientation_TOP_BOTTOM:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors_p);
                        break;
                    case orientation_TR_BL:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.TR_BL, colors_p);
                        break;
                    case orientation_RIGHT_LEFT:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors_p);
                        break;
                    case orientation_BR_TL:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors_p);
                        break;
                    case orientation_BOTTOM_TOP:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors_p);
                        break;
                    case orientation_BL_TR:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors_p);
                        break;
                    case orientation_LEFT_RIGHT:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors_p);
                        break;
                    case orientation_TL_BR:
                        shapeDrawableP = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors_p);
                        break;
                }
            }
            shapeDrawableP.setShape(GradientDrawable.RECTANGLE);
            if (bgColor_p != 0) shapeDrawableP.setColor(bgColor_p);
            if (lineColor_p != 0) {
                if (lineWidth == 0) lineWidth = UIHelper.getDp1Px();
                shapeDrawableP.setStroke((int) lineWidth, lineColor_p);
            }
            if (conner > 0) {
                shapeDrawableP.setCornerRadius(conner);
            } else {
//            设置图片四个角圆形半径：1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                shapeDrawableP.setCornerRadii(new float[]{connerLeftTop, connerLeftTop, connerRightTop, connerRightTop, connerRightBottom, connerRightBottom, connerLeftBottom, connerLeftBottom});
            }

        } else {
            shapeDrawableP = null;
        }

        setBackGroundSelector(view, shapeDrawable, shapeDrawableP);
    }

    private OnListener<Boolean> onSelectedChangeListener;

    public void setOnSelectedChangeListener(OnListener<Boolean> onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    class MyStateListDrawable extends StateListDrawable {
        @Override
        protected boolean onStateChange(int[] stateSet) {
            boolean isPress = false;
            for (int state : stateSet) {
                if (state == android.R.attr.state_pressed || state == android.R.attr.state_selected || state == android.R.attr.state_checked) {
                    isPress = true;
                    break;
                }
            }
            if (onSelectedChangeListener != null) {
                onSelectedChangeListener.onListen(isPress);
            }
            return super.onStateChange(stateSet);
        }
    }

    private void setBackGroundSelector(View view, Drawable normalDrawer, Drawable pressDrawer) {
        StateListDrawable backgroundDrawable = new MyStateListDrawable();
        backgroundDrawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                normalDrawer);
        backgroundDrawable.addState(new int[]{android.R.attr.state_focused,},
                pressDrawer);
        backgroundDrawable.addState(new int[]{android.R.attr.state_selected},
                pressDrawer);
        backgroundDrawable.addState(new int[]{android.R.attr.state_pressed},
                pressDrawer);
        view.setBackground(backgroundDrawable);
    }
}
