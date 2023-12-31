//package com.android.baselib.network;
//
//import java.io.EOFException;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.charset.UnsupportedCharsetException;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Connection;
//import okhttp3.Headers;
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.Protocol;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import okhttp3.internal.http.HttpHeaders;
//import okhttp3.internal.platform.Platform;
//import okio.Buffer;
//import okio.BufferedSource;
//
//import static okhttp3.internal.platform.Platform.INFO;
//
//public final class HttpLoggingInterceptor implements Interceptor {
//  private static final Charset UTF8 = Charset.forName("UTF-8");
//
//  public enum Level {
//
//    NONE,
//    BASIC,
//    HEADERS,
//    BODY
//  }
//
//  public interface Logger {
//    void log(String message);
//
//
//    Logger DEFAULT = new Logger() {
//      @Override
//      public void log(String message) {
//        Platform.get().log(INFO, message, null);
////          Timber.d(message);
//      }
//    };
//  }
//
//  public HttpLoggingInterceptor() {
//    this(Logger.DEFAULT);
//  }
//
//  public HttpLoggingInterceptor(Logger logger) {
//    this.logger = logger;
//  }
//
//  private final Logger logger;
//
//  private volatile Level level = Level.NONE;
//
//
//  public HttpLoggingInterceptor setLevel(Level level) {
//    if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
//    this.level = level;
//    return this;
//  }
//
//  public Level getLevel() {
//    return level;
//  }
//
//  @Override
//  public Response intercept(Chain chain) throws IOException {
//    Level level = this.level;
//
//    Request request = chain.request();
////    if (level == Level.NONE || (request.method().equals("GET") && !Constant.DEBUG_LOG)) {
//    if (level == Level.NONE || request.method().equals("GET")) {
//      return chain.proceed(request);
//    }
//
//    boolean logBody = level == Level.BODY;
//    boolean logHeaders = logBody || level == Level.HEADERS;
//
//    RequestBody requestBody = request.body();
//    boolean hasRequestBody = requestBody != null;
//
//    Connection connection = chain.connection();
//    Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
//    String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
//    if (!logHeaders && hasRequestBody) {
//      requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
//    }
//    logger.log(requestStartMessage);
//
//    if (logHeaders) {
//      if (hasRequestBody) {
//        // Request body headers are only present when installed as a network interceptor. Force
//        // them to be included (when available) so there values are known.
//        if (requestBody.contentType() != null) {
//          logger.log("Content-Type: " + requestBody.contentType());
//        }
//        if (requestBody.contentLength() != -1) {
//          logger.log("Content-Length: " + requestBody.contentLength());
//        }
//      }
//
//      Headers headers = request.headers();
//      for (int i = 0, count = headers.size(); i < count; i++) {
//        String name = headers.name(i);
//        // Skip headers from the request body as they are explicitly logged above.
//        if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
//          logger.log(name + ": " + headers.value(i));
//        }
//      }
//
//      if (!logBody || !hasRequestBody) {
//        logger.log("--> END " + request.method());
//      } else if (bodyEncoded(request.headers())) {
//        logger.log("--> END " + request.method() + " (encoded body omitted)");
//      } else {
//        Buffer buffer = new Buffer();
//        requestBody.writeTo(buffer);
//
//        Charset charset = UTF8;
//        MediaType contentType = requestBody.contentType();
//        if (contentType != null) {
//          charset = contentType.charset(UTF8);
//        }
//
//        logger.log("");
//        if (isPlaintext(buffer)) {
//          logger.log(buffer.readString(charset));
//          logger.log("--> END " + request.method()
//                  + " (" + requestBody.contentLength() + "-byte body)");
//        } else {
//          logger.log("--> END " + request.method() + " (binary "
//                  + requestBody.contentLength() + "-byte body omitted)");
//        }
//      }
//    }
//
//    long startNs = System.nanoTime();
//    Response response;
//    try {
//      response = chain.proceed(request);
//    } catch (Exception e) {
//      logger.log("<-- HTTP FAILED: " + e);
//      throw e;
//    }
//    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
//
//    ResponseBody responseBody = response.body();
//    long contentLength = responseBody.contentLength();
//    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
//    logger.log("<-- " + response.code() + ' ' + response.message() + ' '
//            + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
//            + bodySize + " body" : "") + ')');
//
//    if (logHeaders) {
//      Headers headers = response.headers();
//      for (int i = 0, count = headers.size(); i < count; i++) {
//        logger.log(headers.name(i) + ": " + headers.value(i));
//      }
//
//      if (!logBody || !HttpHeaders.hasBody(response)) {
//        logger.log("<-- END HTTP");
//      } else if (bodyEncoded(response.headers())) {
//        logger.log("<-- END HTTP (encoded body omitted)");
//      } else {
//        BufferedSource source = responseBody.source();
//        source.request(Long.MAX_VALUE); // Buffer the entire body.
//        Buffer buffer = source.buffer();
//
//        Charset charset = UTF8;
//        MediaType contentType = responseBody.contentType();
//        if (contentType != null) {
//          try {
//            charset = contentType.charset(UTF8);
//          } catch (UnsupportedCharsetException e) {
//            logger.log("");
//            logger.log("Couldn't decode the response body; charset is likely malformed.");
//            logger.log("<-- END HTTP");
//
//            return response;
//          }
//        }
//
//        if (!isPlaintext(buffer)) {
//          logger.log("");
//          logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
//          return response;
//        }
//
//        if (contentLength != 0) {
//          logger.log("");
//          logger.log(buffer.clone().readString(charset));
//        }
//
//        logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
//      }
//    }
//
//    return response;
//  }
//
//  static boolean isPlaintext(Buffer buffer) {
//    try {
//      Buffer prefix = new Buffer();
//      long byteCount = buffer.size() < 64 ? buffer.size() : 64;
//      buffer.copyTo(prefix, 0, byteCount);
//      for (int i = 0; i < 16; i++) {
//        if (prefix.exhausted()) {
//          break;
//        }
//        int codePoint = prefix.readUtf8CodePoint();
//        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
//          return false;
//        }
//      }
//      return true;
//    } catch (EOFException e) {
//      return false; // Truncated UTF-8 sequence.
//    }
//  }
//
//  private boolean bodyEncoded(Headers headers) {
//    String contentEncoding = headers.get("Content-Encoding");
//    return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
//  }
//}
