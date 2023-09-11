package com.android.liba.ui.base.titlebar;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liba.R;


public class TitleBarImageStringImage implements ITitleBar {

    private ImageView rightView;
    private TextView tvCenter;

    private TitleBarImageStringImage() {
    }

    public static TitleBarImageStringImage newInstance() {
        return new TitleBarImageStringImage();
    }

    @Override
    public int getTitleLayout() {
        return R.layout.titlebar_image_string_image;
    }

    @Override
    public void initTitleView(Activity activity, ViewGroup view, TitleBarConfig titleBarConfig) {
        tvCenter = (TextView) view.findViewById(R.id.title_center);
        ImageView leftView = (ImageView) view.findViewById(R.id.title_left);
        rightView = (ImageView) view.findViewById(R.id.title_right);

        tvCenter.setVisibility(titleBarConfig.centerVisible);
        rightView.setVisibility(titleBarConfig.rightVisible);
        leftView.setVisibility(titleBarConfig.leftVisible);

        tvCenter.setText(titleBarConfig.title);

        if (titleBarConfig.leftImageRes != 0) {
            leftView.setImageResource(titleBarConfig.leftImageRes);
        }
        if (titleBarConfig.rightImageRes != 0) {
            rightView.setImageResource(titleBarConfig.rightImageRes);
        }

        tvCenter.setOnClickListener(v -> {
            if (titleBarConfig.centerVisible != View.VISIBLE) {
                return;
            }
            if (titleBarConfig.onCenterClickListener != null) {
                titleBarConfig.onCenterClickListener.onClick(v);
            }
        });

        leftView.setOnClickListener(v -> {
            if (titleBarConfig.leftVisible != View.VISIBLE) {
                return;
            }
            if (titleBarConfig.onLeftClickListener == null) {
                if (activity != null) {
                    activity.finish();
                }
            } else {
                titleBarConfig.onLeftClickListener.onClick(v);
            }
        });
        rightView.setOnClickListener(v -> {
            if (titleBarConfig.rightVisible != View.VISIBLE) {
                return;
            }
            if (titleBarConfig.onRightClickListener != null) {
                titleBarConfig.onRightClickListener.onClick(v);
            }
        });
    }

    public ImageView getRightView() {
        return rightView;
    }

    public void setTitleContent(String title) {
        if (tvCenter != null) {
            tvCenter.setText(title);
        }
    }
}
