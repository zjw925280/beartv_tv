package com.android.liba.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.liba.util.UIHelper;

public class TouchRemoveXLayout extends LinearLayout {

    private int width;
    private int height;
    private VelocityTracker mVelocityTracker;
    private ValueAnimator valueAnimator;
    private OnMoveListener onMoveListener;
    private final String TAG = "TouchRemoveXLayout";

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    public interface OnMoveListener {
        void onChange(boolean isNext);
    }

    public TouchRemoveXLayout(Context context) {
        super(context);
        init();
    }

    public TouchRemoveXLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchRemoveXLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TouchRemoveXLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getWidth();
        height = getHeight();
    }

    private float downX;
    private int left;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getVisibility() == View.GONE) return super.onTouchEvent(ev);
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        float x = ev.getRawX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                left = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                left += (x - downX);
                downX = x;
                layout(left, 0, left + width, height);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocity = mVelocityTracker.getXVelocity(0);
                UIHelper.showLog(TAG, "速度：" + velocity + ",left = " + left + ",width = " + width);
                if (left < -width / 4 || velocity < -700) {
                    //next
                    UIHelper.showLog(TAG, "next");
                    startAnim(1);
                } else if (left > width / 4 || velocity > 700) {
                    //last
                    UIHelper.showLog(TAG, "last");
                    startAnim(-1);
                } else {
                    //归位
                    UIHelper.showLog(TAG, "reset");
                    startAnim(0);
                }
                break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    //不要写在 dispatchTouchEvent 里！！这样会拦截child的触摸点击事件！
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (getVisibility() == View.GONE) return super.dispatchTouchEvent(ev);
//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(ev);
//        float x = ev.getRawX();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = x;
//                left = 0;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                left += (x - downX);
//                downX = x;
//                layout(left, 0, left + width, height);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mVelocityTracker.computeCurrentVelocity(1000);
//                float velocity = mVelocityTracker.getXVelocity(0);
//                UIHelper.showLog(TAG, "速度：" + velocity + ",left = " + left + ",width = " + width);
//                if (left < -width / 4 || velocity < -700) {
//                    //next
//                    UIHelper.showLog(TAG, "next");
//                    startAnim(1);
//                } else if (left > width / 4 || velocity > 700) {
//                    //last
//                    UIHelper.showLog(TAG, "last");
//                    startAnim(-1);
//                } else {
//                    //归位
//                    UIHelper.showLog(TAG, "reset");
//                    startAnim(0);
//                }
//                break;
//        }
//        return true;
////        return super.dispatchTouchEvent(ev);
//    }

    private void startAnim(int type) {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        int endValue;
        switch (type) {
            case 0:
                endValue = 0;
                break;
            case 1:
                //next
                endValue = -width;
                break;
            case -1:
                //last
                endValue = width;
                break;
            default:
                return;
        }
        valueAnimator = ValueAnimator.ofInt(left, endValue);
        long duration = (long) (Math.abs((endValue - left)) * 1.0f * 1000 / width);
        UIHelper.showLog(TAG, "动画 start = " + left + ",end = " + endValue + ",duration = " + duration);
        if (duration <= 0) return;
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            layout(value, 0, value + width, height);
            if (value == endValue) {
                left = 0;
                if (onMoveListener != null) {
                    if (type == 1) {
                        onMoveListener.onChange(true);
                    } else if (type == -1) {
                        onMoveListener.onChange(false);
                    }
                }
            }
        });
        valueAnimator.start();
    }
}
