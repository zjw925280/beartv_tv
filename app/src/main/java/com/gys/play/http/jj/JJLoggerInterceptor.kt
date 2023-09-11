package com.gys.play.http.jj

import android.text.TextUtils
import com.android.liba.util.GsonUtil
import com.android.liba.util.log.LoggerUtil
import com.gys.play.BuildConfig
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

open class JJLoggerInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val body = response.body!!
        val mediaType = body.contentType()
        var content = body.string()
        body.close()

        //自身项目都有加密
        val isOurHttp = HttpMethods.getBaseUrl().contains(request.url.host)
        if (isOurHttp) {
            //解密
            content = decode(content)
            //打印
            logForResponse(response, mediaType, content, isOurHttp)
        } else {
            //打印
            logForResponse(response, mediaType, content, isOurHttp)
        }

        return response.newBuilder()
            .body(content.toResponseBody(mediaType))
            .build()
    }

    /**
     * 接口数据解密解码成可读的
     */
    private fun decode(content: String): String {
        return try {
            val baseData = GsonUtil.instance().fromJson(content, BaseData::class.java)
            baseData.data = AESEncrypt.decodeReal(baseData.data)
            return GsonUtil.instance().toJson(baseData)
//            json = json.replace("\\\\/", "/")
//            json = json.replace("\\\\u", "\\u")
//            json = json.replace("\\\"", "\"")// data 是字符串，里面带的json 格式，不能简单把引号处理
//            UnicodeUtils.decode(json)
        } catch (e: Exception) {
            e.printStackTrace()
            content
        }
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
        if (!BuildConfig.DEBUG) return
        try {
            val builder = response.newBuilder()
            val clone = builder.build()
            val url = clone.request.url.toString()
            val logList: MutableList<String> = ArrayList()
            logList.add("┌─────────────────Http Log Start───────────────────")
            logList.add("│    url : $url")
            val body = clone.request.body
            if (body != null) {
                if (body is FormBody) {
                    val size = body.size
                    if (size > 0) {
                        val sb = StringBuilder()
                        for (i in 0 until size) {
                            if (sb.isNotEmpty()) sb.append(",")
                            sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i))
                        }
                        logList.add("│    body : $sb")
                    }
                }
            }
            val tag = clone.request.tag()
            if (tag != null) {
                logList.add("│    tag : $tag")
            }
            val headers = clone.request.headers
            val sb = StringBuilder()
            for (key in headers.names()) {
                if (sb.isNotEmpty()) {
                    sb.append("&")
                }
                if (isEncrypt) {
                    sb.append(key).append("=").append(AESEncrypt.decodeReal(headers[key]))
                } else {
                    sb.append(key).append("=").append(headers[key])
                }
            }
            if (sb.isNotEmpty()) {
                logList.add("│    headers : $sb")
            }
            logList.add("│    method : " + clone.request.method)
            logList.add("│    code : " + clone.code)
            logList.add("│    protocol : " + clone.protocol)
//            logList.add("│    headers : " + clone.headers)
//            logList.add("│    trailers : " + clone.trailers())
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