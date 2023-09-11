package com.gys.play.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import com.android.liba.context.AppContext;
import com.gys.play.R;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.List;

public class OpenFileWebChromeClient extends WebChromeClient {
    public static final int REQUEST_FILE_PICKER = 1;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbacks;
    Activity mContext;
    public OpenFileWebChromeClient(Activity mContext){
        super();
        this.mContext = mContext;
    }
    // Android < 3.0 调用这个方法
    public void openFileChooser(ValueCallback<Uri> filePathCallback) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_FILE_PICKER);
    }

    // 3.0 + 调用这个方法
    public void openFileChooser(ValueCallback filePathCallback,
                                String acceptType) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_FILE_PICKER);
    }
    //  / js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
    // Android > 4.1.1 调用这个方法
    public void openFileChooser(ValueCallback<Uri> filePathCallback,
                                String acceptType, String capture) {
        mFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                REQUEST_FILE_PICKER);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionX.init((FragmentActivity) webView.getContext()).permissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            if(allGranted){
                                mFilePathCallbacks = filePathCallback;
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("*/*");
                                mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                                        REQUEST_FILE_PICKER);
                            }else {
                                AppContext.showToast(webView.getContext().getString(R.string.permission_sd_no));
                            }
                        }
                    });
        }else{
            mFilePathCallbacks = filePathCallback;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"),
                    REQUEST_FILE_PICKER);
        }
        return true;
    }

}
