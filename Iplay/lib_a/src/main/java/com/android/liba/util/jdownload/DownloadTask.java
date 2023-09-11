package com.android.liba.util.jdownload;

import android.text.TextUtils;

import com.android.liba.R;
import com.android.liba.util.UIHelper;
import com.android.liba.util.task.TaskDelayBManager;
import com.android.liba.util.task.TaskDelayManager;


public class DownloadTask {

    private final String TAG = "DownloadTask";
    private String url;
    private String saveDir;
    private String saveName;
    private DownloadFileListen downloadListen;
    private RangeDownload rangeDownload;
    private String key = "";
    private boolean cancelEnable = true;//是否允许取消，有需要后台下载的，在cancelAll时不取消

    DownloadTask(String url, String saveDir, String saveName, DownloadFileListen downloadListen) {
        this.url = url;
        this.saveDir = saveDir;
        this.saveName = saveName;
        this.downloadListen = downloadListen;
        this.key = DownloadManager.instance().getKey(url, saveDir, saveName);
    }

    DownloadTask(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setCancelEnable(boolean cancelEnable) {
        this.cancelEnable = cancelEnable;
    }

    public boolean isCancelEnable() {
        return cancelEnable;
    }

    public void start() {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName))
            return;
        if (DownloadManager.instance().isStarting(this)) {
            UIHelper.showLog(TAG, "已经正在下载，请勿重复点击");
            return;
        }
        DownloadManager.instance().onDownloadTaskDoStart(this);
        try {
            realStart();
        } catch (Exception e) {
            e.printStackTrace();
            DownloadManager.instance().onDownloadTaskCancelOrFinishOrFail(DownloadTask.this);
            if (downloadListen != null)
                downloadListen.onFail("下载失败，请重试。--" + e.getMessage());
        }
    }

    private void realStart() {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName))
            return;
        if (UIHelper.isRunInUIThread()) {
            new TaskDelayBManager() {
                @Override
                public void onListen(Long index) {
                    simpleDownload();
                }
            }.start();
        } else {
            simpleDownload();
        }
    }

    private boolean isFinished;
    private long outLoadedLength;
    private boolean isCanceled;
    private boolean isFailed;
    private boolean isStarted;
    private boolean isPrepared;

    private void simpleDownload() {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName))
            return;
        isFinished = isCanceled = isFailed = isStarted = isPrepared = false;
        outLoadedLength = 0;
        rangeDownload = new RangeDownload();
        rangeDownload.download(url, saveDir, saveName, new DownloadFileListen() {

            @Override
            public void onPrepare() {
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                if (isPrepared) return;
                isPrepared = true;
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null) downloadListen.onPrepare();
                    }
                }.start();
            }

            @Override
            public void onStart(final long l) {
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                if (isStarted) return;
                isStarted = true;
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null) downloadListen.onStart(l);
                    }
                }.start();
            }

            @Override
            public void onCancel() {
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                isCanceled = true;
                DownloadManager.instance().onDownloadTaskCancelOrFinishOrFail(DownloadTask.this);
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null) downloadListen.onCancel();
                    }
                }.start();
            }

            @Override
            public void onFinish(final String filePath) {
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                isFinished = true;
                DownloadManager.instance().onDownloadTaskCancelOrFinishOrFail(DownloadTask.this);
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null) downloadListen.onFinish(filePath);
                    }
                }.start();
            }

            @Override
            public void onFail(final String s) {
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                isFailed = true;
                DownloadManager.instance().onDownloadTaskCancelOrFinishOrFail(DownloadTask.this);
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null) downloadListen.onFail(s);
                    }
                }.start();
            }

            @Override
            public void onLoading(final long loadedLength, final float progress) {
                if (loadedLength <= outLoadedLength) {
                    return;
                }
                outLoadedLength = loadedLength;
                if (isFinished) return;
                if (isCanceled) return;
                if (isFailed) return;
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long aLong) {
                        if (downloadListen != null)
                            downloadListen.onLoading(outLoadedLength, progress);
                    }
                }.start();
            }
        });
    }

    public void cancel() {
        cancel(false);
    }

    public void cancel(boolean isFromCancelAll) {
        if (!cancelEnable) return;
        UIHelper.showLog(TAG, "cancel");
        if (!DownloadManager.instance().isStarting(this)) return;
        if (!isFromCancelAll) {
            DownloadManager.instance().onDownloadTaskCancelOrFinishOrFail(DownloadTask.this);
        }
        if (rangeDownload != null) {
            rangeDownload.cancel();
            rangeDownload = null;
            UIHelper.showLog(TAG, "rangeDownload.cancel");
        }
        if (isFinished) return;
        if (isCanceled) return;
        if (isFailed) return;
        isCanceled = true;
        if (downloadListen != null) {
            if (UIHelper.isRunInUIThread()) {
                if (downloadListen != null) downloadListen.onCancel();
            } else {
                new TaskDelayManager() {
                    @Override
                    public void onListen(Long index) {
                        if (downloadListen != null) downloadListen.onCancel();
                    }
                }.start();
            }
        }
    }

}
