package com.gys.play.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;

import java.io.File;

public class InstallUtil {
    //context.getPackageName() + ".fileprovider"
    public static void installAPK(String path, Context context, String authority) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (path == null || path.isEmpty())
            return;
        File apkFile = new File(path);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0启动姿势<pre name="code" class="html">    //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同；apkFile为问题1中的外部存储apk文件</pre>
            uri = FileProvider.getUriForFile(context, authority, apkFile);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，次过程没有用户交互
        } else {//7.0以下启动姿势
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static boolean checkInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.O) {
            if (context.getPackageManager().canRequestPackageInstalls()) {
                return true;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                //          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果加了这句，onActivityResult的回调就会在startActivityForResult时调用，而非返回了再回调
                context.startActivity(intent);
                return false;
            }
        }
        return true;
    }

    public static boolean isHasInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }
}
