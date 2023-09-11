package com.android.liba.ui.base;

import android.os.Bundle;
import android.view.View;

public interface ViewHandler {
     void click(int viewId, View.OnClickListener listener);
     void click(View view, View.OnClickListener listener);
     void longClick(View view,View.OnLongClickListener listener);
     int  getLayoutId();
     void initView(Bundle savedInstanceState, View fragmentView);
     void loadData(Bundle savedInstanceState);
     void setListener();
     //广州漫画需要的回调
     default void handlerErro(Throwable errorThrowable){}
}
