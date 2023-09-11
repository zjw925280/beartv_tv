package com.gys.play.util

import com.google.gson.Gson
import java.lang.reflect.Type

object GsonUtils {
    private val gson: Gson = Gson()

    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun <T> fromJson(json: String, typeOfT: Type): T? {
        return gson.fromJson<T>(json, typeOfT)
    }

    fun toJson(src: Any): String {
        return gson.toJson(src)
    }

}
