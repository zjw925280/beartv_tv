package com.android.liba.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.liba.BuildConfig;
import com.android.liba.jk.OnSimpleListener;
import com.google.android.material.tabs.TabLayout;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;

public class UIHelper {


    public static void setEditTextAction(final Activity activity, final EditText editText, final OnSimpleListener onActionListener) {
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_GO) {
                    if (activity != null) KeyboardUtil.hideSoftInput(activity);
                    if (onActionListener != null) {
                        onActionListener.onListen();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 设置弹窗不允许触摸、返回键关闭
     */
    public static void setDialogNoDismissByTouchAndBackPress(Dialog dialog) {
        if (dialog == null) return;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

//    public static void removeTabLayoutLongClick(TabLayout tabLayout) {
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.view.setLongClickable(false);
//                // 针对android 26及以上需要设置setTooltipText为null
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    // 可以设置null也可以是""
//                    tab.view.setTooltipText(null);
////                     tab.view.setTooltipText("");
//                }
//            }
//        }
//    }

    /**
     * 给TabLayout设置特殊的Indicator
     */
    public static void setTabLayoutIndicator(TabLayout tabLayout, int drawId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Drawable drawable = ContextCompat.getDrawable(tabLayout.getContext(), drawId);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable});
            layerDrawable.setLayerGravity(0, Gravity.CENTER | Gravity.BOTTOM);
            UIHelper.showLog("layerDrawable = " + layerDrawable);
            tabLayout.setSelectedTabIndicator(null);
            tabLayout.setSelectedTabIndicator(layerDrawable);
        }
    }

    /**
     * MD5加密
     */
    public static String md5(String string) {
        try {
            return md5(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
    }

    public static String md5(byte[] bytes) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 获取网络文件大小
     */
    public static long getHttpFileLengthRunB(final String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int responseCode = connection.getResponseCode();
            long contentLengthLong;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                contentLengthLong = connection.getContentLengthLong();
            } else {
                contentLengthLong = connection.getContentLength();
            }
            connection.disconnect();
            if (responseCode == 200) {
                return contentLengthLong;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public static <T> List<T> listQuChong(List<T> list) {
        List<T> tmpList = new ArrayList<>();
        for (T next : list) {
            if (!tmpList.contains(next)) {
                tmpList.add(next);
            }
        }
        list.clear();
        list.addAll(tmpList);
        tmpList.clear();
        return list;
    }

    public static <T> T[] list2Array(List<T> list, T[] array) {
        return list.toArray(array);
    }

//    new Object[]不能强转，会报错
//    public static <T> T[] list2Array(List<T> list) {
//        T[] array = (T[]) new Object[list.size()];
//        return list.toArray(array);
//    }

    public static <T> List<T> array2List(T[] array) {
        List<T> list = Arrays.asList(array);
        // 不能直接返回list，不是不能用，而是如果用这个list，则不能操作增删；
        // Arrays.asList 返回的 ArrayList 和一般用的 ArrayList 不是同一个
        return new ArrayList<T>(list);
    }

    /**
     * 使用外部浏览器打开链接
     */
    public static void openWebBrowser(Context context, String url) {
        UIHelper.showLog("openWebBrowser " + url);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isTodayFirst(Context context, String key) {
        return isTodayFirst(context, key, true);
    }

    /**
     * 是否是今天第一次
     */
    public static boolean isTodayFirst(Context context, String key, boolean isSave) {
        String currentActivityDate = UIHelper.timeLongHaoMiaoToString(System.currentTimeMillis(), "yyyy_MM_dd");
        String activityDate = SharePHelper.instance(context).readObject(key + "activity_date");
        if (activityDate == null || !activityDate.equals(currentActivityDate)) {
            if (isSave) {
                SharePHelper.instance(context).saveObject(key + "activity_date", currentActivityDate);
            }
            return true;
        }
        return false;
    }

    /**
     * 毫秒 时间戳转换为String
     */
    public static String timeLongHaoMiaoToString(long millis, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(new Date(millis));
    }

    public static boolean isRunInUIThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static void runOnUIThread(Runnable runnable) {
        if (isRunInUIThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    public static int getDp1Px() {
        return 1;
    }

    public static void showLog(String text) {
        showLog(null, text);
    }

    public static void showLog(Object tag, String text) {
        if (!BuildConfig.DEBUG) return;
        if (text != null && text.length() > 2048) {
            while (text.length() > 2048) {
                String show = text.substring(0, 2048);
                text = text.substring(2048);
                showLog1(tag, show);
            }
        } else {
            showLog1(tag, text);
        }
    }

    private static void showLog1(Object tag, String text) {
        if (BuildConfig.DEBUG) {
            if (tag == null) {
                Log.e("Log", text == null ? "null" : text);
            } else {
                if (tag instanceof String) {
                    Log.e(tag.toString(), text == null ? "null" : text);
                } else {
                    Log.e(tag.getClass().getSimpleName(), text == null ? "null" : text);
                }
            }
        }
    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) return (int) (dipValue * 2);
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5 * (dipValue >= 0 ? 1 : -1));
    }

    public static int px2dip(Context context, float pxValue) {
        if (context == null) return (int) (pxValue / 2);
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        if (context == null) return (int) (pxValue / 2);
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        if (context == null) return (int) (spValue * 2);
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * View绘制监听，回调后可正常获取View的宽高属性
     * 注：未show的fragment中view进行监听时，返回的宽高还是0！！
     */
    public static void getViewDrawListen(final View view, final OnSimpleListener onSimpleListener) {
        if (view.getWidth() != 0) {
            if (onSimpleListener != null) {
                onSimpleListener.onListen();
            }
            return;
        }
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                if (onSimpleListener != null) {
                    onSimpleListener.onListen();
                }
                return true;
            }
        });
    }

    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

}
