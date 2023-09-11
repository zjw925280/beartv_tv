package com.android.liba.util;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class LifeUtil {

    private static final Map<Lifecycle, MyLifeObserver> map = new HashMap<>();

    public static void addListener(LifecycleOwner lifecycleOwner, OnLifeListener onListener) {
        addListener(lifecycleOwner.getLifecycle(), onListener);
    }

    public static void addListener(ComponentActivity activity, OnLifeListener onListener) {
        addListener(activity.getLifecycle(), onListener);
    }

    public static void addListener(Fragment fragment, OnLifeListener onListener) {
        addListener(fragment.getLifecycle(), onListener);
    }

    public static void addListener(Lifecycle lifecycle, OnLifeListener onListener) {
        MyLifeObserver observer = new MyLifeObserver(lifecycle, onListener);
        lifecycle.addObserver(observer);
        map.put(lifecycle, observer);
    }

    public static void removeListener(ComponentActivity activity, OnLifeListener onListener) {
        removeListener(activity.getLifecycle(), onListener);
    }

    public static void removeListener(Fragment fragment, OnLifeListener onListener) {
        removeListener(fragment.getLifecycle(), onListener);
    }

    public static void removeListener(Lifecycle lifecycle, OnLifeListener onListener) {
        if (map.containsKey(lifecycle)) {
            MyLifeObserver observer = map.get(lifecycle);
            if (observer == null) {
                map.remove(lifecycle);
                return;
            }
            if (onListener == null || onListener == observer.getOnListener()) {
                lifecycle.removeObserver(observer);
                map.remove(lifecycle);
            }
        }
    }

    public static void printMap() {
        if (map.size() == 0) {
            UIHelper.showLog("LifeUtil", "map empty");
        } else {
            for (Lifecycle lifecycle : map.keySet()) {
                UIHelper.showLog("LifeUtil", "map key = " + lifecycle + ",value = " + map.get(lifecycle));
            }
        }
    }

    public static class OnSimpleLifeListener implements OnLifeListener {

        @Override
        public void onCreate() {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void onAny() {

        }
    }

    public interface OnLifeListener {
        void onCreate();

        void onStart();

        void onResume();

        void onPause();

        void onStop();

        void onDestroy();

        void onAny();
    }

    private static class MyLifeObserver implements LifecycleObserver {

        private final OnLifeListener onListener;
        private final Lifecycle lifecycle;

        public MyLifeObserver(Lifecycle lifecycle, OnLifeListener onListener) {
            this.lifecycle = lifecycle;
            this.onListener = onListener;
        }

        public OnLifeListener getOnListener() {
            return onListener;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        public void onCreate() {
            if (onListener == null) {
                return;
            }
            onListener.onCreate();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onStart() {
            if (onListener == null) {
                return;
            }
            onListener.onStart();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            if (onListener == null) {
                return;
            }
            onListener.onResume();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            if (onListener == null) {
                return;
            }
            onListener.onPause();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onStop() {
            if (onListener == null) {
                return;
            }
            onListener.onStop();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            if (onListener == null) {
                return;
            }
            onListener.onDestroy();

            removeListener(lifecycle, onListener);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void onAny() {
            //只要生命周期有变动，都会调用这个
            if (onListener == null) {
                return;
            }
            onListener.onAny();
        }
    }
} 
