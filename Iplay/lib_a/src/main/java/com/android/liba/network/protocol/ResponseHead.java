package com.android.liba.network.protocol;

import com.android.liba.util.UIHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResponseHead {
    public int ret;
    public String msg;
    public String data;

    @Override
    public String toString() {
        return "ResponseHead{" +
                "data='" + data + '\'' +
                ", ret=" + ret +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static ResponseHead fromJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(String.class, new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonElement json, Type type, JsonDeserializationContext context)
                            throws JsonParseException {
                        if (json.isJsonObject()) {
                            JsonObject object = json.getAsJsonObject();
                            return object.toString();
                        }
                        return json.getAsString();
                    }
                }).create();
        ResponseHead head = null;
        try {
            head = gson.fromJson(json, ResponseHead.class);
        } catch (Exception ex) {
            UIHelper.showLog("ResponseHead", "fromJson faild: " + json + "-" + ex);
        }
        return head;
    }

}
