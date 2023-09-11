package com.android.liba.network.protocol;

import com.alibaba.fastjson.JSONObject;
import com.android.liba.network.ApiManager;
import com.android.liba.service.User;
import com.android.liba.service.UserService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RequestArgs {
    public HashMap<String, String> args = new HashMap<>();

    public RequestArgs() {
        User user = UserService.getInstance().getUserInfo();
        if (user != null)
            args.put("token", user.token);
    }

    public RequestArgs(String service, int version_code) {
        this(service);
        args.put("version_code", version_code + "");
    }

    public RequestArgs(String service, int version_code, String osType) {
        this(service);
        args.put("version_code", version_code + "");
        args.put("os_type", osType);
    }

    public RequestArgs(String service) {
        this();
        args.put("service", service);
    }

    public RequestArgs add(String key, String value) {
        args.put(key, value);
        return this;
    }

    public RequestArgs checkAdd(String key, String value) {
        if (!args.containsKey(key))
            args.put(key, value);
        return this;
    }

    public static RequestArgs get() {
        return new RequestArgs();
    }

    public static RequestArgs get(String service) {
        return new RequestArgs(service);
    }

    public static RequestArgs get(String service, int version_code) {
        return new RequestArgs(service, version_code);
    }

    public static RequestArgs getOsType(String service, int version_code) {
        return new RequestArgs(service, version_code, "android");
    }

    public static String map2json(Map<String, String> map) {
        return JSONObject.toJSONString(map);
    }

    public static Map<String, Object> json2map(String jsonstr) {
        return JSONObject.parseObject(jsonstr);
    }

    public String build() {
        Gson gson = new Gson();
        String s = gson.toJson(args);
        String encrypt = ApiManager.getSecret().encode(s);
        return encrypt;

    }

    public HashMap<String, String> buildMap() {
        return args;
    }
}
