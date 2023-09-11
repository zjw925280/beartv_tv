package com.android.liba.network;

import com.android.liba.BuildConfig;
import com.android.liba.exception.Error;
import com.android.liba.exception.Fail;
import com.android.liba.network.protocol.ResponseHead;
import com.android.liba.util.UIHelper;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FilterInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //请求
        Request request = chain.request();
        String encype = request.header("data");
        boolean isOurUrl = false;
        HttpUrl url = request.url();
        String host = url.host();
        if (host != null) {
            if (ApiManager.getApiManager().getBaseUrl().contains(host))
                isOurUrl = true;
        }
//        UIHelper.showLog("KOi","host == " + request.url().host());

        /*     RequestBody oldRequestBody = request.body();

        if(oldRequestBody.contentLength()!=0) {
            Buffer requestBuffer = new Buffer();
            oldRequestBody.writeTo(requestBuffer);
            String oldBodyStr = requestBuffer.readUtf8();
            requestBuffer.close();
            String encrypt = AESUtil.encrypt(oldBodyStr);
            //生成随机AES密钥并用serverPublicKey进行RSA加密
            RequestBody newBody = RequestBody.create(oldRequestBody.contentType(), "");
            //构造新的request
            request = request.newBuilder()
                    .header("Content-Type", oldRequestBody.contentType().toString())
                    .header("Content-Length", String.valueOf(newBody.contentLength()))
                    .method(request.method(), newBody)
                    .header("data", encrypt)
                    .build();
        }*/
        //响应
        Response response = chain.proceed(request);
        if (response.code() == 200) {
            ResponseBody oldResponseBody = response.body();
            String oldResponseBodyStr = oldResponseBody.string();
            String decrypt = oldResponseBodyStr;
            if (isOurUrl) {
                ResponseHead res = ResponseHead.fromJson(oldResponseBodyStr);
                if (res == null) {
                    Fail fail = new Fail("server return format error :" + oldResponseBodyStr);
                    fail.printStackTrace();
                    if (BuildConfig.DEBUG) {
                        UIHelper.showLog("Exception", "url :" + url);
                        UIHelper.showLog("Exception", "server return format error :" + oldResponseBodyStr);
                    }
                    res = new ResponseHead();
                    res.ret = -1;
                    res.msg = "服务器异常，请稍候再试";
                    res.data = "";
                    throw new Error(res);
                }
                if (res.ret != 200) {
                    throw new Error(res);
                }
                oldResponseBody.close();
                decrypt = res.data;
                if (encype != null && !encype.isEmpty()) {
                    decrypt = ApiManager.getSecret().decode(res.data);
                }
            }
            ResponseBody newResponseBody = ResponseBody.create(oldResponseBody.contentType(), decrypt);
            Response message = response.newBuilder().body(newResponseBody).build();
            response.close();
            return message;
        } else {
            response.close();
            UIHelper.showLog(this, "code = " + response.code() + ",message = " + response.message());
            throw new Error(response.code(), response.message());
        }
    }
}