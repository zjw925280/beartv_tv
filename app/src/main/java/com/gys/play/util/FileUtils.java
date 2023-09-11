package com.gys.play.util;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.android.liba.util.UIHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    private static final String TAG = "FileUtils";

    public static void add(Context context) {
        // Add a specific media item.
        ContentResolver resolver = context.getApplicationContext()
                .getContentResolver();

// Find all audio files on the primary external storage device.
        Uri audioCollection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioCollection = MediaStore.Audio.Media
                    .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            audioCollection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

// Publish a new song.
        ContentValues newSongDetails = new ContentValues();
        newSongDetails.put(MediaStore.Audio.Media.DISPLAY_NAME,
                "My Song.mp3");

// Keeps a handle to the new song's URI in case we need to modify it
// later.
        Uri myFavoriteSongUri = resolver
                .insert(audioCollection, newSongDetails);
    }

    public static boolean saveImageToGallery(Bitmap bmp, File file, Context context) {
        //生成路径
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String dirName = "erweima16";
//        File appDir = new File(root, dirName);
//        if (!appDir.exists()) {
//            appDir.mkdirs();
//        }

//        //文件名为时间
//        long timeStamp = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sd = sdf.format(new Date(timeStamp));
//        String fileName = sd + ".jpg";

        //获取文件
//        File file = new File(appDir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 调用系统分享功能
     */
    public static void share(File file, Context context) {
        Uri uri = getUri(file, context);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        String type = context.getContentResolver().getType(uri);
        intent.setType(type);
        context.startActivity(intent);
    }

    public static Uri getUri(File file, Context context) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 复制文件夹
     *
     * @param resource 源路径
     * @param target   目标路径
     */
    public static void copyFolder(String resource, String target) throws Exception {
        UIHelper.showLog(TAG, "copyFolder: " + resource + " " + target);
        File resourceFile = new File(resource);
        if (!resourceFile.exists()) {
            throw new Exception("源目标路径：[" + resource + "] 不存在...");
        }
        File targetFile = new File(target);
        boolean mkdirs;
        if (!targetFile.exists()) {
            mkdirs = targetFile.mkdirs();

        }
        if (!targetFile.exists()) {
            throw new Exception("存放的目标路径：[" + target + "] 不存在...");
        }

        // 获取源文件夹下的文件夹或文件
        File[] resourceFiles = resourceFile.listFiles();
        UIHelper.showLog(TAG, "resourceFile: list " + resourceFile.list());
        UIHelper.showLog(TAG, "resourceFile: resourceFiles " + resourceFiles);
        if (resourceFiles == null) {
            return;
        }
        for (File file : resourceFiles) {

            File file1 = new File(targetFile.getAbsolutePath() + File.separator + resourceFile.getName());
            // 复制文件
            if (file.isFile()) {
                System.out.println("文件" + file.getName());
                // 在 目标文件夹（B） 中 新建 源文件夹（A），然后将文件复制到 A 中
                // 这样 在 B 中 就存在 A
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                File targetFile1 = new File(file1.getAbsolutePath() + File.separator + file.getName());
                copyFile(file, targetFile1);
            }
            // 复制文件夹
            if (file.isDirectory()) {// 复制源文件夹
                String dir1 = file.getAbsolutePath();
                // 目的文件夹
                String dir2 = file1.getAbsolutePath();
                copyFolder(dir1, dir2);
            }
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        UIHelper.showLog(TAG, "copyFolder: " + sourceFile.getPath() + " " + targetFile.getPath());
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        FileInputStream input = null;
        BufferedInputStream inBuff = null;
        FileOutputStream output = null;
        BufferedOutputStream outBuff = null;
        try {
            input = new FileInputStream(sourceFile);

            inBuff = new BufferedInputStream(input);
            // 新建文件输出流并对它进行缓冲
            output = new FileOutputStream(targetFile);
            outBuff = new BufferedOutputStream(output);
            // 缓冲数组
            byte[] b = new byte[1024 * 1024];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            // 刷新此缓冲的输出流
            outBuff.flush();
            //关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String dataColumn = getDataColumn(context, contentUri, null, null);
        if (TextUtils.isEmpty(dataColumn)) {
            File file = uriToFileApiQ(context, contentUri);
            if (file != null) {
                return file.getPath();
            }
        }
        return dataColumn;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public static File uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把文件复制到沙盒目录
            ContentResolver contentResolver = context.getContentResolver();
//            Cursor cursor = contentResolver.query(uri, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + ".jpg");
//                    File cache = new File(context.getCacheDir().getPath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            }
        }
        return file;
    }

    public static void copy(InputStream input, FileOutputStream downloadFile) {
        int index;
        byte[] bytes = new byte[1024];
        try {
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            input.close();
            downloadFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logFolder(File file) {
        if (!file.exists()) {
            UIHelper.showLog(TAG, "logFolder file is null : " + " ptah " + file.getPath());
            return;
        }
        UIHelper.showLog(TAG, "logFolder file size : " + file.length() + " ptah " + file.getPath());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                logFolder(f);
            }
        }
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        String string = bigInt.toString(16);
        UIHelper.showLog(TAG, "getFileMD5: " + file.getName() + "  " + string);
        return string;
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param file
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        UIHelper.showLog(TAG, "ZipFolder: start ");
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        UIHelper.showLog(TAG, "---- " + file.getParent() + "===" + file.getAbsolutePath());
        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
        UIHelper.showLog(TAG, "ZipFolder: end ");
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        UIHelper.showLog(TAG, "folderString:" + folderString + "\n" +
                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString + "/" + fileList[i], zipOutputSteam);
            }
        }
    }

}
