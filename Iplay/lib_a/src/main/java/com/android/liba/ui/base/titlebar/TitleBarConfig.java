package com.android.liba.ui.base.titlebar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

public class TitleBarConfig {

    private final ViewGroup contain;
    private ViewGroup mTitleBar;
    public Activity getActivity() {
        return activity;
    }
    private Activity activity;
    protected ITitleBar iTitleBar;
    protected String title = "";
    protected int leftImageRes;
    protected int centerImageRes;
    protected int rightImageRes;
    protected String rightString = "";
    protected int rightTextColor;
    protected int titleTextColor;

    protected int leftVisible;
    protected int rightVisible;
    protected int centerVisible;

    protected View.OnClickListener onCenterClickListener;
    protected View.OnClickListener onLeftClickListener;
    protected View.OnClickListener onRightClickListener;

    protected int backgroundColor;
    private int statusBarColor;
    private  LayoutMode layoutMode =  LayoutMode.TITLE_BAR;
    public enum LayoutMode {
        FULLSCREEN,
        TITLE_BAR,
        NULL
    }

    public TitleBarConfig setLayoutMode(LayoutMode layoutMode) {
        this.layoutMode = layoutMode;
        return this;
    }

    public TitleBarConfig setStatusBarColor(@ColorRes int color) {
        this.statusBarColor = color;
        return this;
    }

    public  LayoutMode getLayoutMode() {
        return layoutMode;
    }

    public int getColor() {
        return statusBarColor;
    }

    public TitleBarConfig(Activity target, ViewGroup contain, LayoutMode layoutMode, @ColorRes int color) {
        this.activity = target;
        this.layoutMode = layoutMode;
        this.contain=contain;
        this.statusBarColor = color;
    }



    public  void initTitleBar() {
        if (iTitleBar == null) {
            return;
        }
        mTitleBar = (ViewGroup) LayoutInflater.from(getActivity()).inflate(iTitleBar.getTitleLayout(), contain, false);
       iTitleBar.initTitleView(getActivity(), mTitleBar, this);
    }
   public View getTitleBar()
   {
       return mTitleBar;
   }

    public TitleBarConfig config(ITitleBar iTitleBar) {
        this.iTitleBar = iTitleBar;
        this.layoutMode = LayoutMode.TITLE_BAR;
        return this;
    }

    public TitleBarConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public TitleBarConfig setTitle(@StringRes int titleRes) {
        return setTitle(activity.getString(titleRes));
    }

    public TitleBarConfig setRightText(String rightString) {
        this.rightString = rightString;
        return this;
    }

    public TitleBarConfig setRightTextColor(@ColorRes int color) {
        this.rightTextColor = color;
        return this;
    }
    public TitleBarConfig setTitleTextColor(@ColorRes int color) {
        this.titleTextColor = color;
        return this;
    }

    public TitleBarConfig setRightText(@StringRes int rightStringRes) {
        return setRightText(activity.getString(rightStringRes));
    }

    public TitleBarConfig setRightImage(@DrawableRes int rightImageRes) {
        this.rightImageRes = rightImageRes;
        return this;
    }

    public TitleBarConfig setCenterImage(@DrawableRes int centerImageRes) {
        this.centerImageRes = centerImageRes;
        return this;
    }

    public TitleBarConfig setLeftImage(@DrawableRes int leftImage) {
        this.leftImageRes = leftImage;
        return this;
    }

    public TitleBarConfig setLeftVisible(boolean visible) {
        this.leftVisible = visible ? View.VISIBLE : View.INVISIBLE;
        return this;
    }

    public TitleBarConfig setRightVisible(boolean visible) {
        this.rightVisible = visible ? View.VISIBLE : View.INVISIBLE;
        return this;
    }

    public TitleBarConfig setCenterVisible(boolean visible) {
        this.centerVisible = visible ? View.VISIBLE : View.INVISIBLE;
        return this;
    }

    public TitleBarConfig setOnLeftClickListener(View.OnClickListener onClickListener) {
        this.onLeftClickListener = onClickListener;
        return this;
    }

    public TitleBarConfig setOnRightClickListener(View.OnClickListener onClickListener) {
        this.onRightClickListener = onClickListener;
        return this;
    }

    public TitleBarConfig setOnCenterClickListener(View.OnClickListener onClickListener) {
        this.onCenterClickListener = onClickListener;
        return this;
    }

    public TitleBarConfig setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(getTitleBar()!=null)
        {
            getTitleBar().setBackgroundResource(backgroundColor);
        }
        else
            throw  new IllegalStateException("Title Bar has no Init ");
        return this;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isShowTitle() {
        return iTitleBar != null;
    }

    public void clear() {
        iTitleBar = null;
        onCenterClickListener = null;
        onLeftClickListener = null;
        onRightClickListener = null;
    }
}
