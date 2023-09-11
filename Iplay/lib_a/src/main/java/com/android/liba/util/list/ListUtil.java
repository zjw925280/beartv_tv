package com.android.liba.util.list;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.liba.jk.OnScrollListener;

public class ListUtil {
    public static void initRecyclerViewH(Context context, RecyclerView mRecyclerView) {
        initRecyclerView(context, mRecyclerView, 0, 0, 0);
    }

    public static void initRecyclerViewV(Context context, RecyclerView mRecyclerView) {
        initRecyclerView(context, mRecyclerView, 0, 0, 1);
    }

    public static void initRecyclerViewV(Context context, RecyclerView mRecyclerView, int type, int gNum) {
        initRecyclerView(context, mRecyclerView, type, gNum, 1);
    }

    public static void initRecyclerView(Context context, RecyclerView mRecyclerView, int type, int gNum) {
        initRecyclerView(context, mRecyclerView, type, gNum, 1);
    }

    /**
     * 初始化RecyclerView
     *
     * @param type   0:普通列表，1：普通GridView样式，2：瀑布流样式
     * @param gNum   如果type = 1 或 2，则该值表示列数
     * @param orient 0:水平，1垂直
     */
    public static void initRecyclerView(Context context, RecyclerView mRecyclerView, int type, int gNum, int orient) {
        mRecyclerView.setHasFixedSize(true);
        if (type == 0) {
            MyLinearLayoutManager mLayoutManager = new MyLinearLayoutManager(context.getApplicationContext(), orient == 0 ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else if (type == 1) {
            MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(context.getApplicationContext(), gNum, orient == 0 ? GridLayoutManager.HORIZONTAL : GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            MyStaggeredGridLayoutManager staggeredGridLayoutManager =
                    new MyStaggeredGridLayoutManager(gNum, orient == 0 ? StaggeredGridLayoutManager.HORIZONTAL : StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        }
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);
        mRecyclerView.setItemAnimator(animator);
    }

    public static void setOnListScrollListener(final RecyclerView mRecyclerView, final OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisiblePosition;
            int firstVisibleItemPosition;
            int scrolledX;
            int scrolledY;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledX += dx;
                scrolledY += dy;
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager1 = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findLastCompletelyVisibleItemPositions(lastPositions);
                    lastVisiblePosition = findMax(lastPositions);
                    int[] firstPositions = new int[layoutManager1.getSpanCount()];
                    layoutManager1.findFirstVisibleItemPositions(firstPositions);
                    firstVisibleItemPosition = findMin(firstPositions);
                }
                if (onScrollListener != null)
                    onScrollListener.onScroll(scrolledX, scrolledY, firstVisibleItemPosition, lastVisiblePosition);

            }

            //To find the maximum value in the array
            private int findMax(int[] lastPositions) {
                int max = lastPositions[0];
                for (int value : lastPositions) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }

            private int findMin(int[] firstPositions) {
                int min = firstPositions[0];
                for (int value : firstPositions) {
                    if (value < min) {
                        min = value;
                    }
                }
                return min;
            }

        });
    }

    /**
     * 滑动到 position 并尽量置顶
     */
    public static void scrollToPosition(RecyclerView recyclerView, int needKeepTopPosition) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearLayoutManager == null) return;
        //获取当前RecycleView屏幕可见的第一项和最后一项的Position
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        if (needKeepTopPosition < firstVisibleItemPosition) {
            //要置顶的项在当前显示的第一项之前
            recyclerView.smoothScrollToPosition(needKeepTopPosition);
        } else if (needKeepTopPosition < lastVisibleItemPosition) {
            //要置顶的项已经在屏幕上显示，计算它离屏幕原点的距离
            int top = recyclerView.getChildAt(needKeepTopPosition - firstVisibleItemPosition).getTop();
            recyclerView.smoothScrollBy(0, top);
        } else {
            //要置顶的项在当前显示的最后一项之后
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                        int n = needKeepTopPosition - linearLayoutManager.findFirstVisibleItemPosition();
//                        if (n >= 0 && n < recyclerView.getChildCount()) {
//                            //获取要置顶的项顶部距离RecyclerView顶部的距离
//                            int top = recyclerView.getChildAt(n).getTop();
//                            //进行第二次滚动（最后的距离）
//                            recyclerView.smoothScrollBy(0, top);
//                        }
//                        recyclerView.removeOnScrollListener(this);
//                    }
//                }
//            });
//            recyclerView.smoothScrollToPosition(needKeepTopPosition);

            recyclerView.scrollToPosition(needKeepTopPosition);
            recyclerView.postDelayed(() -> {
                int n = needKeepTopPosition - linearLayoutManager.findFirstVisibleItemPosition();
                if (n >= 0 && n < recyclerView.getChildCount()) {
                    //获取要置顶的项顶部距离RecyclerView顶部的距离
                    int top = recyclerView.getChildAt(n).getTop();
                    //进行第二次滚动（最后的距离）
                    recyclerView.smoothScrollBy(0, top);
                }
            }, 50);
        }
    }

    public static boolean isRecyclerViewHOrientation(RecyclerView mRecyclerView) {
        if (mRecyclerView == null) return false;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        //不需要 GridLayoutManager 因为 GridLayoutManager 继承 LinearLayoutManager
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            return manager.getOrientation() == StaggeredGridLayoutManager.HORIZONTAL;
        }
        return false;
    }

} 
