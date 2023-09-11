package com.android.liba.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.text.TextUtils;

import com.android.liba.context.AbstractApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
public class FileUtil {
    private static final String TAG = "FileUtil ";

    public static String readStringFromFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        return readStringFromFile(new File(filePath));
    }

    public static String readStringFromFile(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String mimeTypeLine;
            while ((mimeTypeLine = br.readLine()) != null) {
                sb.append(mimeTypeLine);
            }
            isr.close();
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean writeStringToFileIgnorePermission(File file, String text) {
        if (file == null) return false;
        if (file.getParentFile() == null) return false;
        return writeStringToFileIgnorePermission(file.getParentFile().getAbsolutePath(), file.getName(), text);
    }

    public static boolean writeStringToFileIgnorePermission(String fileDic, String fileName, String text) {
        File file = FileUtil.createFile(fileDic, fileName);
        if (file == null) {
            return false;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file, false);
            fos.write(text.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFile(String filePath) {
        return TextUtils.isEmpty(filePath) || deleteFile(new File(filePath));
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) return true;
        boolean isSuccess = true;
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                for (File f : fileList) {
                    if (!deleteFile(f)) {
                        isSuccess = false;
                    }
                }
            }
            boolean delete = file.delete();
            return delete && isSuccess;
        } else {
            return file.delete();
        }
    }

    // 获取指定文件夹内所有文件大小的和
    public static long getFileSize(File... files) {
        if (files == null || files.length == 0) return 0;
        long size = 0;
        for (File file : files) {
            if (file == null || !file.exists()) {
                size += 0;
            } else {
                if (file.isDirectory()) {
                    File[] fileList = file.listFiles();
                    for (File f : fileList) {
                        size = size + getFileSize(f);
                    }
                } else {
                    size += file.length();
                }
            }
        }
        return size;
    }

    public static File createFile(File file) {
        return createFile(file.getAbsolutePath());
    }

    public static File createFile(String filePath) {
        int lastIndex = filePath.lastIndexOf("/");
        String dir = filePath.substring(0, lastIndex);
        String name = filePath.substring(lastIndex);
        return createFile(dir, name);
    }

    public static File createFile(String dirPath, String fileName) {
        try {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                if (!dirFile.mkdirs()) {
                    UIHelper.showLog(TAG + "createFile dirFile.mkdirs fail");
                    return null;
                }
            } else if (!dirFile.isDirectory()) {
                boolean delete = dirFile.delete();
                if (delete) {
                    return createFile(dirPath, fileName);
                } else {
                    UIHelper.showLog(TAG + "createFile dirFile !isDirectory and delete fail");
                    return null;
                }
            }
            File file = new File(dirPath, fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    UIHelper.showLog(TAG + "createFile createNewFile fail");
                    return null;
                }
            }
            return file;
        } catch (Exception e) {
            UIHelper.showLog(TAG + "createFile fail :" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static String getInsideCacheDir(Context context, String dir, boolean isEndWithSeparator) {
        if (dir == null || dir.length() == 0) dir = "myCache";
        File cacheDir = context.getCacheDir();
        File myCacheDirFile = new File(cacheDir.getParentFile(), dir);
        String absolutePath = myCacheDirFile.getAbsolutePath();
        if (!isEndWithSeparator && absolutePath.endsWith("/")) {
            absolutePath = absolutePath.substring(0, absolutePath.length() - 1);
        } else if (isEndWithSeparator && !absolutePath.endsWith("/")) {
            absolutePath = absolutePath + "/";
        }
        return absolutePath;
    }

    public static void checkMediaFileUpdate(File file) {
        String absolutePath = file.getAbsolutePath();
        String s = absolutePath.toLowerCase();
        if (s.endsWith(".png") || s.endsWith(".jpg") || s.endsWith(".jpeg") || s.endsWith(".gif")
                || s.endsWith(".mp3") || s.endsWith(".mp4") || s.endsWith(".aac") || s.endsWith(".avi")) {
            FileUtil.updateMedia(AbstractApplication.application, absolutePath);
            FileUtil.updateMedia(AbstractApplication.application, file.getParent());
        }
    }

    /**
     * 刷新媒体库
     */
    public static void updateMedia(Context context, String path) {
        if (context == null) return;
        if (TextUtils.isEmpty(path)) return;
        context = context.getApplicationContext();

////        方法1:此方法兼容4.4以下及以上（推荐使用）
        try {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFile(File sourceFile, File resultFile) {
        try {
            boolean isSuccess = writeFile(resultFile, new FileInputStream(sourceFile), false);
            if (isSuccess) {
                checkMediaFileUpdate(resultFile);
            }
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            if (!file.exists()) {
                createFile(file.getParent(), file.getName());
            }
            o = new FileOutputStream(file, append);
            byte[] data = new byte[1024];
            int length;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (o != null) {
                    o.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 
