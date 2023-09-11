package com.android.liba.network.log;

import androidx.annotation.NonNull;

import com.android.liba.BuildConfig;
import com.android.liba.network.ApiManager;
import com.android.liba.util.UnicodeUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import kotlin.text.Charsets;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;


public class LogUtil {

    private static final String TAG = "HTTP_X";

    private static boolean isDebug = BuildConfig.DEBUG;


    //打印Http返回的正常结果
    public static void log(@NonNull Response response, String body) {
        if (!isDebug) return;
        try {
            Request request = response.request();
            String result = "";
            if (body != null) {
                result = body;
            }
            LogTime logTime = request.tag(LogTime.class);
            long tookMs = logTime != null ? logTime.tookMs() : 0;
            StringBuilder builder = new StringBuilder("<------ ")
                    .append(" request end ------>\n")
                    .append(request.method()).append(" ").append(request.url())
                    .append("\n\n").append(response.protocol()).append(" ")
                    .append(response.code()).append(" ").append(response.message())
                    .append(tookMs > 0 ? " " + tookMs + "ms" : "")
                    .append("\n").append(response.headers())
                    .append("\n").append(UnicodeUtils.INSTANCE.decode(result));
            Platform.get().loge(TAG, builder.toString());
        } catch (Throwable e) {
            Platform.get().logd(TAG, "Request end Log printing failed", e);
        }
    }


    //请求前，打印日志
    public static void logQuest(Request userRequest) {
        if (!isDebug) return;
        try {
            Request.Builder requestBuilder = userRequest.newBuilder();
            StringBuilder builder = new StringBuilder("<------ ")
                    .append(" request start ------>\n")
                    .append(userRequest.method())
                    .append(" ").append(userRequest.url());
            RequestBody body = userRequest.body();
            if (body != null) {
                MediaType contentType = body.contentType();
                if (contentType != null) {
                    requestBuilder.header("Content-Type", contentType.toString());
                }
                long contentLength = body.contentLength();
                if (contentLength != -1L) {
                    requestBuilder.header("Content-Length", String.valueOf(contentLength));
                    requestBuilder.removeHeader("Transfer-Encoding");
                } else {
                    requestBuilder.header("Transfer-Encoding", "chunked");
                    requestBuilder.removeHeader("Content-Length");
                }
            }

            if (userRequest.header("Host") == null) {
                requestBuilder.header("Host", hostHeader(userRequest.url()));
            }

            if (userRequest.header("Connection") == null) {
                requestBuilder.header("Connection", "Keep-Alive");
            }

            if (userRequest.header("Accept-Encoding") == null
                    && userRequest.header("Range") == null) {
                requestBuilder.header("Accept-Encoding", "gzip");
            }
            Headers headers = requestBuilder.build().headers();

            for (String name : headers.names()) {
                if (name.equals("data")) {
                    String value = headers.get(name);
                    builder.append("\n").append(name).append(" = ").append(ApiManager.getSecret().decode(value));
                }
            }
            builder.append("\n").append(headers);
            if (body != null) {
                builder.append("\n");
                if (bodyHasUnknownEncoding(userRequest.headers())) {
                    builder.append("(binary ")
                            .append(body.contentLength())
                            .append("-byte encoded body omitted)");
                } else {
                    builder.append(requestBody2Str(body));
                }
            }
            Platform.get().loge(TAG, builder.toString());
        } catch (Throwable e) {
            Platform.get().logd(TAG, "Request start log printing failed", e);
        }
    }

    private static String hostHeader(HttpUrl url) {
        String host = url.host().contains(":")
                ? "[" + url.host() + "]"
                : url.host();
        return host + ":" + url.port();
    }

    private static boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null
                && !contentEncoding.equalsIgnoreCase("identity")
                && !contentEncoding.equalsIgnoreCase("gzip");
    }

    private static String requestBody2Str(@NonNull RequestBody body) throws IOException {

        if (body instanceof MultipartBody) {
            return multipartBody2Str((MultipartBody) body);
        }
        long contentLength = -1;
        try {
            contentLength = body.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (versionGte3140() && body.isDuplex()) {
            return "(binary " + contentLength + "-byte duplex body omitted)";
        } else if (versionGte3140() && body.isOneShot()) {
            return "(binary " + contentLength + "-byte one-shot body omitted)";
        } else {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            if (!isProbablyUtf8(buffer)) {
                return "(binary " + body.contentLength() + "-byte body omitted)";
            } else {
                return buffer.readString(getCharset(body));
            }
        }
    }

    private static boolean isProbablyUtf8(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }


    private static String multipartBody2Str(MultipartBody multipartBody) {
        final byte[] colonSpace = {':', ' '};
        final byte[] CRLF = {'\r', '\n'};
        final byte[] dashDash = {'-', '-'};
        Buffer sink = new Buffer();
        for (MultipartBody.Part part : multipartBody.parts()) {
            Headers headers = part.headers();
            RequestBody body = part.body();
            sink.write(dashDash)
                    .writeUtf8(multipartBody.boundary())
                    .write(CRLF);
            if (headers != null) {
                for (int i = 0, size = headers.size(); i < size; i++) {
                    sink.writeUtf8(headers.name(i))
                            .write(colonSpace)
                            .writeUtf8(headers.value(i))
                            .write(CRLF);
                }
            }
            MediaType contentType = body.contentType();
            if (contentType != null) {
                sink.writeUtf8("Content-Type: ")
                        .writeUtf8(contentType.toString())
                        .write(CRLF);
            }
            long contentLength = -1;
            try {
                contentLength = body.contentLength();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sink.writeUtf8("Content-Length: ")
                    .writeDecimalLong(contentLength)
                    .write(CRLF);

            if (body instanceof MultipartBody) {
                sink.write(CRLF)
                        .writeUtf8(multipartBody2Str((MultipartBody) body));
            } else if (versionGte3140() && body.isDuplex()) {
                sink.writeUtf8("(binary " + contentLength + "-byte duplex body omitted)");
            } else if (versionGte3140() && body.isOneShot()) {
                sink.writeUtf8("(binary " + contentLength + "-byte one-shot body omitted)");
            } else if (contentLength > 1024) {
                sink.writeUtf8("(binary " + contentLength + "-byte body omitted)");
            } else {
                try {
                    body.writeTo(sink);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (contentLength > 0) sink.write(CRLF);
            sink.write(CRLF);
        }
        sink.write(dashDash)
                .writeUtf8(multipartBody.boundary())
                .write(dashDash);
        return sink.readString(getCharset(multipartBody));
    }


    private static Charset getCharset(RequestBody requestBody) {
        MediaType mediaType = requestBody.contentType();
        return mediaType != null ? mediaType.charset(Charsets.UTF_8) : Charsets.UTF_8;
    }


    private static boolean versionGte3140() {
        return true;
    }

}
