package com.android.liba.util.jdownload;

import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;


import com.android.liba.R;
import com.android.liba.context.AbstractApplication;
import com.android.liba.util.AppUtil;
import com.android.liba.util.FileUtil;
import com.android.liba.util.OkHttpClientUtil;
import com.android.liba.util.UIHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class RangeDownload {

    private final String TAG = "RangeDownload";
    private Call call;
    private boolean isCancel;

    public void cancel() {
        UIHelper.showLog(TAG, "cancel");
        isCancel = true;
        if (call != null && call.isExecuted() && !call.isCanceled()) {
            call.cancel();
            call = null;
            UIHelper.showLog(TAG, "call.cancel");
        }
    }

    /**
     * @param url            下载连接
     * @param resultFileDir  下载的文件储存目录
     * @param resultFileName 下载文件名称
     * @param listener       下载监听
     */
    public void download(final String url, final String resultFileDir, final String resultFileName, final DownloadFileListen listener) {
        if (TextUtils.isEmpty(url)) {
            if (listener != null)
                listener.onFail(AbstractApplication.application.getString(R.string.jj_download_path_excpt));
            return;
        }
        if (url.startsWith(Environment.getExternalStorageDirectory() + "")) {
            //url是本地文件地址，本身就是已经下载了的
            if (listener != null) {
                listener.onFinish(url);
            }
            return;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (listener != null) {
                UIHelper.showLog(TAG, "下载地址异常：" + url);
                listener.onFail(AbstractApplication.application.getString(R.string.jj_download_path_excpt));
            }
            return;
        }
        if (TextUtils.isEmpty(resultFileDir) || TextUtils.isEmpty(resultFileName)) {
            if (listener != null)
                listener.onFail(AbstractApplication.application.getString(R.string.jj_save_path_excpt));
            return;
        }
        final File resultFile = new File(resultFileDir, resultFileName);
        if (resultFile.exists() && resultFile.isFile() && resultFile.length() > 0) {
            if (listener != null) listener.onFinish(resultFile.getAbsolutePath());
            return;
        }
        if (!resultFile.getParentFile().exists()) {
            boolean mkdirs = resultFile.getParentFile().mkdirs();
            if (!mkdirs && !resultFile.getParentFile().exists()) {
                if (listener != null)
                    listener.onFail(AbstractApplication.application.getString(R.string.jj_create_file_failed));
                return;
            }
        }

        String linShiFileDir = FileUtil.getInsideCacheDir(AbstractApplication.application, "temp", true);
        String linShiFileName = resultFileName + ".ls";
        final File linShiFile = new File(linShiFileDir, linShiFileName);
//        UIHelper.showLog(TAG, "linShiFile=" + linShiFile.getAbsolutePath());
        Request request;
        final long loadedLength;
        final long contentLength;
        final boolean isRange;
        if (linShiFile.exists() && linShiFile.length() > 0) {
            contentLength = getContentLength(url);
            if (contentLength == 0) {
                boolean delete = linShiFile.delete();
                if (!delete) {
                    if (listener != null)
                        listener.onFail(AbstractApplication.application.getString(R.string.jj_file_handle_excpt));
                    return;
                } else {
                    isRange = false;
                }
            } else if (contentLength == linShiFile.length()) {
                onLinShiFileDownloadSuccess(linShiFile, resultFile, listener);
                return;
            } else {
                isRange = true;
            }
        } else {
            contentLength = 0;
            isRange = false;
        }
        if (!AppUtil.isNetworkAvalible(AbstractApplication.application)) {
            if (listener != null) listener.onFail(AbstractApplication.application.getString(R.string.http_error));
            return;
        }
        if (listener != null) listener.onPrepare();

//        UIHelper.showLog(TAG, "linShiFile=" + linShiFile.length());
//        UIHelper.showLog(TAG,  "contentLength=" + contentLength);
        if (isRange) {
            loadedLength = linShiFile.length();
            int loadedB = (int) (loadedLength * 100 / contentLength);
//            UIHelper.showLog(TAG,  "断点续传-" + loadedLength + "/" + contentLength + " = " + loadedB + "/100");
            request = new Request.Builder().addHeader("RANGE", "bytes=" + loadedLength + "-" + contentLength).url(url).build();
        } else {
//            UIHelper.showLog(TAG,  "完整下载");
            loadedLength = 0;
            request = new Request.Builder().url(url).build();
        }
        isCancel = false;
        call = OkHttpClientUtil.instance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 下载失败监听回调
                if (isCancel) {
                    if (listener != null) listener.onCancel();
                    return;
                }
                String message = e.getMessage();
                if (message != null) {
                    if (message.contains("Canceled")) {
                        if (listener != null) listener.onCancel();
                    } else {
                        if (listener != null) listener.onFail(message);
                    }
                } else {
                    if (listener != null)
                        listener.onFail(AbstractApplication.application.getString(R.string.jj_download_excpt));
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (isCancel) {
                    if (listener != null) listener.onCancel();
                    return;
                }
                InputStream is = null;
                FileOutputStream fos = null;
                ResponseBody body = null;
                try {
                    byte[] buf = new byte[2048];
                    int len;
                    // 储存下载文件的目录
                    if (!linShiFile.getParentFile().exists()) {
                        boolean mkdirs = linShiFile.getParentFile().mkdirs();
                        if (!mkdirs && !linShiFile.getParentFile().exists()) {
                            if (listener != null)
                                listener.onFail(AbstractApplication.application.getString(R.string.jj_file_create_failed));
                            return;
                        }
                    }
                    if (!linShiFile.exists()) {
                        UIHelper.showLog(TAG + "临时文件不存在，执行创建createFile");
                        FileUtil.createFile(linShiFile);
                    }
                    if (!linShiFile.exists()) {
                        UIHelper.showLog(TAG + "临时文件创建不成功");
                        if (listener != null)
                            listener.onFail(AbstractApplication.application.getString(R.string.jj_file_create_failed));
                        return;
                    }
                    body = response.body();
                    if (body == null) {
                        if (listener != null)
                            listener.onFail(AbstractApplication.application.getString(R.string.jj_unfound_file_data));
                        return;
                    }
                    is = body.byteStream();
                    long total = body.contentLength();
                    if (contentLength > total) {
                        //断点续传的时候，body 的长度是剩余长度，不是总长度
                        total = contentLength;
                    }
                    if (listener != null) listener.onStart(total);
                    fos = new FileOutputStream(linShiFile, true);
                    long sum = loadedLength;
                    int progress = 0;
                    if (isCancel) {
                        if (listener != null) listener.onCancel();
                        return;
                    }
                    while (!isCancel && (len = is.read(buf)) != -1) {
                        if (isCancel) {
                            if (listener != null) listener.onCancel();
                            return;
                        }
                        fos.write(buf, 0, len);
                        sum += len;
                        int progressNew = (int) (sum * 1.0f / total * 100);
                        if (progressNew > progress) {
                            progress = progressNew;
                            // 下载中更新进度条
                            if (listener != null) listener.onLoading(sum, progress);
                        }
                    }
                    if (isCancel) {
                        if (listener != null) listener.onCancel();
                        return;
                    }
                    if (listener != null) listener.onLoading(total, 100);
                    fos.flush();
                    // 下载完成
                    onLinShiFileDownloadSuccess(linShiFile, resultFile, listener);
                } catch (Exception e) {
                    if (isCancel) {
                        if (listener != null) listener.onCancel();
                        return;
                    }
                    e.printStackTrace();
                    if (listener != null) listener.onFail(e.getMessage());
                } finally {
                    if (body != null) {
                        body.close();
                    }
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onLinShiFileDownloadSuccess(File linShiFile, File resultFile, DownloadFileListen listener) {
        if (linShiFile.exists() && !resultFile.exists()) {
            boolean isSuccess = linShiFile.renameTo(resultFile);
            if (!isSuccess) {
                if (!linShiFile.exists() && resultFile.exists() && resultFile.length() > 0) {
                    //实际上是成功了
                    isSuccess = true;
                }
            }
            if (!isSuccess) {
                isSuccess = FileUtil.copyFile(linShiFile, resultFile);
                if (isSuccess && resultFile.exists() && resultFile.length() > 0) {
                    FileUtil.deleteFile(linShiFile);
                }
            }
            if (isSuccess) {
                //先通知刷新，再回调
                FileUtil.checkMediaFileUpdate(resultFile);
                if (listener != null) listener.onFinish(resultFile.getAbsolutePath());
            } else {
                if (listener != null)
                    listener.onFail(AbstractApplication.application.getString(R.string.jj_file_trans_save_failed));
            }
        } else if (resultFile.exists() && resultFile.length() > 0) {
            if (listener != null) listener.onFinish(resultFile.getAbsolutePath());
        }
    }

    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder().url(downloadUrl).build();
        try {
            Response response = OkHttpClientUtil.instance().getOkHttpClient().newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body == null) return 0;
                long contentLength = body.contentLength();
                body.close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

