package com.android.liba.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharePHelper {

    private final String fileName;
    private static SharePHelper sharePHelper;
    private final String dirPath;

    protected SharePHelper(Context context) {
        this(context, "jiujie_key");
    }

    protected SharePHelper(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName))
            fileName = "jiujie_key";
        this.fileName = fileName;

        //SharedPreferences 对多进程并不友好，只是在 getSharedPreferences 时读取全部数据，如进程AB都已初始化，进程A此时改变key1的值，进程B读取到的却还是旧的值
        //改成使用文件的形式，每一个key对应一个文件
//        dirPath = context.getCacheDir() + "/file_sp/" + getFileName();//Old
        dirPath = context.getCacheDir().getParentFile() + "/file_sp/" + getFileName();
//        dirPath = context.getCacheDir().getParentFile() + "/shared_prefs/"+getFileName();//原本xml SharedPreferences存储路径
    }

    public static SharePHelper instance(Context context) {
        if (sharePHelper == null) {
            sharePHelper = new SharePHelper(context);
        }
        return sharePHelper;
    }

    public long getCacheSize() {
        return FileUtil.getFileSize(new File(getDataCachePath()));
    }

    public String getDataCachePath() {
        return dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public static SharePHelper instance(Context context, String fileName) {
        if (sharePHelper == null || !fileName.equals(sharePHelper.getFileName())) {
            sharePHelper = new SharePHelper(context, fileName);
        }
        return sharePHelper;
    }

    public void saveObject(String key, Object object) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (object == null) {
            remove(key);
            return;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(object);
            String saveStr = new String(Base64.encode(os.toByteArray(), Base64.DEFAULT));

            //不需要权限
            FileUtil.writeStringToFileIgnorePermission(getKeySaveFile(key), saveStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveValueAfterBase64(String key, String saveStr) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (saveStr == null) {
            remove(key);
            return;
        }
        try {
            //不需要权限
            FileUtil.writeStringToFileIgnorePermission(getKeySaveFile(key), saveStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T readObject(String key, T defaultValue) {
        T value = readObject(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public <T> T readObject(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        File saveFile = getKeySaveFile(key);
        try {
            String string = FileUtil.readStringFromFile(saveFile);
            if (string == null) return null;
            byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream is = new ByteArrayInputStream(base64);
            T object1;
            ObjectInputStream bis = new ObjectInputStream(is);
            Object object = bis.readObject();
            object1 = (T) object;
            return object1;
        } catch (Exception e) {
            UIHelper.showLog("Exception " + getClass().getName() + " readObject:" + e.getMessage());
            e.printStackTrace();
            remove(key);
            return null;
        }
    }

    public void clear() {
        File saveFile = new File(dirPath);
        FileUtil.deleteFile(saveFile);
    }

    public boolean isContainsKey(String key) {
        File saveFile = getKeySaveFile(key);
        return saveFile.exists() && saveFile.isFile() && saveFile.length() > 0;
    }

    private File getKeySaveFile(String key) {
        String encode = MD5.encode(key);
        if (encode == null) encode = key;
        return new File(dirPath, encode);
    }

    public void remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        File saveFile = getKeySaveFile(key);
        FileUtil.deleteFile(saveFile);
    }
}
