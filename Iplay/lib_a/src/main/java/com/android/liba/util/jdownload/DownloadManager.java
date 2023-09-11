package com.android.liba.util.jdownload;

import android.content.Context;
import android.text.TextUtils;

import androidx.activity.ComponentActivity;

import com.android.liba.util.InstallUtil;
import com.android.liba.util.LifeUtil;
import com.android.liba.util.MyMap;
import com.android.liba.util.UIHelper;
import com.android.liba.util.task.TaskManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DownloadManager {

    private static DownloadManager downloadManager;
    private MyMap<String, DownloadTask> taskMyMap;

    public static synchronized DownloadManager instance() {
        if (downloadManager == null) {
            downloadManager = new DownloadManager();
        }
        return downloadManager;
    }

    private DownloadManager() {
        taskMyMap = new MyMap<>();
    }

    public synchronized DownloadTask create(String url, String saveDir, String saveName, final DownloadFileListen downloadFileListen) {
        DownloadTask downloadTask;
        final String key = getKey(url, saveDir, saveName);
        if (taskMyMap.containsKey(key)) {
            downloadTask = taskMyMap.get(key);
            if (downloadTask == null)
                downloadTask = new DownloadTask(url, saveDir, saveName, downloadFileListen);
        } else {
            downloadTask = new DownloadTask(url, saveDir, saveName, downloadFileListen);
        }
        return downloadTask;
    }

    public synchronized DownloadTask startDownload(String url, String saveDir, String saveName, DownloadFileListen downloadFileListen) {
        DownloadTask downloadTask = create(url, saveDir, saveName, downloadFileListen);
        downloadTask.start();
        return downloadTask;
    }

    public synchronized DownloadTask startDownload(String url, String resultFilePath, DownloadFileListen downloadFileListen) {
        File resultFile = new File(resultFilePath);
        DownloadTask downloadTask = create(url, resultFile.getParentFile().getAbsolutePath(), resultFile.getName(), downloadFileListen);
        downloadTask.start();
        return downloadTask;
    }

    public synchronized void startDownload(String[] urls, String[] resultFilePaths, DownloadFileListen downloadFileListen) {
        if (urls == null || urls.length == 0) {
            downloadFileListen.onFinish("");
            return;
        }
        startDownload(UIHelper.array2List(urls), UIHelper.array2List(resultFilePaths), false, downloadFileListen);
    }

    /**
     * @param isGetTotalLengthFirst 是否要先获取总任务文件总长度，如果是，将多增一个耗时操作，任务越多，耗时越长
     */
    public synchronized void startDownload(String[] urls, String[] resultFilePaths, boolean isGetTotalLengthFirst, DownloadFileListen downloadFileListen) {
        if (urls == null || urls.length == 0) {
            downloadFileListen.onFinish("");
            return;
        }
        startDownload(UIHelper.array2List(urls), UIHelper.array2List(resultFilePaths), isGetTotalLengthFirst, downloadFileListen);
    }

    /**
     * @param isGetTotalLengthFirst 是否要先获取总任务文件总长度，如果是，将多增一个耗时操作，任务越多，耗时越长
     */
    public synchronized void startDownload(List<String> urlList, List<String> resultFilePathList, boolean isGetTotalLengthFirst, DownloadFileListen downloadFileListen) {
        if (urlList == null || urlList.size() == 0) {
            downloadFileListen.onFinish("");
            return;
        }
        //任务去重，不能简单去重，有可能链接一样，但存储目录不一样，所以要url+filePath 去重
        List<String> tmpUrlAndFilePathList = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            if (i > resultFilePathList.size() - 1) continue;
            String filePath = resultFilePathList.get(i);
            tmpUrlAndFilePathList.add(url + "###" + filePath);
        }
        List<String> tmpList1 = UIHelper.listQuChong(tmpUrlAndFilePathList);
        urlList.clear();
        resultFilePathList.clear();
        for (String urlAndFilePath : tmpList1) {
            String[] split = urlAndFilePath.split("###");
            urlList.add(split[0]);
            resultFilePathList.add(split[1]);
        }
        tmpList1.clear();
        if (urlList.size() == 0) {
            downloadFileListen.onFinish("");
            return;
        }
        String[] urls = UIHelper.list2Array(urlList, new String[urlList.size()]);
        String[] resultFilePaths = UIHelper.list2Array(resultFilePathList, new String[resultFilePathList.size()]);
//        DownloadManager.instance().cancelAll();
        if (!isGetTotalLengthFirst) {
            startDownloadReal(urls, resultFilePaths, 0, downloadFileListen);
        } else {
            new TaskManager<Long>() {
                @Override
                public Long runOnBackgroundThread() {
                    if (downloadFileListen != null) {
                        downloadFileListen.onPrepare();
                    }
                    long totalLength = 0;
                    for (String url : urls) {
                        long length = UIHelper.getHttpFileLengthRunB(url);
                        totalLength += length;
                    }
                    return totalLength;
                }

                @Override
                public void runOnUIThread(Long totalLength) {
                    startDownloadReal(urls, resultFilePaths, totalLength, downloadFileListen);
                    cancel();
                }
            }.start();
        }
    }

    private synchronized void startDownloadReal(String[] urls, String[] resultFilePaths, long totalLengthInput, DownloadFileListen downloadFileListen) {
        final boolean[] isStarted = {false};
        final boolean[] isCanceled = {false};
        final boolean[] isFailed = {false};
        final boolean[] isFinished = {false};
        long[] totalLengths = new long[urls.length];
        long[] loadedLengths = new long[urls.length];
        DownloadTask[] downloadTasks = new DownloadTask[urls.length];
        final int[] finishCount = {0};
        final long[] totalLength = {totalLengthInput};
        for (int i = 0; i < urls.length; i++) {
            final String url = urls[i];
            final int index = i;
            String resultFilePath = resultFilePaths[i];
            downloadTasks[i] = startDownload(url, resultFilePath, new SimpleDownloadFileListen() {

                private void cancelDownloadTasks() {
                    for (DownloadTask downloadTask : downloadTasks) {
                        if (downloadTask != null) {
                            downloadTask.cancel();
                        }
                    }
                }

                @Override
                public void onStart(long total) {
                    if (!isStarted[0]) {
                        isStarted[0] = true;
                        if (downloadFileListen != null) {
                            downloadFileListen.onStart(getTotalLength());
                        }
                    }
                    totalLengths[index] = total;
                }

                @Override
                public void onCancel() {
                    if (!isCanceled[0]) {
                        isCanceled[0] = true;
//                        DownloadManager.instance().cancelAll();
                        cancelDownloadTasks();
                        if (downloadFileListen != null) {
                            downloadFileListen.onCancel();
                        }
                    }
                }

                @Override
                public void onFail(String error) {
                    if (isCanceled[0]) return;
                    if (!isFailed[0]) {
                        isFailed[0] = true;
//                        DownloadManager.instance().cancelAll();
                        cancelDownloadTasks();
                        if (downloadFileListen != null) {
                            downloadFileListen.onFail(error);
                        }
                    }
                }

                @Override
                public void onFinish(String filePath) {
                    if (isCanceled[0]) return;
                    finishCount[0]++;
                    loadedLengths[index] = totalLengths[index];
                    if (!isFinished[0]) {
                        if (finishCount[0] == urls.length) {
                            isFinished[0] = true;
                            if (downloadFileListen != null) {
                                downloadFileListen.onFinish("");
                            }
                        }
                    }
                }

                @Override
                public void onLoading(long loadedLength, float progress) {
                    if (isCanceled[0]) return;
                    if (loadedLength <= loadedLengths[index]) {
                        //进度回退或不动
                        return;
                    }
                    loadedLengths[index] = loadedLength;

                    if (!isFinished[0]) {
                        long loadedLengthTotal = 0;
                        for (long itemLoadedLength : loadedLengths) {
                            loadedLengthTotal += itemLoadedLength;
                        }
                        if (loadedLengthTotal <= getTotalLength()) {
                            float totalProgress = loadedLengthTotal * 100f / getTotalLength();
                            if (downloadFileListen != null) {
                                downloadFileListen.onLoading(loadedLengthTotal, totalProgress);
                            }
                        }
                    }
                }

                private long getTotalLength() {
                    if (totalLength[0] > 0) {
                        return totalLength[0];
                    } else {
                        totalLength[0] = 0;
                        for (long l : totalLengths) {
                            totalLength[0] += l;
                        }
                        return totalLength[0];
                    }
                }
            });
        }
    }

    synchronized void onDownloadTaskDoStart(DownloadTask downloadTask) {
        taskMyMap.put(downloadTask.getKey(), downloadTask);
    }

    synchronized void onDownloadTaskCancelOrFinishOrFail(DownloadTask downloadTask) {
        if (isCancelAlling) return;
        if (isStarting(downloadTask)) {
            taskMyMap.remove(downloadTask.getKey());
        }
    }

    private boolean isCancelAlling;

    public synchronized void cancelAll() {
        if (taskMyMap == null || taskMyMap.size() == 0) return;
        if (isCancelAlling) return;
        isCancelAlling = true;
        List<DownloadTask> valueList = taskMyMap.getValueList();
        Iterator<DownloadTask> iterator = valueList.iterator();
        while (iterator.hasNext()) {
            DownloadTask downloadTask = iterator.next();
            if (downloadTask != null) {
                downloadTask.cancel(true);
            }
        }
        taskMyMap.clear();
        isCancelAlling = false;
    }

    synchronized boolean isStarting(DownloadTask downloadTask) {
        if (downloadTask == null) return false;
        return taskMyMap.containsKey(downloadTask.getKey());
    }

    synchronized boolean isStarting(String url, String saveDir, String saveName) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName))
            return false;
        return taskMyMap.containsKey(getKey(url, saveDir, saveName));
    }

    synchronized boolean isStarting(String url, String resultFilePath) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(resultFilePath))
            return false;
        return taskMyMap.containsKey(getKey(url, resultFilePath));
    }

    synchronized boolean isStarting(String[] urls, String[] resultFilePaths) {
        if (urls == null || urls.length == 0)
            return false;
        return taskMyMap.containsKey(getKey(urls, resultFilePaths));
    }

    synchronized boolean isStarting(String key) {
        if (key == null || key.length() == 0)
            return false;
        return taskMyMap.containsKey(key);
    }

    public synchronized void startDownloadApkAndInstall(Context context, String url) {
        String fileName = url.substring(url.lastIndexOf("/"));
        startDownload(url, context.getCacheDir().getAbsolutePath(), fileName, new SimpleDownloadFileListen() {
            @Override
            public void onFinish(String filePath) {
                super.onFinish(filePath);
                if (InstallUtil.checkInstallPermission(context)) {
                    InstallUtil.installAPK(filePath, context, context.getPackageName() + ".fileProvider");
                } else {
                    if (context instanceof ComponentActivity) {
                        LifeUtil.addListener((ComponentActivity) context, new LifeUtil.OnSimpleLifeListener() {
                            @Override
                            public void onResume() {
                                super.onResume();
                                if (InstallUtil.isHasInstallPermission(context)) {
                                    InstallUtil.installAPK(filePath, context, context.getPackageName() + ".fileProvider");
                                }
                                LifeUtil.removeListener(((ComponentActivity) context).getLifecycle(), this);
                            }
                        });
                    }
                }
            }
        });
    }

    public synchronized void startDownloadApkAndInstall(Context context, String url, DownloadFileListen downloadFileListen) {
        String fileName = url.substring(url.lastIndexOf("/"));
        startDownload(url, context.getCacheDir().getAbsolutePath(), fileName, downloadFileListen);
    }

    //============================= 工具api不需要加同步锁 ========================================
    public String getKey(String url, String saveDir, String saveName) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(saveDir) || TextUtils.isEmpty(saveName))
            return "";
        return UIHelper.md5(url + new File(saveDir, saveName).getAbsolutePath());
    }

    public String getKey(String url, String resultFilePath) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(resultFilePath))
            return "";
        return UIHelper.md5(url + resultFilePath);
    }

    public String getKey(String[] urls, String[] resultFilePaths) {
        if (urls == null || urls.length == 0)
            return "";
        return UIHelper.md5(Arrays.toString(urls) + Arrays.toString(resultFilePaths));
    }

    public String getKey(List<String> urlList, List<String> resultFilePathList) {
        if (urlList == null || urlList.size() == 0)
            return "";
        return UIHelper.md5(urlList.toString() + resultFilePathList.toString());
    }
    //============================= 工具api不需要加同步锁 ========================================

}
