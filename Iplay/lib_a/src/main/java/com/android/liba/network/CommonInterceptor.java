package com.android.liba.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request newRequest;
        if (chain.request().method().equals("POST")) {
            newRequest = addPostBaseParams(chain.request());
        } else {
            newRequest = addGetBaseParams(chain.request());
        }
        return chain.proceed(newRequest);
    }

    /**
     * 添加GET方法基础参数
     *
     * @param request
     * @return
     */
    private Request addGetBaseParams(Request request) {
//        String currentTime = String.valueOf(System.currentTimeMillis() / 1000 + SharedPreferManager.getInstance().getLong("timestamp-d", 0));

        HttpUrl httpUrl = request.url()
                .newBuilder()
//                .addQueryParameter("sign", Md5Utils.stringToMD5(currentTime+"nmysky"))
//                .addQueryParameter("timestamp", currentTime)
//                .addQueryParameter("ext", App.CHANNEL_ID)
//                .addQueryParameter("versioncode",App.VERSION_CODE+"" )
                .build();

        //添加签名
        Set<String> nameSet = httpUrl.queryParameterNames();
        ArrayList<String> nameList = new ArrayList<>();
        nameList.addAll(nameSet);
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            paramMap.put(nameList.get(i), httpUrl.queryParameterValue(i));
        }

        request = request.newBuilder().url(httpUrl).build();

        return request;
    }

    /**
     * 添加POST方法基础参数
     *
     * @param request
     * @return
     */
    private Request addPostBaseParams(Request request) {
        FormBody.Builder builder = new FormBody.Builder();
//        String currentTime = String.valueOf(System.currentTimeMillis() / 1000 + SharedPreferManager.getInstance().getLong("timestamp-d", 0));
//        builder.add("timestamp", currentTime)
//                .add("sign", Md5Utils.stringToMD5(currentTime + "nmysky"))
//                .add("ext", App.CHANNEL_ID)
//                .add("versioncode", App.VERSION_CODE + "");
//        FormBody formBody = builder.build();
//        request = request.newBuilder().post(formBody).build();
        return request;
    }
}
