package com.android.liba.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.liba.context.AppContext;
import com.android.liba.jk.OnSimpleListener;
import com.android.liba.util.jdownload.DownloadManager;
import com.android.liba.util.js.BaseJsInterface;

import java.util.HashMap;
import java.util.Map;


public class WebUtil {
    private String TAG = WebUtil.class.getSimpleName();
    private WebUtil WebUtil;
    private Activity mActivity;
    private WebView mWebView;
    private String mUrl;
    private ProgressBar mPbProgress;
    private String urlBack = "";
    private String path;
    private boolean isNeedBackRefresh;
    private OnSimpleListener onDownloadClickListener;

    public void init(Activity activity, WebView webView, String url, ProgressBar pbProgress,
                     boolean clickDownloadGoMain) {
        mActivity = activity;
        mWebView = webView;
        mUrl = url;
        mPbProgress = pbProgress;
    }

    public void init(Activity activity, WebView webView, String url, ProgressBar pbProgress) {
        mActivity = activity;
        mWebView = webView;
        mUrl = url;
        mPbProgress = pbProgress;
    }

    public void setOnDownloadClickListener(OnSimpleListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void doWebSet() {
        doWebSet(new BaseJsInterface(mActivity, (name, o) -> {
            if (name.equals("backWeb")) {
                backWeb();
            }
            return false;
        }));
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void doWebSet(BaseJsInterface cb) {
        WebSettings settings = mWebView.getSettings();
        settings.setUserAgentString(settings.getUserAgentString() + "/xrapp");//加个标识，让页面可以判断是否APP内打开
        UIHelper.showLog(TAG, "getUserAgentString = " + settings.getUserAgentString());
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setBlockNetworkImage(false);//解决图片不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (!TextUtils.isEmpty(mUrl)) {
            String realurl = mUrl.substring((mUrl.lastIndexOf("realurl") + 8), mUrl.length());
            String decode = Uri.decode(realurl);
            Map<String, String> extraHeaders = new HashMap<String, String>();
            extraHeaders.put("referer", decode);
            UIHelper.showLog(TAG, " mUrl :" + mUrl + " , realurl :" + realurl);
            mWebView.loadUrl(mUrl, extraHeaders);
        }
        mWebView.setWebChromeClient(new MyWebChomeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setDownloadListener(new MyDownloadListener());
        mWebView.addJavascriptInterface(cb, "JsInterface");
    }

    private class MyWebChomeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (mPbProgress != null) {
                    mPbProgress.setVisibility(View.GONE);
                }
            } else {
                if (mPbProgress != null) {
                    mPbProgress.setProgress(newProgress);
                    mPbProgress.setVisibility(View.VISIBLE);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            UIHelper.showLog("XXXBACK", "shouldOverrideUrlLoading url  == " + url);

            mUrl = url;
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            } else if (url.startsWith("tbopen://m.taobao.com")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url); // 商品地址
                intent.setData(uri);
//                if (AppUtil.isIntentExisting(mActivity, intent)) {
//                    mActivity.startActivity(intent);
//                }
            }
            try {
                isNeedBackRefresh = true;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mActivity instanceof WebLoadDoneListener) {
                ((WebLoadDoneListener) mActivity).onWebDone();
            }
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //                mWebView.loadUrl("javascript:" + "var element=document.getElementsByClassName(\"title\")[0];\n" +
//                    mWebView.loadUrl("javascript:" + "var element=document.getElementsByClassName(\"Ads_ads_2vcYaB\")[0];\n" +
//                            "element.setAttribute(\"hidden\",true);");
//                }
//            });
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    }

    private class MyDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            String fileName = url.substring(url.lastIndexOf("/"));
            path = mActivity.getCacheDir() + fileName;
            if (AppUtil.isNetworkAvalible(mActivity)) {
                AppContext.showToast("开始下载");
                DownloadManager.instance().startDownloadApkAndInstall(mActivity, url);
                if (onDownloadClickListener != null) {
                    onDownloadClickListener.onListen();
                }
            } else {
                AppContext.showToast("网络无法连接");
            }
        }
    }

    public interface WebLoadDoneListener {
        void onWebDone();
    }

    private void goFeiHuApp() {
//        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(PackageConstants.FEI_HU);
//        mActivity.startActivity(intent);
    }


    public void backWeb() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UIHelper.showLog("XXXBACK", "urlBack == " + urlBack);
                if (urlBack != null && !urlBack.isEmpty() && urlBack.equals(mUrl)) {
                    urlBack = "";
                    mActivity.finish();
                    mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                UIHelper.showLog("XXXBACK", "canGoBack == " + mWebView.canGoBack());
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    urlBack = mUrl;
                } else {
                    mActivity.finish();
                    mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    public void defBackWeb() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    urlBack = mUrl;
                } else {
                    mActivity.finish();
                    mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    public void defBackWeb(int test) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (test > 1) {
                    boolean b = !mWebView.canGoBackOrForward(-2) && mWebView.canGoBackOrForward(-1);
                    if (b) {
                        mActivity.finish();
                        mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }

                }

                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    urlBack = mUrl;
                } else {
                    mActivity.finish();
                    mActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void goAppMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public boolean getIsNeedBackRefresh() {
        return isNeedBackRefresh;
    }
}
