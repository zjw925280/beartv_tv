package com.android.liba.util.download;

import com.arialyy.aria.core.task.DownloadTask;

public interface DownLoadListener {

    void setProgress(int progress, DownloadTask task);

    void complete(DownloadTask task);

    void taskFail(DownloadTask task);
}
