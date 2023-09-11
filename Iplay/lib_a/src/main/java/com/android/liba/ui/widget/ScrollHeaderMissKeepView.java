package com.android.liba.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.liba.R;
import com.android.liba.util.UIHelper;
import com.android.liba.util.list.MyRecyclerView;

public class ScrollHeaderMissKeepView extends NestedScrollView {

    private View missView;
    private View keepView;
    private View scrollView;
    private boolean isContainerListRv;

    public ScrollHeaderMissKeepView(@NonNull @io.reactivex.annotations.NonNull Context context) {
        super(context);
    }

    public ScrollHeaderMissKeepView(@NonNull @io.reactivex.annotations.NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollHeaderMissKeepView(@NonNull @io.reactivex.annotations.NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkChild();
    }

    private void checkChild() {
        int childCount = getChildCount();
        if (childCount == 1) {
            View childAt = getChildAt(0);
            if (childAt instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) childAt;
                int childCount1 = childGroup.getChildCount();
                if (childCount1 == 2) {
                    missView = childGroup.getChildAt(0);
                    scrollView = childGroup.getChildAt(1);
                    checkListView();
                } else if (childCount1 == 3) {
                    missView = childGroup.getChildAt(0);
                    keepView = childGroup.getChildAt(1);
                    scrollView = childGroup.getChildAt(2);
                    checkListView();
                }
            }
        }
        if (scrollView != null) {
            UIHelper.getViewDrawListen(this, () -> {
                if (keepView != null) {
                    UIHelper.setViewHeight(scrollView, getHeight() - keepView.getHeight());
                } else {
                    UIHelper.setViewHeight(scrollView, getHeight());
                }
            });
        }
    }

    private void checkListView() {
        if (scrollView instanceof MyRecyclerView) {
            isContainerListRv = true;
        } else if (scrollView instanceof RecyclerView) {
            isContainerListRv = true;
        } else if (scrollView instanceof ViewGroup) {
            ViewGroup scrollViewGroup = (ViewGroup) scrollView;
            int childCount = scrollViewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = scrollViewGroup.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE) {
                    if (child instanceof RecyclerView) {
                        isContainerListRv = true;
                    }
                }
            }
        } else {
            isContainerListRv = false;
        }
    }

    private int getRvScrollY(RecyclerView recyclerView) {
        if (recyclerView instanceof MyRecyclerView) {
            return ((MyRecyclerView) recyclerView).getListScrollY();
        }
        Object tag = recyclerView.getTag(R.id.scroll_header_miss_keep_view_listener);
        if (tag instanceof MyListener) {
            MyListener myListener = (MyListener) tag;
            return myListener.listScrollY;
        } else {
            MyListener listener = new MyListener();
            recyclerView.setTag(R.id.scroll_header_miss_keep_view_listener, listener);
            recyclerView.addOnScrollListener(listener);
            return 0;
        }
    }

    private static class MyListener extends RecyclerView.OnScrollListener {
        private int listScrollY;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            listScrollY += dy;
        }
    }

    private int getListScrollY() {
        if (scrollView instanceof RecyclerView) {
            return getRvScrollY((RecyclerView) scrollView);
        } else if (scrollView instanceof ViewGroup) {
            ViewGroup scrollViewGroup = (ViewGroup) scrollView;
            int childCount = scrollViewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = scrollViewGroup.getChildAt(i);
                if (child.getVisibility() == View.VISIBLE) {
                    if (child instanceof RecyclerView) {
                        return getRvScrollY((RecyclerView) child);
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull @io.reactivex.annotations.NonNull View target, int dx, int dy, @NonNull @io.reactivex.annotations.NonNull int[] consumed, int type) {
        if (missView != null) {
            int scrollY = getScrollY();
            //往下滑动，dy<0;consumed[1] 是该类消耗掉的距离
            if (dy < 0) {
                //往下滑动
                if (isContainerListRv) {
                    // child优先消耗
                    if (scrollY > 0) {
                        int listScrollY = getListScrollY();
                        if (listScrollY > 0) {
                            if (-dy > listScrollY) {
                                consumed[1] = dy + listScrollY;
                            } else {
                                consumed[1] = 0;
                            }
                        }
                    }
                } else {
                    //以下是parent优先消耗的代码，怎么判断child可消耗呢
                    if (scrollY > 0) {
                        //parent 可消耗
                        int leftConsume = scrollY - missView.getHeight();//剩余可消费
                        if (dy > leftConsume) {
                            consumed[1] = dy;
                        } else {
                            consumed[1] = dy - leftConsume;
                        }
                    }
                }
            } else if (dy > 0) {
                //往上滑动,parent优先消耗
                if (scrollY < missView.getHeight()) {
                    int leftConsume = missView.getHeight() - scrollY;//剩余可消费
                    if (dy < leftConsume) {
                        consumed[1] = dy;
                    } else {
                        consumed[1] = dy - leftConsume;
                    }
                }
            }
            if (consumed[0] != 0 || consumed[1] != 0) {
                scrollBy(consumed[0], consumed[1]);
            }
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }


//    <com.android.baselib.ui.widget.ScrollHeaderMissKeepView
//    android:id="@+id/scrollView"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent">
//
//        <LinearLayout
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:orientation="vertical"
//    android:descendantFocusability="blocksDescendants">
//
//            <TextView
//    android:id="@+id/missLayout"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:text="111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"
//    android:textSize="@dimen/dp30" />
//
//            <TextView
//    android:id="@+id/keepLayout"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:background="@color/gray"
//    android:text="22222222"
//    android:textSize="@dimen/dp30" />
//
//            <androidx.recyclerview.widget.RecyclerView
//    android:id="@+id/listRv"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"/>
//
//        </LinearLayout>
//
//    </com.android.baselib.ui.widget.ScrollHeaderMissKeepView>
}
