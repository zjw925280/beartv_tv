package com.android.liba.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * Created by Koi.
 * Date: 2021/4/12
 * explain: 打造自动循环效果
 */
public class AutoPollRecyclerView extends RecyclerView {
    public static final long TIME_AUTO_POLL = 16;
    /*   1:向右一直播放循环(默认)
     Adapter 的getItemCount 需要设置Integer.MAX_VALUE*/
    public static int CIRCULATION_RIGHT = 1;
    /*2：向右播放到尽头后，向左播放（来回循环）
    Adapter 的getItemCount 需要设置列表长度*/
    public static int CIRCULATION_RIGHT_lEFT = 2;
    public static int CIRCULATION_TYPE = CIRCULATION_RIGHT;
    //播放速度
    public static int CIRCULATION_SPEED = 2;
    AutoPollTask autoPollTask;
    //标示是否正在自动轮询
    private boolean running;
    //标示是否可以自动轮询,可在不需要的是否置false
    private boolean canRun;

    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        autoPollTask = new AutoPollTask(this);
    }

    static class AutoPollTask implements Runnable {
        //默认向左移动
        private int scrollDirection = 1;
        private final WeakReference<AutoPollRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }

        @Override
        public void run() {
            if (CIRCULATION_TYPE == CIRCULATION_RIGHT) {
                //向右一直进行循环
                AutoPollRecyclerView recyclerView = mReference.get();
                if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                    recyclerView.scrollBy(CIRCULATION_SPEED, CIRCULATION_SPEED);
                    recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
                }
            } else if (CIRCULATION_TYPE == CIRCULATION_RIGHT_lEFT) {
                //向右播放到尽头之后，执行向左播放操作
                AutoPollRecyclerView recyclerView = mReference.get();
                if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                    if (recyclerView.canScrollHorizontally(scrollDirection)) {
                        recyclerView.scrollBy(CIRCULATION_SPEED * scrollDirection, CIRCULATION_SPEED * scrollDirection);
                    } else { //改变方向
                        scrollDirection = -scrollDirection;
                    }
                    recyclerView.postDelayed(recyclerView.autoPollTask, recyclerView.TIME_AUTO_POLL);
                }
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running)
            stop();
        canRun = true;
        running = true;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        running = false;
        removeCallbacks(autoPollTask);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (running)
                    stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canRun)
                    start();
                break;
        }
        return super.onTouchEvent(e);
    }

    public void setCirculationType(int CIRCULATION_TYPE) {
        this.CIRCULATION_TYPE = CIRCULATION_TYPE;
    }

}
