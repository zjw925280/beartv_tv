package com.android.liba.context;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.liba.network.ApiManager;
import com.android.liba.util.UIHelper;
import com.arialyy.aria.core.Aria;

public abstract class AbstractApplication extends Application implements AppProvider {

    public static Application application;

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        ARouter.init(this);
        resetHttp();
        //初始化下载
        try {
            Aria.init(this);
        } catch (Throwable e) {
            UIHelper.showLog("AbstractApplication", "onCreate Aria.init Throwable " + e);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    public void resetHttp() {
        ApiManager.init(this);
        AppContext.init(this, ApiManager.getApiManager().createApi(), true);
    }

    @Override
    public void onHttpError(Throwable t) {

    }
}
