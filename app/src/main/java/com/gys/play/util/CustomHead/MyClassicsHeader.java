package com.gys.play.util.CustomHead;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gys.play.MyApplication;
import com.gys.play.R;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;

public class MyClassicsHeader extends MyClassicsAbstract<MyClassicsHeader> implements RefreshHeader {

    private TextView tvHeader;

    public MyClassicsHeader(Context context) {
        this(context, null);
    }

    public MyClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        inflate(context, R.layout.my_classics_header, this);
        View thisView = this;
        tvHeader = thisView.findViewById(R.id.tv_header);
        ImageView img_refresh = thisView.findViewById(R.id.img_refresh);
        Animation animation = AnimationUtils.loadAnimation(MyApplication.getInstance(), R.anim.load_animation);
        img_refresh.startAnimation(animation);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            tvHeader.setText(R.string.loading__);
        } else {
            tvHeader.setText(R.string.refresh_failure);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                tvHeader.setText(R.string.pull_down_refresh);
                break;
            case Refreshing:
            case RefreshReleased:
                tvHeader.setText(R.string.loading__);
                break;
            case ReleaseToRefresh:
                tvHeader.setText(R.string.release_refresh_now);
                break;
            case ReleaseToTwoLevel:
                break;
            case Loading:
                break;
        }
    }
}
