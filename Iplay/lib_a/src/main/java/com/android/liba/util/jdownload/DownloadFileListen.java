package com.android.liba.util.jdownload;

public interface DownloadFileListen {
    void onPrepare();

    void onStart(long total);

    void onCancel();

    void onFinish(String filePath);

    void onFail(String error);

    void onLoading(long loadedLength, float progress);
}
