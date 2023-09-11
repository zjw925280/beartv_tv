package com.android.liba.util.download;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.android.liba.util.UIHelper;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTaskListener;
import com.arialyy.aria.core.task.DownloadTask;

/**
 * 使用 Aria  https://github.com/AriaLyy/Aria/
 */
public class DownLoadUtils implements LifecycleObserver, DownloadTaskListener {
    private static final String TAG = "DownLoadApkUtils";
    private String path;
    private long taskId;
    private String url;
    private DownLoadListener listener;

    public long getTaskId() {
        return taskId;
    }

    public void setListener(DownLoadListener listener) {
        this.listener = listener;
    }

    public DownLoadUtils(LifecycleOwner lifecycleOwner, String url, String path) {
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
        this.url = url;
        this.path = path;
        Aria.download(this).register();
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(LifecycleOwner owner) {
        UIHelper.showLog(TAG, "onDestroy " + owner);
        listener = null;
        Aria.download(this).unRegister();
    }

//    @Download.onTaskRunning
//    protected void running(DownloadTask task) {
//        UIHelper.showLog(TAG, "onTaskRunning: " + task.getPercent());
//        if (listener != null) {
//            listener.setProgress(task.getPercent(), task);
//        }
//    }

//
//    @Download.onTaskComplete
//    void taskComplete(DownloadTask task) {
//        //在这里处理任务完成的状态
//        UIHelper.showLog(TAG, "taskComplete: " + task);
//        if (listener != null) {
//            listener.complete(task);
//        }
//    }

//    @Download.onTaskFail
//    void taskFail(DownloadTask task) {
//        //在这里处理任务完成的状态
//        UIHelper.showLog(TAG, "taskComplete: " + task);
//        if (listener != null) {
//            listener.taskFail(task);
//        }
//    }

    public void stop() {
        Aria.download(this).load(taskId).stop();
    }

    public void resume() {
        Aria.download(this).load(taskId).resume();
    }

    public long create() {
        taskId = Aria.download(this).load(url).setFilePath(path).create();
        return taskId;
    }

    @Override
    public void onWait(DownloadTask task) {

    }

    @Override
    public void onPre(DownloadTask task) {

    }

    @Override
    public void onTaskPre(DownloadTask task) {

    }

    @Override
    public void onTaskResume(DownloadTask task) {

    }

    @Override
    public void onTaskStart(DownloadTask task) {

    }

    @Override
    public void onTaskStop(DownloadTask task) {

    }

    @Override
    public void onTaskCancel(DownloadTask task) {

    }

    @Override
    public void onTaskFail(DownloadTask task, Exception e) {
        //在这里处理任务完成的状态
        UIHelper.showLog(TAG, "taskComplete: " + task);
        if (listener != null) {
            listener.taskFail(task);
        }
    }

    @Override
    public void onTaskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        UIHelper.showLog(TAG, "taskComplete: " + task);
        if (listener != null) {
            listener.complete(task);
        }
    }

    @Override
    public void onTaskRunning(DownloadTask task) {
        UIHelper.showLog(TAG, "onTaskRunning: " + task.getPercent());
        if (listener != null) {
            listener.setProgress(task.getPercent(), task);
        }
    }

    @Override
    public void onNoSupportBreakPoint(DownloadTask task) {

    }
}
