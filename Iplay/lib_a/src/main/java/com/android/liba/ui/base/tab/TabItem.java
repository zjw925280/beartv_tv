package com.android.liba.ui.base.tab;



import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.android.liba.ui.base.BaseFragment;


public class TabItem {
    private int  titleId;
    private int normalBackground;
    private int SelectBackground;

    public View getView() {
        return view;
    }

    public void attachView(View view) {
        this.view = view;
        TextView tv=(TextView)view;
        tv.setText(getTitleId());
        Drawable drawable=tv.getContext().getDrawable(getNormalBackground());
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable,null,null);
    }
    public void select(boolean isSelect)
    {
        TextView tv=(TextView)view;
        tv.setSelected(isSelect);
        Drawable drawable=tv.getContext().getDrawable(isSelect?getSelectBackground():getNormalBackground());
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(null,drawable,null,null);
    }
    private View view;

    public int getNormalBackground() {
        return normalBackground;
    }

    public int getSelectBackground() {
        return SelectBackground;
    }

    public int getTitleId() {
        return titleId;
    }
    private BaseFragment fragment;

    public TabItem(int titleId, int normalBackground, int SelectBackground, BaseFragment fragment) {
        this.titleId = titleId;
        this.normalBackground = normalBackground;
        this.SelectBackground = SelectBackground;
        this.fragment = fragment;
    }
    public BaseFragment getFragment() {
        return fragment;
    }
    public int getTabTitle() {
        return titleId;
    }
}
