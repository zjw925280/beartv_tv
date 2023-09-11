package com.android.liba.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtil {
    private Gson mGson;
    private static GsonUtil gsonUtil;

    private GsonUtil() {
    }

    public void release() {
        mGson = null;
        gsonUtil = null;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .setLenient()
                    .create();
        }
        return mGson;
    }

    public static GsonUtil instance() {
        if (gsonUtil == null) {
            gsonUtil = new GsonUtil();
        }
        return gsonUtil;
    }

    public <T> T fromJson(final String json, final Class<T> classOfT) {
        if (json == null || json.length() == 0 || classOfT == null) {
            return null;
        }
        return getGson().fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, Type type) {
        if (json == null || json.length() == 0 || type == null) {
            return null;
        }
        return getGson().fromJson(json, type);
    }

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return getGson().toJson(object);
    }


}
