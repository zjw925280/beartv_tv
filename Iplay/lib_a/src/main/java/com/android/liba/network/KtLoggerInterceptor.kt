package com.android.liba.network

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.android.liba.network.log.LogUtil
import com.android.liba.util.UnicodeUtils
import com.android.liba.util.log.LoggerUtil
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

class KtLoggerInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val body = response.body!!
        val content = body.string()
        response.close()

        //打印
        LogUtil.log(response, content)

        val newResponseBody: ResponseBody =
            ResponseBody.create(body.contentType(), content)

        val message = response.newBuilder().body(newResponseBody).build()

        return message
    }

    /**
     * 接口数据解密解码成可读的
     */
    private fun decode(content: String): String {
        return try {
            val parseObject = JSON.parseObject(content)
            val string: String? = parseObject.getString("data")
            if (!string.isNullOrEmpty()) {
                return UnicodeUtils.decode(ApiManager.getSecret().decode(string))
            }
            return "${parseObject.getString("msg")}"
        } catch (e: Exception) {
            e.printStackTrace()
            content
        }
    }

    fun getDebugParamStr(): String? {
        return null
    }

    /**
     * @param isEncrypt 是否加密
     */
    private fun logForResponse(
        response: Response,
        mediaType: MediaType?,
        content: String,
        isEncrypt: Boolean
    ) {
//        MediaType mediaType = null;
//        String resp = null;
        try {
            val builder = response.newBuilder()
            val clone = builder.build()
            val url = clone.request.url.toString()
            val logList: MutableList<String> = ArrayList()
            logList.add("┌─────────────────Http Log Start───────────────────")
            val debugParamStr = getDebugParamStr()
            if (url.contains("?") && debugParamStr != null) {
                logList.add("│    debug url : $url&$debugParamStr")
            }
            logList.add("│    url : $url")
            val headers = clone.request.headers
            val sb = StringBuilder()
            for (key in headers.names()) {
                if (sb.isNotEmpty()) {
                    sb.append("&")
                }
                if (isEncrypt) {
                    sb.append(key).append("=").append(ApiManager.getSecret().decode(headers[key]))
                } else {
                    sb.append(key).append("=").append(headers[key])
                }
            }
            if (sb.isNotEmpty()) {
                logList.add("│    urlWithParam : $url?$sb")
            }
            logList.add("│    method : " + clone.request.method)
            logList.add("│    code : " + clone.code)
            logList.add("│    protocol : " + clone.protocol)
            if (!TextUtils.isEmpty(clone.message)) logList.add("│    message : " + clone.message)
            if (mediaType != null) {
                logList.add("│    responseBody's contentType : $mediaType")
                if (isText(mediaType)) {
                    if (!TextUtils.isEmpty(content)) {
                        if (content.length > 2048) {
                            var text = content
                            while (text.length > 2048) {
                                val show = text.substring(0, 2048)
                                text = text.substring(2048)
                                logList.add("│    responseBody's content : $show")
                            }
                            logList.add("│    responseBody's content : $text")
                        } else {
                            logList.add("│    responseBody's content : $content")
                        }
                    } else {
                        logList.add("│    responseBody's content : $content")
                    }
                    //                        return response.newBuilder().body(ResponseBody.create(mediaType, resp)).build();
                } else {
                    logList.add("│    responseBody's content : " + " maybe [file part] , too large too print , ignored!")
                }
            }
            //            ResponseBody body = clone.body();
//            if (body != null) {
//            }
            logList.add("└─────────────────Http Log end  ───────────────────")
            LoggerUtil.instance().addLog("httpLog", logList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isText(mediaType: MediaType): Boolean {
        if (mediaType.type == "text") {
            return true
        }
        val subtype = mediaType.subtype
        if (subtype == "json" || subtype == "xml" || subtype == "html" || subtype == "javascript" || subtype == "x-javascript" || subtype == "webviewhtml") return true
        return false
    }
}