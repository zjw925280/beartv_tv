package com.android.liba.context;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.liba.network.ApiService;
import com.android.liba.service.UserService;
import com.android.liba.util.UIHelper;
import com.tencent.mmkv.MMKV;

import androidx.annotation.StringRes;

public class AppContext extends ContextWrapper {
    private static AppContext appContext;
    private Object api = null;

    public AppContext(Context base, Object net) {
        super(base);
        api = net;
    }

    public static boolean isOnMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static AppContext getInstance() {
        if (appContext == null) {
            throw new IllegalStateException("un init ");
        }
        return appContext;
    }

    public static UserService getUserService() {
        return UserService.getInstance();
    }

    public static boolean isDebug() {
        try {
            ApplicationInfo info = appContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static MMKV getMMKV() {
        if (MMKV.getRootDir() == null) {
            Log.i("AppContext", "getMMKV: MMKV.getRootDir() == null initialize ");
            MMKV.initialize(AbstractApplication.application);
        }
        return MMKV.defaultMMKV();
    }

    public static MMKV getMMKV(String id) {
        return MMKV.mmkvWithID(id);
    }

    public static MMKV getRaceMMKV(String id) {
        return MMKV.mmkvWithID(id, MMKV.MULTI_PROCESS_MODE);
    }

    public static void init(Application context, Object net) {
        init(context, net, false);
    }

    public static void init(Application context, Object net, boolean isFocus) {
        if (!isFocus && appContext != null)
            return;
        appContext = new AppContext(context, net);
        String rootDir = MMKV.initialize(appContext);
        UIHelper.showLog("MMKV", "mmkv root: " + rootDir);
        if (isDebug()) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
    }

    public static void showToast(String msg) {
        if (!isOnMainThread()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> showToast(msg));
            return;
        }
        if (msg == null || msg.isEmpty()) {
            return;
        }
        Toast toast = Toast.makeText(appContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(int id) {
        Toast toast = Toast.makeText(appContext, AppContext.getInstance().getString(id), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 要外传Context 是因为多语言得用Activity的Context才行！不然获取到的都是默认语言
     */
    public static void showToast(Context context, int id) {
        Toast toast = Toast.makeText(appContext, context.getString(id), Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 要外传Context 是因为多语言得用Activity的Context才行！不然获取到的都是默认语言
     */
    public static void showToast(Context context, String str) {
        Toast toast = Toast.makeText(appContext, str, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static String getStringRes(@StringRes int stringRes, Object... formatArgs) {
        return getInstance().getResources().getString(stringRes, formatArgs);
    }

    public <T extends ApiService> T getApi() {
        return (T) api;
    }
}
